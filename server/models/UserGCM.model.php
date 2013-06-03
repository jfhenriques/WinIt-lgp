<?php

	class UserGCM
		extends ActiveRecord
		implements SavableActiveRecord, DeletableActiveRecord {

		const TABLE_NAME = "usergcm";

		private $isInsert = false;

		public static function instantiate($uid, $token, $time = null)
		{
			$gcm = new UserGCM();

			if( !is_null( $uid ) && !is_null( $token ) )
			{
				$gcm->isInsert = true;

				$gcm->data['uid'] = $uid;
				$gcm->data['gcm'] = $token;

				$gcm->data['date'] = is_null( $time ) ? time() : $time ;
			}

			return $gcm;
		}



		public function getUID()
		{
			return (int)$this->getData('uid');
		}

		public function getTokenGCM()
		{
			return $this->getData('gcm');
		}

		public function getDate()
		{
			return (int)$this->getData('date');
		}



		public static function findByUID($uid)
		{
			$gcms = array();

			$return = static::executeQuery( 'SELECT * FROM ' . self::TABLE_NAME . ' WHERE uid = ? ;',
									  			array( $uid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new UserGCM() ) ) )
						$gcms[] = $res ;
				}
			}

			return $gcms;
		}

		public static function findByToken($token)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE gcm = ? LIMIT 1;',
									  array( $token ) );

			return static::fillModel( $result, new UserGCM() );
		}

		public static function deleteByUID($uid)
		{
			return static::query( 'DELETE FROM '. self::TABLE_NAME . ' WHERE uid = ? ;', array( $uid ) );
		}


		public function save()
		{
			if( $this->isInsert )
			{
				$dbh = DbConn::getInstance()->getDB();

				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (uid,gcm,date) VALUES (:uid, :gcm, :date);');

				$uid = $this->getUID();
				$gcm = $this->getTokenGCM();
				$date = $this->getDate();
		
				$sth->bindParam(':uid', $uid, PDO::PARAM_INT );
				$sth->bindParam(':gcm', $gcm, PDO::PARAM_STR );
				$sth->bindParam(':date', $date, PDO::PARAM_INT );

				return  $sth->execute();
			}

			return false;
		}

		public function delete()
		{
			if( !$this->isInsert )
			{
				$dbh = DbConn::getInstance()->getDB();
				
				$sth = $dbh->prepare('DELETE FROM ' . self::TABLE_NAME . ' WHERE uid = :uid AND gcm = :gcm ;');

				$uid = $this->getUID();
				$gcm = $this->getTokenGCM();
		
				$sth->bindParam(':uid', $uid, PDO::PARAM_INT );
				$sth->bindParam(':gcm', $gcm, PDO::PARAM_STR );

				return $sth->execute();
			}

			return false;
		}


	}
