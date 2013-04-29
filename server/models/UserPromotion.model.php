<?php

	class UserPromotion
		extends ActiveRecord
		implements SavableActiveRecord {


		const TABLE_NAME = "userpromotion";

		const STATE_UNKNOWN		  = 0;
		const STATE_WON			  = 1;




		// public function __construct(User $user, Promotion $promotion)
		// {

		// }

		public function getUPID()
		{
			return (int)$this->getData('upid');
		}
		public function getPID()
		{
			return (int)$this->getData('pid');
		}
		public function getUID()
		{
			return (int)$this->getData('uid');
		}

		public function getState()
		{
			return (int)$this->getData('state', 0);
		}
		public function setState($state)
		{
			$this->data['state'] = $state;
		}

		public function getInitDate()
		{
			return (int)$this->getData('init_date');
		}
		public function setInitDate($date)
		{
			$this->data['init_date'] = $date;
		}

		public function getEndDate()
		{
			return (int)$this->getData('end_date');
		}
		public function setEndDate($date)
		{
			$this->data['end_date'] = $date;
		}


		public function inMotion()
		{
			return ( $this->getInitDate() > 0 && $this->getEndDate() === 0 ) ;
		}

		public function getPromotion()
		{
			$pid = $this->getPid();

			return $pid > 0 ? Promotion::findByPID( $pid ) : null ;
		}


		


		public static function findByUPID($upid)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE upid = ? LIMIT 1 ;',
									  array( $upid ) );
							
			return static::fillModel( $result, new UserPromotion() );
		}

		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();
			$sth = null;

			$upid = $this->getUPID();
			$isInsert = is_null( $upid );
			
			$init_date = $this->getInitDate();
			$end_date = $this->getEndDate();
			$state = $this->getState();
			
			if( $isInsert )
			{
				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (uid,pid,init_date,end_date,state) ' .
												' VALUES(:uid,:pid,:init_date,:end_date,:state); ');

				$pid = $this->getPID();
				$uid = $this->getUID();

				$sth->bindParam(':pid', $pid, PDO::PARAM_INT);
				$sth->bindParam(':uid', $uid, PDO::PARAM_INT);
			}
			else
			{
				$sth = $dbh->prepare('UPDATE ' . self::TABLE_NAME . ' SET init_date = :init , end_date = :end , state = :state WHERE upid = :upid ;' );
				
				$sth->bindParam(':upid', $upid, PDO::PARAM_INT);
			}

			$sth->bindParam(':init', $init_date, PDO::PARAM_INT);
			$sth->bindParam(':end', $end_date, PDO::PARAM_INT);
			$sth->bindParam(':state', $state, PDO::PARAM_INT);
			
			$ret = $sth->execute();
			
			if( $ret && $isInsert )
				$this->data['upid'] = $dbh->lastInsertId();

			return $ret;

		}


	}

?>