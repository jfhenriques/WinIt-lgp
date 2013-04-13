<?php

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
	
		public function requireAuth( $value = true )
		{
			$this->checkAuth( $value, true );
		}
		public function checkAuth( $value = true, $exit = false )
		{
			if( $value && !is_null( Controller::$authFunction ) )
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