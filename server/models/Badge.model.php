<?php
	
	class Badge
		extends ActiveRecord
		implements SavableActiveRecord {
		

		const TABLE_NAME = 'badges' ;


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
		
		public static function findById($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE bid = ? LIMIT 1;',
									  array( $id ) );

			return static::fillModel( $result, new Badge() );
		}

		/*public function list_promotions_won() {
		
			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			$userID = $this->getUID();
		
			$sth = $dbh->prepare('SELECT promotion.pid, promotion.name, promotion.active, promotion.end_date '.
									'FROM promotion, user, userpromotion '.
									'WHERE user.uid = userpromotion.uid '.
									'AND promotion.pid = userpromotion.pid '.
									'AND user.uid = '. $userID .' ;' );
			
			$ret = $sth->execute();
			
			$result = $sth->fetchAll();
			
			// var_dump($result);

			return $result;
			
		
		}*/

	}

?>