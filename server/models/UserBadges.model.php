<?php

	class UserBadges
		extends ActiveRecord
		implements SavableActiveRecord {


		const TABLE_NAME = "userbadges";

		const STATE_UNKNOWN		  = 0;
		const STATE_WON			  = 1;


		public function getUID()
		{
			return (int)$this->getData('uid');
		}
		public function getBID()
		{
			return (int)$this->getData('bid');
		}
		public function getAquisDate()
		{
			return (int)$this->getData('aquis_date');
		}

		public function setUID($uid)
		{
			$this->data['uid'] = $uid;
		}
		public function setBID($bid)
		{
			$this->data['bid'] = $bid;
		}
		public function setAquisDate($aquis_date)
		{
			$this->data['aquis_date'] = $aquis_date;
		}


		public function getBadge()
		{
			$bid = $this->getBID();

			return $bid > 0 ? Badge::findByBID( $bid ) : null ;
		}

		public static function findByUID($uid)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE uid = ? LIMIT 1 ;',
									  array( $uid ) );
							
			return static::fillModel( $result, new UserBadges() );
		}

		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();
			$sth = null;

			$bid = $this->getBID();
			$isInsert = $bid == 0;
			
			$uid = $this->getUID();
			$bid = $this->getBID();
			$aquis_date = $this->getAquisDate();

			if( $isInsert )
			{
				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (uid,bid,aquis_date) ' .
												' VALUES(:uid,:bid,:aquis_date); ');

				$bid = $this->getBID();
				$uid = $this->getUID();

				$sth->bindParam(':uid', $uid, PDO::PARAM_INT);
				$sth->bindParam(':bid', $bid, PDO::PARAM_INT);
			}
			else
			{
				$sth = $dbh->prepare('UPDATE ' . self::TABLE_NAME . ' SET aquis_date = :aquis_date WHERE bid = :bid ;' );
				
				$sth->bindParam(':bid', $bid, PDO::PARAM_INT);
			}

			$sth->bindParam(':aquis_date', $aquis_date, PDO::PARAM_INT);

			$ret = $sth->execute();
			
			if( $ret && $isInsert )
				$this->data['bid'] = $dbh->lastInsertId();

			return $ret;

		}

	}

?>