<?php
	
	class UserPoints
		extends ActiveRecord
		implements SavableActiveRecord {
		

		const TABLE_NAME = 'xppoints' ;
		
		private $isInsert = false;


		//public function __construct() {}

		
		/* GET's and SET's*/

		public function getDataAquis()
		{
			return (int)$this->getData('aquis_date');
		}		

		public function getXPPoints()
		{
			return (int)$this->getData('xp_points');
		}		
		
		public function getPID()
		{
			return $this->getData('pid');
		}	
		
		public function getUID()
		{
			return $this->getData('uid');
		}	
		



		public static function instantiate($uid, $pid, $points, $time = null)
		{
			$userPoints = new UserPoints();

			if( !is_null( $uid ) && !is_null( $pid ) && !is_null( $points ) )
			{
				$userPoints->isInsert = true;

				$userPoints->data['uid'] = $uid;
				$userPoints->data['pid'] = $pid;

				$userPoints->data['aquis_date'] = is_null( $time ) ? time() : $time ;

				$userPoints->data['xp_points'] = $points ;
			}

			return $userPoints;
		}
		
		public function save()
		{
			if( $this->isInsert )
			{
				$dbh = DbConn::getInstance()->getDB();

				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (uid, aquis_date, xp_points, pid) VALUES (:uid, :aquis_date, :xp_points, :pid);');

				$uid = $this->getUID();
				$aquis_date = $this->getDataAquis();
				$pid = $this->getPID();
				$xp_points = $this->getXPPoints();
		
				$sth->bindParam(':uid', $uid, PDO::PARAM_INT );
				$sth->bindParam(':aquis_date', $aquis_date, PDO::PARAM_STR );
				$sth->bindParam(':pid', $pid, PDO::PARAM_INT );
				$sth->bindParam(':xp_points', $xp_points, PDO::PARAM_INT );

				return  $sth->execute();
			}

			return false;
		}
		
		public static function findByUID($uid)
		{
			$userpoints = array();

			$return = static::executeQuery( 'SELECT * FROM ' . self::TABLE_NAME . ' WHERE uid = ? ;',
									  			array( $uid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new UserPoints() ) ) )
						$userpoints[] = $res ;
				}
			}

			return $userpoints;
		}
		
	}
