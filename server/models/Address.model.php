<?php

	class Address extends ActiveRecord {

		// private $adid;
		// private $cp4;
		// private $cp3;

		// private $district;
		// private $locality;

		// private $street;


		const TABLE_NAME_CP  = "address";
		const TABLE_NAME_LOC = "cplocality";
		const TABLE_NAME_DIS = "cpdistrict";
		const TABLE_NAME_STR = "addrstreet";


		public function getID()
		{
			return $this->getData('adid');
		}
		public function getCP4()
		{
			return $this->getData('cp4');
		}
		public function getCP3()
		{
			return $this->getData('cp3');
		}

		public function getDistrict()
		{
			return $this->getData('district');
		}
		public function getLocality()
		{
			return $this->getData('locality');
		}

		public function getStreet()
		{
			return $this->getData('street');
		}


		// public static function fillAddress( $arr )
		// {
		// 	if( is_array( $arr ) && count( $arr ) > 0 )
		// 	{
		// 		$addr = new Address();
				
		// 		$addr->adid = $arr['adid'];
		// 		$addr->cp4 = $arr['cp4'];
		// 		$addr->cp3 = $arr['cp3'];

		// 		$addr->district = $arr['district'];
		// 		$addr->locality = $arr['locality'];

		// 		$addr->street = $arr['street'];
				
		// 		return $addr;
		// 	}
			
		// 	return null;
		// }

		public static function findByCP($cp4, $cp3, $hint = null)
		{
			$streets = array();
			$hint = '%' . ( is_null( $hint ) ? '' : $hint ) . '%' ;

			$return = static::executeQuery( 'SELECT a.adid AS adid, a.cp4 AS cp4, a.cp3 AS cp3, s.street AS street, ' .
										' l.locality AS locality, d.district AS district ' .
										' FROM '. self::TABLE_NAME_CP . ' AS a ' .
										' INNER JOIN '. self::TABLE_NAME_LOC . ' AS l ON ( l.llll = a.llll ) ' .
										' INNER JOIN '. self::TABLE_NAME_DIS . ' AS d ON ( d.dd = l.dd ) ' .
										' INNER JOIN '. self::TABLE_NAME_STR . ' AS s ON ( s.stid = a.stid ) ' .
										' WHERE a.cp4 = ? AND a.cp3 = ? AND s.street LIKE ? ;',
									  array( $cp4, $cp3, $hint ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					// if( ( $res = static::fillAddress( $row ) ) !== null )
					// 	$streets[] = $res ;
					if( ( $res = static::fillModel( $row, new Address() ) ) !== null )
						$streets[] = $res ;
				}
			}

			return $streets;
		}

		public static function findByADID($adid)
		{

			$result = static::query( 'SELECT a.adid AS adid, a.cp4 AS cp4, a.cp3 AS cp3, s.street AS street, ' .
										' l.locality AS locality, d.district AS district ' .
										' FROM '. self::TABLE_NAME_CP . ' AS a ' .
										' INNER JOIN '. self::TABLE_NAME_LOC . ' AS l ON ( l.llll = a.llll ) ' .
										' INNER JOIN '. self::TABLE_NAME_DIS . ' AS d ON ( d.dd = l.dd ) ' .
										' INNER JOIN '. self::TABLE_NAME_STR . ' AS s ON ( s.stid = a.stid ) ' .
										' WHERE a.adid = ? ;',
									  array( $adid ) );

			return static::fillModel( $result, new Address() );
		}


		public function save() {}

	}

?>
