<?php
	
	
	
	class Session extends ActiveRecord {
		
		private $token = null;
		private $userId = 0;
		private $validity = 0;
		
		const TABLE_NAME = 'session' ;
		
		

		public function getToken()
		{
			return $this->token;
		}
		public function setToken($token)
		{
			$this->token = $token;
		}
		
		
		public function getUserId()
		{
			return $this->userId;
		}
		public function setUserId($userId)
		{
			$this->userId = $userId;
		}
		

		public function getValidity()
		{
			return $this->validity;
		}
		public function setValidity($validity)
		{
			$this->validity = $validity;
		}
		
		
		
		
		public function save()
		{
			$dbh = DbConn::getInstance()->getDB();
			
			$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' VALUES(:tok, :uid, :val) ON DUPLICATE KEY UPDATE validity = :val ;');	
			
			$sth->bindParam(':tok', $this->token, PDO::PARAM_STR);
			$sth->bindParam(':uid', $this->userId, PDO::PARAM_INT);
			$sth->bindParam(':val', $this->validity, PDO::PARAM_INT);

			// If this instance was cached, force its deletetion, so the next query caches it back with the correct values
			CommonCache::getInstance()->delete( CommonCache::buildVarName( self::TABLE_NAME, $this->token ) );

			return $sth->execute();
		}

		private static function fillSession($arr)
		{
			if( is_array( $arr ) && count( $arr ) > 0 )
			{
				$sess = new Session();
				
				$sess->token = $arr['token'];
				$sess->userId = $arr['uid'];
				$sess->validity = $arr['validity'];
				
				return $sess;
			}
			
			return null;
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
				
			return static::fillSession( $result );
		}
		

		public static function findByUserId($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME .
											' WHERE uid = ? AND validity >= 0 AND ( ? = \'0\' OR validity >= ? ) ORDER BY validity DESC LIMIT 1;',
									  array( $id, TOKEN_VALIDITY, time() + 60 ) );

							
			return static::fillSession( $result );
		}
	}
?>