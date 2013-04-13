<?php

	DEFINE( 'AUTH_PREFIX', 'auth_' );
	
	DEFINE( 'TOKEN_VALIDITY', 0 );
	
	
	DEFINE( 'TOKEN_ID_KEY', 'uid' );
	DEFINE( 'TOKEN_VALIDITY_KEY', 'val' );
	
	class Authenticator {
	
		static private $instance = null;
		
		private $token = null;
		private $userId = null;
		
		private function __clone() { }
		private function __construct() { }
		
		public static function getInstance()
		{
			if( is_null( static::$instance ) )
				static::$instance = new Authenticator();
				
			return static::$instance;
		}
		
		public function evalLogin()
		{
			if( isset( $_REQUEST['token'] ) && strlen( $_REQUEST['token'] ) > 0 )
			{
				$token = $_REQUEST['token'];
				$token_key = AUTH_PREFIX . $token ;
				$cc = CommonCache::getInstance();
				
				$aInfo = $cc-get( $token_key );
				
				if( $aInfo !== false && is_array( $aInfo )
						&& isset( $aInfo[ TOKEN_ID_KEY ] ) )
				{
					if( TOKEN_VALIDITY == 0 ||
							( isset( $aInfo[ TOKEN_VALIDITY_KEY ] )
								&& ( $aInfo[ TOKEN_VALIDITY_KEY ] + TOKEN_VALIDITY ) < time() ) )
					{
						$this->token = $token;
						$this->userId = $aInfo[ TOKEN_ID_KEY ];
					
						return true;
					}
					else
						$cc->delete( $token_key );
				}
				else
				{
					// do mysql
				
				}
				

			}
			
			return false;
		}
		
		public function getToken()
		{
			if( is_null( $this->token ) )
				return false;
				
			return $token;
		}
		
		public function getUserId()
		{
			if( is_null( $this->userId ) )
				return false;
				
			return $userId;
		}
	
	}
	
	
	Controller::registerAutheFunction(function() {
		
		$auth = Authenticator::getInstance();
		
		return $auth->evalLogin();
	});


?>