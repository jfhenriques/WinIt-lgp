<?php

	class UserPromotion extends ActiveRecord {


		const TABLE_NAME = "userpromotion";



		public function getUPID()
		{
			return (int)$this->getData('upid');
		}
		public function getPID()
		{
			return (int)$this->getData('pid');
		}
		public function getUID()
		{
			return (int)$this->getData('uid');
		}




		public static function findByUPID($upid)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE upid = ? LIMIT 1 ;',
									  array( $upid ) );
							
			return static::fillModel( $result, new UserPromotion() );
		}

		public function save() { }


	}

?>