<?php

	class Authenticator {
	
		static private $instance = null;
		
		private $session = null;
		
		
		public static function getInstance()
		{
			if( is_null( static::$instance ) )
				static::$instance = new Authenticator();
				
			return static::$instance;
		}
		
		
		private function __clone() { }
		
		private function __construct()
		{
			$sess = null;
			$token = valid_request_var( 'token' );

			if( !is_null($token)
				&& ( $sess = Session::findById( $token ) ) != null
				&& $sess->getValidity() >= 0
				&& ( TOKEN_VALIDITY === 0
					|| $sess->getValidity() >= time() ) )
					$this->session = $sess;
		}
		
		public function getSession()
		{
			return $this->session;
		}
		
		public function getToken()
		{
			if( !is_null( $this->session ) )
				return $this->session->getToken();
				
			return false;
		}
		
		public function getUserId()
		{
			if( !is_null( $this->session ) )
				return $this->session->getUserId();
				
			return false;
		}
	
	}
	
	
	Controller::registerAuthFunction(function() {
		
		$auth = Authenticator::getInstance();
		
		return !is_null( $auth->getSession() );
	});


?>