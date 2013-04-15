<?php


	DEFINE('RESPOND_STATUS_OK'			, 0x0	);

	DEFINE('RESPOND_ERROR_UNDEFINED'	, 0x100	);
	DEFINE('RESPOND_ERROR_SAVE_UNABLE'	, 0x101	);

	DEFINE('RESPOND_ERROR_MUST_AUTH'		, 0x200	);
	DEFINE('RESPOND_ERROR_MUST_NOT_AUTH'	, 0x201	);




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

								RESPOND_ERROR_MUST_AUTH		=> 'Utilizador não autenticado',
								RESPOND_ERROR_MUST_NOT_AUTH	=> 'Utilizador não pode estar autenticado',
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
	

		public function __checkAuth( $auth = true, $exit = false )
		{
			if( is_null( Controller::$authFunction ) )
				return false;

			$func = &Controller::$authFunction;
			$ret_val = $func();

			if( $exit && $ret_val !== $auth )
			{
				if( $auth )
					header('HTTP/1.0 403 Forbidden', true);

				$retType = Router::getInstance()->responseType() ;
				$renderCode = $auth ? RESPOND_ERROR_MUST_AUTH : RESPOND_ERROR_MUST_NOT_AUTH ;

				if( $retType === RESPOND_JSON )
					$this->respond->renderJSON(null, $renderCode, describeMessage($renderCode));

				else
					echo $auth ? "403 Forbidden" : describeMessage($renderCode) ;


				exit (1);

			}
			
			return $ret_val === $auth;
		}


		public function checkAuth($check = true)
		{
			return $this->__checkAuth($check, false);
		}

		public function requireAuth()
		{
			return $this->__checkAuth(true, true);
		}
		public function requireNoAuth()
		{
			return $this->__checkAuth(false, true);
		}



		
		public static function registerAutheFunction( $func )
		{
			Controller::$authFunction = &$func;
		}
	}
?>