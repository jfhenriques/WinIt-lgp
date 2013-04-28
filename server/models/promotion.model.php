<?php

	DEFINE('ITEM_RET_SRC_DIR', 'img/04e36748dd36c92422c3a300db9897ceb4c4cd48/');

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
		const TABLE_NAME_RET = 'retailer' ;
		const TABLE_NAME_TYPE = 'promotiontype' ;



		const TYPE_QUIZ_GAME = 1;
		const TYPE_PROXIMITY_ALERT = 2;


		private function __construct() { }

		
		/* GET's and SET's*/

		public function getPID()
		{
			return (int)$this->getData('pid');
		}
		public function getRID()
		{
			return (int)$this->getData('rid');
		}


	
		public function getName()
		{
			return $this->getData('name');
		}
		// public function setName($name)
		// {
		// 	$this->data['name'] = $name;
		// }

		
		public function getInitDate()
		{
			return (int)$this->getData('init_date');
		}
		// public function setInitDate($init_date)
		// {
		// 	$this->data['init_date'] = $init_date;
		// }
		
		public function getEndDate()
		{
			return (int)$this->getData('end_date');
		}
		// public function setEndDate($end_date)
		// {
		// 	$this->data['end_date'] = $end_date;
		// }
		
		public function getGrandLimit()
		{
			return (int)$this->getData('grand_limit');
		}
		// public function setGrandLimit($grand_limit)
		// {
		// 	$this->data['grand_limit'] = $grand_limit;
		// }

		public function getUserLimit()
		{
			return (int)$this->getData('user_limit');
		}
		// public function setUserLimit($user_limit)
		// {
		// 	$this->data['user_limit'] = $user_limit;
		// }
		
		public function getValidCoord()
		{
			return $this->getData('valid_coord');
		}
		// public function setValidCoord($valid_coord)
		// {
		// 	$this->data['valid_coord'] = $valid_coord;
		// }
		
		public function getValidCoordRadius()
		{
			return $this->getData('valid_coord_radius');
		}
		// public function setValidCoordRadius($valid_coord_radius)
		// {
		// 	$this->data['valid_coord_radius'] = $valid_coord_radius;
		// }
		
		public function isTransferable()
		{
			return (boolean)$this->getData('transferable');
		}
		// public function setTransferable($transferable)
		// {
		// 	$this->data['transferable'] = $transferable;
		// }
		

		public function isActive()
		{
			return (boolean)$this->getData('active');
		}
		// public function setActive($active)
		// {
		// 	$this->data['active'] = $active;
		// }
		
		public function getWinPoints()
		{
			return (int)$this->getData('win_points');
		}
		// public function setWinPoints($win_points)
		// {
		// 	$this->data['win_points'] = $win_points;
		// }
		
		public function getPTID()
		{
			return $this->getData('ptid');
		}
		// public function setPtid($ptid)
		// {
		// 	$this->data['ptid'] = $ptid;
		// }
		
		
		public function getFuncType()
		{
			return (int)$this->getData('func_type');
		}
		// public function setFuncType($func_type)
		// {
		// 	$this->data['func_type'] = $func_type;
		// }

		public function getRetailerName()
		{
			return $this->getData('ret_name');
		}

		public function geRetailerImage()
		{
			return $this->getData('ret_image');
		}

		public function getRetailerImageSRC()
		{
			$img = $this->getData('ret_image');
			return is_null( $img ) ? null : ( ITEM_RET_SRC_DIR . $img );
		}

		public function getPromotionType()
		{
			return $this->getData('prom_type');
		}
		

		public function getItems()
		{
			$pid = $this->getPid();

			return $pid > 0 ? ItemPromotion::findByPID( $pid ) : array() ;
		}

		
		public static function findByPID($pid)
		{
			$result = static::query( 'SELECT p.pid AS pid, p.active AS active, p.name AS name, p.init_date AS init_date, ' .
									  ' p.end_date AS end_date, p.grand_limit AS grand_limit, p.user_limit AS user_limit, ' .
									  ' p.valid_coord AS valid_coord, p.valid_coord_radius AS valid_coord_radius, ' .
									  ' p.transferable AS transferable, p.win_points AS win_points, p.func_type AS func_type ,' .
									  ' p.rid AS rid, p.ptid AS ptid, r.name AS ret_name, t.name AS prom_type, r.image AS ret_image  ' .
									  ' FROM ' . self::TABLE_NAME . ' AS p ' .
									  'INNER JOIN ' . self::TABLE_NAME_RET . ' AS r ON(r.rid = p.rid) ' .
									  'INNER JOIN ' . self::TABLE_NAME_TYPE . ' AS t ON(t.ptid = p.ptid) ' .
									  'WHERE p.pid = ? AND p.active = 1 LIMIT 1;',
									  array( $pid ) );

			return static::fillModel( $result, new Promotion() );
		}

		public static function findValidPromotions($uid)
		{
			$promos = array();
			$return = static::executeQuery( 'SELECT * FROM ' . self::TABLE_NAME . ' WHERE pid = ? ORDER BY qid ASC;',
									  			array( $pid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new QuizGameQuestion() ) ) )
						$promos[] = $res ;
				}
			}

			return $promos;
		}
	}

?>