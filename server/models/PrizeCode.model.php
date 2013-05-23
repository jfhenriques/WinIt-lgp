<?php

	class PrizeCode
		extends ActiveRecord
		implements SavableActiveRecord {

		const TABLE_NAME = "prizecode";

		

		public function getPCID()
		{
			return (int)$this->getData('pcid');
		}

		public function getEmissionDate()
		{
			return (int)$this->getData('emiss_date');
		}
		public function setEmissionDate($date)
		{
			$this->data['emiss_date'] = $date;
		}

		public function getUtilizationDate()
		{
			return (int)$this->getData('util_date');
		}
		public function setUtilizationDate($date)
		{
			$this->data['util_date'] = $date;
		}

		public function getOwnerUID()
		{
			return (int)$this->getData('cur_uid');
		}
		public function setOwnerUID($uid)
		{
			$this->data['cur_uid'] = $uid;
		}

		public function getCode()
		{
			return $this->getData('valid_code');
		}
		// public function setCode($code)
		// {
		// 	$this->data['valid_code'] = $code;
		// }

		public function inTrading()
		{
			return (bool)$this->getData('in_trading');
		}
		public function setTrading($state)
		{
			$this->data['in_trading'] = (bool)$state;
		}

		public function getUPID()
		{
			return (int)$this->getData('upid');
		}
		public function setUPID($upid)
		{
			$this->data['upid'] = $upid;
		}

		/* START: Only available on tradable finds, for performance boost */

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
				return is_null( $img ) ? null : ( PROM_IMG_SRC_DIR . $img );
			}

		/* END */


		public function genValidCode($binaryPass)
		{
			if( strlen($binaryPass) !== 32 )
				return null;

			$iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
			$iv = mcrypt_create_iv($iv_size, MCRYPT_RAND);

			$bytes = "yay, a code!!4567890123456789012";

			$ciphertext = rtrim(mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $binaryPass, $bytes, MCRYPT_MODE_CBC, $iv), "\0");
			
			$this->data['valid_code'] = base64_encode($iv . $ciphertext);
		}


		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			$pcid = $this->getPCID();
			$isInsert = ( $pcid <= 0 ) ;

			if( $isInsert )
			{
				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (emiss_date, util_date, cur_uid, valid_code, upid) ' .
										' VALUES(:emiss_date, :util_date, :cur_uid, :valid_code, :upid);');

				$upid = $this->getUPID();
				$sth->bindParam(':upid', $upid, PDO::PARAM_INT);
			}

			else
			{
				$sth = $dbh->prepare('UPDATE ' . self::TABLE_NAME . ' SET emiss_date = :emiss_date , util_date = :util_date, cur_uid = :cur_uid,' .
												' valid_code = :valid_code, in_trading = :in_trading WHERE pcid = :pcid ;' );

				$inTrading = $this->inTrading();
				$sth->bindParam(':in_trading', $inTrading, PDO::PARAM_INT);
			}

			$emiss_date = $this->getEmissionDate();
			$util_date = $this->getUtilizationDate();
			$cur_uid = $this->getOwnerUID();
			$valid_code = $this->getCode();

			$sth->bindParam(':emiss_date', $emiss_date, PDO::PARAM_INT);
			$sth->bindParam(':util_date', $util_date, PDO::PARAM_INT);
			$sth->bindParam(':cur_uid', $cur_uid, PDO::PARAM_INT);
			$sth->bindParam(':valid_code', $valid_code, PDO::PARAM_STR);
			
			$ret = $sth->execute();
			
			if( $ret && $isInsert )
				$this->data['pcid'] = $dbh->lastInsertId();

			return $ret;
		}


		public static function _fillTradablePrizes($prizArr)
		{
			$ret = array();

			if( !is_null( $prizArr ) && is_array( $prizArr ) )
			{
				foreach($prizArr as $p)
				{
					$ret[] = array('pcid' => $p->getPCID(),
								   'pid' => $p->getPID(),
								   'max_util_date' => $p->getMaxUtilizationDate(),
								   'name' => $p->getPromotionName(),
								   'image' => Controller::formatURL( $p->getPromotionImageSRC() ),
						);

				}

			}

			return $ret;
		}


		public static function findOwnTrading($uid, $time = null)
		{
			return self::_findTradable($uid, true, 1, $time);
		}
		public static function findOwnTradable($uid, $time = null)
		{
			return self::_findTradable($uid, true, 0, $time);
		}
		public static function findOthersTradable($uid, $time = null)
		{
			return self::_findTradable($uid, false, 1, $time);
		}


		private static function _findTradable($uid, $owned, $inTrading, $time)
		{
			$prizes = array();
			$time = is_null( $time ) ? time() : $time ;

			$return = static::executeQuery( 'SELECT pc.pcid AS pcid, pc.emiss_date AS emiss_date, pc.util_date AS util_date, ' .
											' pc.cur_uid AS cuir_uid, pc.valid_code AS valid_code, pc.in_trading AS in_trading, ' .
											' pc.upid AS upid, up.uid AS o_uid, up.pid AS pid, p.util_date AS p_util_date, ' .
											' p.name AS p_name, p.image AS p_image ' .
											' FROM ' . self::TABLE_NAME .' AS pc ' .
											' INNER JOIN ' . UserPromotion::TABLE_NAME . ' AS up ON (up.upid = pc.upid)' .
											' INNER JOIN ' . Promotion::TABLE_NAME . ' AS p ON (p.pid = up.pid)' .
											' WHERE p.transferable = 1 AND p.active = 1 AND ( p.util_date = 0 OR p.util_date > ? ) ' .
											' AND up.end_date > 0 AND up.state = 1 AND pc.util_date = 0 ' .
											' AND pc.in_trading = ? AND pc.cur_uid ' . ( $owned ? '=' : '<>' ) . ' ? ;',
									  			array(  $time, $inTrading, $uid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new PrizeCode() ) ) )
						$prizes[] = $res ;
				}
			}

			return $prizes;
		}


		public static function findByPCID($pcid)
		{
			$result = static::query( 'SELECT pcid, emiss_date, util_date, cur_uid, valid_code, in_trading, upid' .
											' FROM ' . self::TABLE_NAME . ' WHERE pcid = ? ;',
									  			array(  $pcid ) );

			return static::fillModel( $result, new Promotion() );
		}

		/*public static function sendPromoToTrading($pcid, $uid) 
		{			
			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			$sth = $dbh->prepare('PROEDURE(?, ?);');//update prizecode SET in_trading = 1 where prizecode.upid = (select userpromotion.upid from userpromotion where userpromotion.uid = :uid and userpromotion.pid = :pid);' );
			
			$sth->bindParam(':uid', $uid, PDO::PARAM_INT);
			$sth->bindParam(':pid', $pid, PDO::PARAM_INT);
						
			$ret = $sth->execute(); // try catch PDOException 
			*/
			/*$result = static::query( 'SELECT pcid, emiss_date, util_date, cur_uid, valid_code, in_trading, upid' .
											' FROM prizecode WHERE prizecode.upid = (select userpromotion.upid from userpromotion where userpromotion.uid = ? and userpromotion.pid = ?);',
									  			array(  $uid, $pid ) );*/
		/*	
			// return static::fillModel( $ret, new PrizeCode() );
			return $ret;
		}*/


	}

?>