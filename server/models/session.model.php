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
		
		
		
		
		public static function findById($token)
		{
			$result = static::cachedQuery( $token,
											self::TABLE_NAME,
											'SELECT * FROM '. self::TABLE_NAME . ' WHERE token = ? LIMIT 1;',
											array( $token ),
											function($arr) {
												return TOKEN_VALIDITY == 0 ||
															( isset($arr['validity']) && ( $arr['validity'] + TOKEN_VALIDITY ) >= time() ) ;
											});
				
			if( is_array( $result ) && count( $result ) > 0 )
			{
				$sess = new Session();
				
				$sess->token = $result['token'];
				$sess->userId = $result['uid'];
				$sess->validity = $result['validity'];
				
				return $sess;
			}
			
			return null;
		}
	}
?>