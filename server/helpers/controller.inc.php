<?php


	function valid( $var, $arr )
	{
		return ( is_array( $arr ) && isset( $arr[ $var ] ) && strlen( $arr[ $var ] ) > 0 ) ? $arr[ $var ] : null ;
	}
	function valid_request( $var )
	{
		return valid( $var, $_REQUEST );
	}

	class Controller {
	
		public $respond = null;
		private $requireAuth = false;
		
		private static $authFunction = null;
		
		
		
		
		public function __construct()
		{
			$this->respond = new Template();
			
			$this->__configure();
		}
		
		public function __configure() {}
	
		public function requireAuth()
		{
			$this->checkAuth( true );
		}
		public function checkAuth( $exit = false )
		{
			if( !is_null( Controller::$authFunction ) )
			{
				$func = &Controller::$authFunction;
				
				$ret_val = $func();
				
				if( $exit && !$ret_val )
				{
					header('HTTP/1.0 403 Forbidden', true);
					
					echo "403 Forbidden";
					
					exit (1);
				}
				
				return $ret_val ;
			}
			
		}
		
		public static function registerAutheFunction( $func )
		{
			Controller::$authFunction = &$func;
		}
	}
?>