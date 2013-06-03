<?php



	class Promotion extends ActiveRecord {

		
		const TABLE_NAME = 'promotion' ;
		const TABLE_NAME_RET = 'retailer' ;
		const TABLE_NAME_TYPE = 'promotiontype' ;

		const KEY_PROMOTIONS = "sql.promotions";

		const TYPE_QUIZ_GAME = 1;
		const TYPE_PROXIMITY_ALERT = 2;


		const RET_IMG_SRC_DIR = 'img/eef8ac0ae5c57b53321cc1b14e34bc7b7494c649/';
		const PROM_IMG_SRC_DIR = 'img/0bb53157538fde36def76790f3d674969cba5218/';


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

		
		public function getInitDate()
		{
			return (int)$this->getData('init_date');
		}
		
		public function getEndDate()
		{
			return (int)$this->getData('end_date');
		}

		public function getMaxUtilizationDate()
		{
			return (int)$this->getData('util_date');
		}
		
		public function getGrandLimit()
		{
			return (int)$this->getData('grand_limit');
		}

		public function getUserLimit()
		{
			return (int)$this->getData('user_limit');
		}
		
		public function getValidCoord()
		{
			return $this->getData('valid_coord');
		}
		
		public function getValidCoordRadius()
		{
			return $this->getData('valid_coord_radius');
		}
		
		public function isTransferable()
		{
			return (boolean)$this->getData('transferable');
		}

		public function isActive()
		{
			return (boolean)$this->getData('active');
		}

		
		public function getWinPoints()
		{
			return (int)$this->getData('win_points');
		}
		
		public function getPTID()
		{
			return $this->getData('ptid');
		}

		
		public function getFuncType()
		{
			return (int)$this->getData('func_type');
		}


		public function getDescription()
		{
			return $this->getData('description');
		}

		public function getImage()
		{
			return $this->getData('image');
		}

		public function getImageSRC()
		{
			$img = $this->getData('image');
			return is_null( $img ) ? null : ( self::PROM_IMG_SRC_DIR . $img );
		}


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
			return is_null( $img ) ? null : ( self::RET_IMG_SRC_DIR . $img );
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

		/* --- ONLY AVAILABLE WITH User::findByUIDWithPoints( UID ) --- */

			public function getActiveUPID()
			{
				return (int)$this->getData('active_upid');
			}

		/* --- */


		
		public static function findByPID($pid, $time = null, $active = true)
		{
			$cc = CommonCache::getInstance();
			$time = is_null( $time ) ? time() : $time ;
			
			$sql = $cc->get( self::KEY_PROMOTIONS );

			if( $sql === false )
			{
				$sql = 'SELECT p.pid AS pid, p.active AS active, p.name AS name, p.init_date AS init_date, p.util_date AS util_date, ' .
					   ' p.end_date AS end_date, p.grand_limit AS grand_limit, p.user_limit AS user_limit, ' .
					   ' p.valid_coord AS valid_coord, p.valid_coord_radius AS valid_coord_radius, ' .
					   ' p.transferable AS transferable, p.win_points AS win_points, p.func_type AS func_type ,' .
					   ' p.rid AS rid, p.ptid AS ptid, r.name AS ret_name, t.name AS prom_type, r.image AS ret_image, ' .
					   ' p.desc AS description, p.image AS image ' .
					   ' FROM ' . self::TABLE_NAME . ' AS p ' .
					   ' INNER JOIN ' . self::TABLE_NAME_RET . ' AS r ON(r.rid = p.rid) ' .
					   ' INNER JOIN ' . self::TABLE_NAME_TYPE . ' AS t ON(t.ptid = p.ptid) ' .
					   ' WHERE p.pid = ? AND p.active = ? AND ( p.end_date = 0 OR p.end_date >= ? ) LIMIT 1;' ;

				$cc->set( self::KEY_PROMOTIONS, $sql );
			}

			$result = static::query( $sql, array( $pid, $active ? 1 : 0, $time ) );

			return static::fillModel( $result, new Promotion() );
		}


		public static function findValidPromotions($uid)
		{
			$promos = array();
			$return = static::executeQuery( 'CALL proc_avail_prom(?);', array( $uid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new Promotion() ) ) )
						$promos[] = $res ;
				}
			}

			return $promos;
		}
	}
