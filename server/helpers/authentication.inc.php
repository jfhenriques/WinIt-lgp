<?php

	DEFINE( 'AUTH_PREFIX', 'auth_' );
	
	
	
	
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
			if( isset( $_REQUEST['token'] ) && strlen( $_REQUEST['token'] ) > 0 )
				$this->session = Session::findById( $_REQUEST['token'] );
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