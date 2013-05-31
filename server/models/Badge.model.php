<?php


	class Badge
		extends ActiveRecord {
		

		const TABLE_NAME = 'badges' ;

		const BADGE_IMG_SRC_DIR = 'img/9cc030d4cccab8c52273613ef010120eb9e3228c/';

		const KEY_USERBADGES = 'sql.userbadges';


		//public function __construct() {}

		
		/* GET's and SET's*/

		public function getBID()
		{
			return (int)$this->getData('bid');
		}
		

		public function getName()
		{
			return $this->getData('name');
		}		
		public function setName($name)
		{
			$this->data['name'] = $name;
		}
		

		public function getImage()
		{
			return $this->getData('image');
		}
		public function setImage($image)
		{
			$this->data['image'] = $image;
		}
		
		public function getImageSRC()
		{
			$img = $this->getData('image');
			return is_null( $img ) ? null : ( self::BADGE_IMG_SRC_DIR . $img );
		}

		public function getDescription()
		{
			return $this->getData('description');
		}
		public function setDescription($description)
		{
			$this->data['description'] = $description;
		}


		public function getAquisDate()
		{
			return (int)$this->getData('aquis_date');
		}

		
		public static function findByBID($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE bid = ? LIMIT 1;',
									  array( $id ) );

			return static::fillModel( $result, new Badge() );
		}

		public static function findByUID($uid)
		{
			$badges = array();

			$cc = CommonCache::getInstance();
			$sql = $cc->get( self::KEY_USERBADGES );

			if( $sql === false )
			{
				$sql =  'SELECT b.bid AS bid, b.name AS name, b.image AS image, ub.aquis_date AS aquis_date, b.description AS description '.
						' FROM ' . self::TABLE_NAME . ' AS b '.
						' INNER JOIN ' . UserBadges::TABLE_NAME . ' AS ub ON (ub.bid = b.bid) '.
						' INNER JOIN ' . User::TABLE_NAME . ' AS u ON (u.uid = ub.uid) '.
						' WHERE u.uid = ?;';

				$cc->set( self::KEY_USERBADGES, $sql );
			}

			$return = static::executeQuery( $sql, array( $uid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new Badge() ) ) )
						$badges[] = $res ;
				}
			}
			return $badges;	
		}
	}

?>