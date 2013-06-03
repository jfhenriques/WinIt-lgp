<?php

	class TradingSuggestion
		extends ActiveRecord
		implements SavableActiveRecord {

		const TABLE_NAME = "tradingsuggestion";

		const KEY_PRIZESUGGESTIONS = "sql.prizesuggestions";
		const KEY_SENT_PRIZESUGGESTIONS = "sql.sr.prizesuggestions.";

		

		private function __construct() { }


		public static function instantiate(PrizeCode $orig, PrizeCode $dest)
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
		public function getEndDate()
		{
			return (int)$this->getData('end_date');
		}
		public function setEndDate($date)
		{
			$this->data['end_date'] = $date;
		}


		public function getTransactionID()
		{
			return (int)$this->getData('transaction');
		}


		public function getState()
		{
			return (int)$this->getData('state');
		}
		public function setState($sate)
		{
			$this->data['state'] = $sate;
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
			public function getMaxUtilizationDate()
			{
				return (int)$this->getData('p_util_date');
			}
			public function getPromotionName()
			{
				return $this->getData('p_name');
			}
			public function getPromotionImageSRC()
			{
				$img = $this->getData('p_image');
				return is_null( $img ) ? null : ( Promotion::PROM_IMG_SRC_DIR . $img );
			}



			public function getPIDDest()
			{
				return (int)$this->getData('pid_o');
			}
			public function getMaxUtilizationDateDest()
			{
				return (int)$this->getData('p_util_date_o');
			}
			public function getPromotionNameDest()
			{
				return $this->getData('p_name_o');
			}
			public function getPromotionImageSRCDest()
			{
				$img = $this->getData('p_image_o');
				return is_null( $img ) ? null : ( Promotion::PROM_IMG_SRC_DIR . $img );
			}

		/* END */





		public function save()
		{
			$dbh = DbConn::getInstance()->getDB();

			$pcid_orig = $this->getPCIDOrig();
			$pcid_dest = $this->getPCIDDest();
			$date = $this->getDate();
			$end_date = $this->getEndDate();
			$transaction = $this->getTransactionID();
			$state = $this->getState();

			if( $pcid_orig <= 0 || $pcid_dest <= 0 )
				throw new Exception("Chave primária não definida");
			
			$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (pcid_orig,pcid_dest,date,end_date,transaction,state) ' .
								 ' VALUES(:pcid_orig, :pcid_dest, :date, :end_date, :transaction, :state) ' .
								 ' ON DUPLICATE KEY UPDATE state = :state, date = :date, end_date = :end_date ;');
			
			$sth->bindParam(':pcid_orig', $pcid_orig, PDO::PARAM_INT);
			$sth->bindParam(':pcid_dest', $pcid_dest, PDO::PARAM_INT);
			$sth->bindParam(':date', $date, PDO::PARAM_INT);
			$sth->bindParam(':end_date', $end_date, PDO::PARAM_INT);
			$sth->bindParam(':transaction', $transaction, PDO::PARAM_INT);
			$sth->bindParam(':state', $state, PDO::PARAM_INT);

			return $sth->execute();
		}



		public static function findPrizeSuggestions( $uid, $state = 0, $sent = true, $time = null )
		{
			$prizes = array();
			$time = is_null( $time ) ? time() : $time ;

			if( !is_array( $state ) )
				$state = array( $state );

			$totalQ = count( $state );

			$params = array_merge( array( $time, $time, $uid ), $state );

			$base = 1000 + $totalQ;

			if( $sent )
				$base += 100;

			$key = self::KEY_SENT_PRIZESUGGESTIONS . $base ;

			$cc = CommonCache::getInstance();
			$sql = $cc->get( $key );

			if( $sql === false )
			{
				$qMarks = str_repeat( '?, ', ( $totalQ - 1 ) ) . '?';

				$sql = null;

				if( $sent )
				{
					$sql =  'SELECT pcs.pcid_orig AS pcid_orig, pcs.pcid_dest AS pcid_dest, pcs.date AS date, ' .
							' pcs.transaction AS transaction, pcs.state AS state,  ' .
							' p_my.pid AS pid, p_my.name AS p_name, p_my.util_date AS p_util_date, p_my.image AS p_image, ' .
							' p_o.pid AS pid_o, p_o.name AS p_name_o, p_o.util_date AS p_util_date_o, p_o.image AS p_image_o ' .
							' FROM ' . self::TABLE_NAME .' AS pcs ' .
							' INNER JOIN ' . PrizeCode::TABLE_NAME . ' AS pc_my ON (pc_my.pcid = pcs.pcid_orig) ' .
							' INNER JOIN ' . PrizeCode::TABLE_NAME . ' AS pc_o ON (pc_o.pcid = pcs.pcid_dest AND pc_o.transaction = pcs.transaction) ' .
							' INNER JOIN ' . UserPromotion::TABLE_NAME . ' AS up_my ON (up_my.upid = pc_my.upid) ' .
							' INNER JOIN ' . UserPromotion::TABLE_NAME . ' AS up_o ON (up_o.upid = pc_o.upid) ' .
							' INNER JOIN ' . Promotion::TABLE_NAME . ' AS p_my ON (p_my.pid = up_my.pid) ' .
							' INNER JOIN ' . Promotion::TABLE_NAME . ' AS p_o ON (p_o.pid = up_o.pid) ' .
							' WHERE p_my.transferable = 1 AND p_my.active = 1 AND ( p_my.util_date = 0 OR p_my.util_date > ? ) ' .
							' AND p_o.transferable = 1 AND p_o.active = 1 AND ( p_o.util_date = 0 OR p_o.util_date > ? ) ' .
							' AND pc_my.util_date = 0 AND pc_o.util_date = 0 ' .
							' AND pc_my.cur_uid = ? AND pc_my.in_trading = 0 AND pc_o.in_trading = 1 ' .
							' AND pcs.state IN ( ' . $qMarks . ' ) ' .
							' ORDER BY pcs.state DESC ;' ;
				}
				else
				{
					$sql =  'SELECT pcs.pcid_orig AS pcid_orig, pcs.pcid_dest AS pcid_dest, pcs.date AS date, ' .
							' pcs.transaction AS transaction, pcs.state AS state,  ' .
							' p_my.pid AS pid, p_my.name AS p_name, p_my.util_date AS p_util_date, p_my.image AS p_image, ' .
							' p_o.pid AS pid_o, p_o.name AS p_name_o, p_o.util_date AS p_util_date_o, p_o.image AS p_image_o ' .
							' FROM ' . self::TABLE_NAME .' AS pcs ' .
							' INNER JOIN ' . PrizeCode::TABLE_NAME . ' AS pc_my ON (pc_my.pcid = pcs.pcid_dest AND pc_my.transaction = pcs.transaction) ' .
							' INNER JOIN ' . PrizeCode::TABLE_NAME . ' AS pc_o ON (pc_o.pcid = pcs.pcid_orig) ' .
							' INNER JOIN ' . UserPromotion::TABLE_NAME . ' AS up_my ON (up_my.upid = pc_my.upid) ' .
							' INNER JOIN ' . UserPromotion::TABLE_NAME . ' AS up_o ON (up_o.upid = pc_o.upid) ' .
							' INNER JOIN ' . Promotion::TABLE_NAME . ' AS p_my ON (p_my.pid = up_my.pid) ' .
							' INNER JOIN ' . Promotion::TABLE_NAME . ' AS p_o ON (p_o.pid = up_o.pid) ' .
							' WHERE p_my.transferable = 1 AND p_my.active = 1 AND ( p_my.util_date = 0 OR p_my.util_date > ? ) ' .
							' AND p_o.transferable = 1 AND p_o.active = 1 AND ( p_o.util_date = 0 OR p_o.util_date > ? ) ' .
							' AND pc_my.util_date = 0 AND pc_o.util_date = 0 ' .
							' AND pc_my.cur_uid = ? AND pc_my.in_trading = 1 AND pc_o.in_trading = 0 ' .
							' AND pcs.state IN ( ' . $qMarks . ' ) ' .
							' ORDER BY pcs.state DESC ;' ;
				}

				$cc->set( $key, $sql );
			}

			$return = static::executeQuery( $sql, $params , $stmt );
			
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

		public static function findSentPrizeSuggestions( $uid, $state = 0, $time = null )
		{
			return TradingSuggestion::findPrizeSuggestions( $uid, $state, true, $time );
		}
		public static function findReceivedPrizeSuggestions( $uid, $state = 0, $time = null )
		{
			return TradingSuggestion::findPrizeSuggestions( $uid, $state, true, $time );
		}



		public static function findByTransaction($trading, $transaction, $suggestion)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE pcid_dest = ? AND transaction = ? AND pcid_orig = ? LIMIT 1;',
									  array( $trading, $transaction, $suggestion ) );

							
			return static::fillModel( $result, new TradingSuggestion() );
		}


	}
