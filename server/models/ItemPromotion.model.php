<?php

	class ItemPromotion extends ActiveRecord {


		const TABLE_NAME = "item";
		const TABLE_NAME_IP = "itempromotion";

		const ITEM_IMG_SRC_DIR = 'img/eef8ac0ae5c57b53321cc1b14e34bc7b7494c649/';



		private function __construct() { }


		public function getIID()
		{
			return (int)$this->getData('iid');
		}
		public function getName()
		{
			return $this->getData('name');
		}
		public function getImage()
		{
			return $this->getData('image');
		}
		public function getImageSRC()
		{
			$img = $this->getData('image');
			return is_null( $img ) ? null : ( self::ITEM_IMG_SRC_DIR . $img );
		}

		public function getPercent()
		{
			return $this->getData('percent');
		}


		public static function findByPID($pid)
		{
			$items = array();
			$return = static::executeQuery( 'SELECT i.iid AS iid, i.name AS name, i.image AS image, ip.percent AS percent FROM ' .
											self::TABLE_NAME . ' AS i ' .
											'INNER JOIN '. self::TABLE_NAME_IP . ' AS ip ON ( ip.iid = i.iid ) ' .
											'WHERE ip.pid = ? ORDER BY pid ASC;',
									  			array( $pid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new ItemPromotion() ) ) )
						$items[] = $res ;
				}
			}

			return $items;
		}


	}
