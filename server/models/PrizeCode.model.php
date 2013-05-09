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
			return $this->getData('upid');
		}
		public function setUPID($upid)
		{
			$this->data['upid'] = $upid;
		}

		public function genValidCode($binaryPass)
		{
			if( strlen($binaryPass) !== 32 )
				return null;

			$iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
			$iv = mcrypt_create_iv($iv_size, MCRYPT_RAND);

			$bytes = "yeah";

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



	}

?>