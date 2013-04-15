<?php


	DEFINE('RESPOND_STATUS_OK'			, 0x0	);

	DEFINE('RESPOND_ERROR_UNDEFINED'	, 0x100	);
	DEFINE('RESPOND_ERROR_SAVE_UNABLE'	, 0x101	);

	DEFINE('RESPOND_ERROR_NO_AUTH'		, 0x200	);




	function valid( $var, $arr )
	{
		return ( is_array( $arr ) && isset( $arr[ $var ] ) && strlen( $arr[ $var ] ) > 0 ) ? $arr[ $var ] : null ;
	}
	function valid_request( $var )
	{
		return valid( $var, $_REQUEST );
	}


	function describeMessage( $code, $arr = array() )
	{
		$globStatusCode = array(
								RESPOND_ERROR_UNDEFINED		=> 'Erro Indefinido',
								RESPOND_ERROR_SAVE_UNABLE	=> 'Impossível Salvar',

								RESPOND_ERROR_NO_AUTH		=> 'Utilizador não autenticado',
							);

		return isset( $globStatusCode[$code] ) ?
							$globStatusCode[$code]
							: ( ( is_array( $arr ) && isset( $arr[$code] ) ) ? $arr[$code] : null );
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