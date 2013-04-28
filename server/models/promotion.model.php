<?php

	class Promotion extends ActiveRecord {
	
		// private $pid = null;
		// private $name;
		// private $init_date;
		// private $end_date;
		// private $user_limit;
		// private $valid_coord;
		// private $valid_coord_radius;
		// private $transferable;
		// private $active;
		// private $win_points;
		// private $rid;
		// private $ptid;
		// private $func_type;
		// private $grand_limit;
		
		const TABLE_NAME = 'promotion' ;
		
		/* GET's and SET's*/

		public function getID()
		{
			return (int)$this->getData('pid');
		}
	
		public function getName()
		{
			return $this->getData('name');
		}
		public function setName($name)
		{
			$this->data['name'] = $name;
		}
		
		public function getInit_date()
		{
			return $this->getData('init_date');
		}
		public function setInit_date($init_date)
		{
			$this->data['init_date'] = $init_date;
		}
		
		public function getEnd_date()
		{
			return $this->getData('end_date');
		}
		public function setEnd_date($end_date)
		{
			$this->data['end_date'] = $end_date;
		}
		
		public function getUser_limit()
		{
			return $this->getData('user_limit');
		}
		public function setUser_limit($user_limit)
		{
			$this->data['user_limit'] = $user_limit;
		}
		
		public function getValid_coord()
		{
			return $this->getData('valid_coord');
		}
		public function setValid_coord($valid_coord)
		{
			$this->data['valid_coord'] = $valid_coord;
		}
		
		public function getValid_coord_radius()
		{
			return $this->getData('valid_coord_radius');
		}
		public function setValid_coord_radius($valid_coord_radius)
		{
			$this->data['valid_coord_radius'] = $valid_coord_radius;
		}
		
		public function getTransferable()
		{
			return $this->getData('transferable');
		}
		public function setTransferable($transferable)
		{
			$this->data['transferable'] = $transferable;
		}
		
		public function getActive()
		{
			return $this->getData('active');
		}
		public function setActive($active)
		{
			$this->data['active'] = $active;
		}
		
		public function getWin_points()
		{
			return $this->getData('win_points');
		}
		public function setWin_points($win_points)
		{
			$this->data['win_points'] = $win_points;
		}
		
		public function getRid()
		{
			return $this->getData('rid');
		}
		public function setRid($rid)
		{
			$this->data['rid'] = $rid;
		}
		
		public function getPtid()
		{
			return $this->getData('ptid');
		}
		public function setPtid($ptid)
		{
			$this->data['ptid'] = $ptid;
		}
		
		
		public function getFunc_type()
		{
			return $this->getData('func_type');
		}
		public function setFunc_type($func_type)
		{
			$this->data['func_type'] = $func_type;
		}
		
		public function getGrand_limit()
		{
			return $this->getData('grand_limit');
		}
		public function setGrand_limit($grand_limit)
		{
			$this->data['grand_limit'] = $grand_limit;
		}	
		
	
		public function save()
		{
		}
		
		public static function findById($pid)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE pid = ? LIMIT 1;',
									  array( $pid ) );

			return static::fillModel( $result, new Promotion() );
		}
		
		public static function getPromotion($id) {
		
			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			$sth = $dbh->prepare('SELECT * FROM promotion WHERE promotion.pid = '. $id .';');
			
			$sth->execute();
			
			$ret = $sth->fetchAll();
			
			var_dump($ret);
			return $ret;	
		}
	}

?>