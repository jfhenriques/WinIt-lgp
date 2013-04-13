<?php
	
	class Session extends SQLModel {
		
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
		
		
		
		
		public static function getByToken($token)
		{
			if( !is_null($token) && strlen($token) > 0 )
			{
				$dbh = DbConn::getInstance()->getDB();
				
				$sth = $dbh->prepare('SELECT * FROM ' . Session::TABLE_NAME . ' WHERE token = ? LIMIT 1;');
				$sth->execute(array($token));
				
				$result = $sth->fetch();
				
				if( $result !== false && count( $result ) > 0 )
				{
					
					$sess = new Session();
					$sess->token = $result['token'];
					$sess->userId = $result['uid'];
					$sess->validity = $result['validity'];
					
					return $sess;
				}
			}
			
			return null;
		}
	}
?>