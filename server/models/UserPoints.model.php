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
			return (int)$this->getData('data_aquis');
		}		

		public function getXPPoints()
		{
			return $this->getData('xp_points');
		}		
		
		public function getPID()
		{
			return $this->getData('pid');
		}	
		
		public function getUID()
		{
			return $this->getData('uid');
		}	
		
		public function setDataAquis($data_aquis)
		{
			$this->data['data_aquis'] = $data_aquis;
		}
		
		public function setXPPoints($xp_points)
		{
			$this->data['xp_points'] = $points;
		}

		public static function instantiate(User $user, Promotion $promotion, $ratio, $time = null)
		{
			$points = new UserPoints();

			if( !is_null( $user ) && !is_null( $promotion ) && !is_null( $ratio ))
			{
				$this->isInsert = true;

				$points->data['uid'] = $user->getUID();
				$points->data['pid'] = $promotion->getPID();

				$points->data['aquis_date'] = is_null( $time ) ? time() : $time ;
				
				$points = (int)($ratio*$promotion->getWinPoints());
			}

			return $points;
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
		
		public static function showUserPoints($uid) {
		
			$result = static::query( 'SELECT SUM(xp_points) AS pts FROM '. self::TABLE_NAME . ' WHERE uid = ? LIMIT 1;',
									  array( $uid ) );
			
			return $result;
			
		}

	}

?>