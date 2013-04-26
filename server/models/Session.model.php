<?php
	
	
	
	class Session extends ActiveRecord {
		
		const TABLE_NAME = 'session' ;
		
		

		public function getToken()
		{
			return $this->getData('token');
		}
		public function setToken($token)
		{
			$this->data['token'] = $token;
		}
		
		
		public function getUserId()
		{
			return $this->getData('uid');
		}
		public function setUserId($userId)
		{
			$this->data['uid'] = $userId;
		}
		

		public function getValidity()
		{
			return $this->getData('validity');
		}
		public function setValidity($validity)
		{
			$this->data['validity'] = $validity;
		}
		
		
		
		
		public function save()
		{
			$dbh = DbConn::getInstance()->getDB();
			
			$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' VALUES(:tok, :uid, :val) ON DUPLICATE KEY UPDATE validity = :val ;');	
			
			$tok = $this->getToken() ;
			$uid = $this->getUserId() ;
			$val = $this->getValidity() ;

			$sth->bindParam(':tok', $tok, PDO::PARAM_STR);
			$sth->bindParam(':uid', $uid, PDO::PARAM_INT);
			$sth->bindParam(':val', $val, PDO::PARAM_INT);

			// If this instance was cached, force its deletetion, so the next query caches it back with the correct values
			CommonCache::getInstance()->delete( CommonCache::buildVarName( self::TABLE_NAME, $this->getToken() ) );

			return $sth->execute();
		}


		
		public static function findById($token)
		{
			$result = static::cachedQuery( $token,
											self::TABLE_NAME,
											'SELECT * FROM '. self::TABLE_NAME . ' WHERE token = ? LIMIT 1;',
											array( $token ),
											function( $arr ) {
												return $arr['validity'] >= 0 &&
															( TOKEN_VALIDITY == 0 || $arr['validity'] >= time() ) ;
											});
				
			return static::fillModel( $result, new Session() );
		}
		

		public static function findByUserId($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME .
											' WHERE uid = ? AND validity >= 0 AND ( ? = \'0\' OR validity >= ? ) ORDER BY validity DESC LIMIT 1;',
									  array( $id, TOKEN_VALIDITY, time() + 60 ) );

							
			return static::fillModel( $result, new Session() );
		}
	}
?>