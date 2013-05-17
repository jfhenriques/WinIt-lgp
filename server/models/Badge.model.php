<?php
	
	class Badge
		extends ActiveRecord
		implements SavableActiveRecord {
		

		const TABLE_NAME = 'badges' ;
		const TABLE_NAME_USER = 'user' ;
		const TABLE_NAME_USERBADGES = 'userbadges' ;


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
			return is_null( $img ) ? null : ( BADGE_IMG_SRC_DIR . $img );
		}

		public function getDescription()
		{
			return $this->getData('description');
		}
		public function setDescription($description)
		{
			$this->data['description'] = $description;
		}

		public function save()
		{
		}
		
		public static function findByBID($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE bid = ? LIMIT 1;',
									  array( $id ) );

			return static::fillModel( $result, new Badge() );
		}

		public static function findByUID($uid) {

			$result = static::query( 'SELECT b.bid AS bid, b.name AS bname, b.image AS bimage, ub.aquis_date AS bdata '.
										'FROM self::TABLE_NAME AS b '.
										'INNER JOIN self::TABLE_NAME_USERBADGES AS ub ON (ub.bid = b.bid) '.
										'INNER JOIN self::TABLE_NAME_USER AS u ON (u.uid = ub.uid) '.
										'WHERE u.uid = ? LIMIT 1;',
										array( $uid ));
			return static::fillModel( $result, new Badge() );			
		}
	}

?>