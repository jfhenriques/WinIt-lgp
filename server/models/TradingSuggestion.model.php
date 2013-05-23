<?php

	class TradingSuggestion
		extends ActiveRecord
		implements SavableActiveRecord {

		const TABLE_NAME = "tradingsuggestion";

		

		private function __construct() { }


		public static function instanciate(PrizeCode $orig, PrizeCode $dest)
		{
			$suggestion = new TradingSuggestion();

			if( !is_null( $orig ) && !is_null( $dest ) )
			{
				$suggestion->data['pcid_orig'] = $orig->getPCID();
				$suggestion->data['pcid_dest'] = $dest->getPCID();

				$suggestion->data['transaction'] = $dest->getTransactionID();
			}

			return $suggestion;
		}



		public function getPCIDOrig()
		{
			return (int)$this->getData('pcid_orig');
		}
		public function getPCIDDest()
		{
			return (int)$this->getData('pcid_dest');
		}


		public function getDate()
		{
			return (int)$this->getData('date');
		}
		public function setDate($date)
		{
			$this->data['date'] = $date;
		}


		public function getTransactionID()
		{
			return (int)$this->getData('transaction');
		}


		public function getState()
		{
			return (int)$this->getData('sate');
		}
		public function setState($sate)
		{
			$this->data['sate'] = $sate;
		}




		/* START: Only available on suggestions finds, for performance boost */

			public function getOriginalUID()
			{
				return (int)$this->getData('o_uid');
			}
			public function getPID()
			{
				return (int)$this->getData('pid');
			}
			public function getPromotionName()
			{
				return $this->getData('p_name');
			}
			public function getPromotionImageSRC()
			{
				$img = $this->getData('p_image');
				return is_null( $img ) ? null : ( PROM_IMG_SRC_DIR . $img );
			}

		/* END */





		public function save()
		{
			$dbh = DbConn::getInstance()->getDB();

			$pcid_orig = $this->getPCIDOrig();
			$pcid_dest = $this->getPCIDDest();
			$date = $this->getDate();
			$transaction = $this->getTransactionID();
			$state = $this->getState();

			if( $pcid_orig <= 0 || $pcid_dest <= 0 )
				throw new Exception("Chave primária não definida");
			
			$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (pcid_orig,pcid_dest,date,transaction,state) ' .
								 ' VALUES(:pcid_orig, :pcid_dest, :date, :transaction, :state) ' .
								 ' ON DUPLICATE KEY UPDATE state = :state, date = :date ;');
			
			$sth->bindParam(':pcid_orig', $pcid_orig, PDO::PARAM_INT);
			$sth->bindParam(':pcid_dest', $pcid_dest, PDO::PARAM_INT);
			$sth->bindParam(':date', $date, PDO::PARAM_INT);
			$sth->bindParam(':transaction', $transaction, PDO::PARAM_INT);
			$sth->bindParam(':state', $state, PDO::PARAM_INT);

			return $sth->execute();
		}



		public static function findPrizeSuggestions($pcid,$time,$restrict = null)
		{
			$prizes = array();
			$time = is_null( $time ) ? time() : $time ;

			$params = array( $time, $pcid );

			if( !is_null($restrict) )
				$params[] = $restrict;

			$return = static::executeQuery( 'SELECT pcs.pcid_orig AS pcid_orig, pcs.pcid_dest AS pcid_dest, pcs.date AS date, ' .
											' pcs.transaction AS transaction, pc.in_trading AS in_trading, ' .
											' pc.upid AS upid, up.uid AS o_uid, up.pid AS pid, p.util_date AS p_util_date, ' .
											' p.name AS p_name, p.image AS p_image ' .
											' FROM ' . self::TABLE_NAME .' AS pcs ' .
											' INNER JOIN ' . PrizeCode::TABLE_NAME . ' AS pcF ON ( pcF.pcid = pcs.pcid_dest AND pcF.transaction = pcs.transaction )' .
											' INNER JOIN ' . PrizeCode::TABLE_NAME . ' AS pc ON (pc.pcid = pcs.pcid_orig)' .
											' INNER JOIN ' . UserPromotion::TABLE_NAME . ' AS up ON (up.upid = pc.upid)' .
											' INNER JOIN ' . Promotion::TABLE_NAME . ' AS p ON (p.pid = up.pid)' .
											' WHERE p.transferable = 1 AND p.active = 1 AND ( p.util_date = 0 OR p.util_date > ? ) ' .
											' AND pcs.pcid_dest = ? AND pc.util_date = 0 AND pc.in_trading = 0 ' . ( is_null( $restrict ) ? '' : ' AND pcs.pcid_orig = ? ' ) . ';',
									  			$params , $stmt );
			
			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new TradingSuggestion() ) ) )
						$prizes[] = $res ;
				}
			}

			return $prizes;
		}

	}

?>