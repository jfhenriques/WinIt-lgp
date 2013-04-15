<?php


	DEFINE( 'R_STATUS_OK'				, 0x0	);

	DEFINE( 'R_GLOB_ERR_UNDEFINED'		, 0x100	);
	DEFINE( 'R_GLOB_ERR_SAVE_UNABLE'	, 0x101	);

	DEFINE( 'R_GLOB_ERR_MUST_AUTH'		, 0x150	);
	DEFINE( 'R_GLOB_ERR_MUST_NOT_AUTH'	, 0x151	);




	function valid( $var, $arr, $trim = true )
	{
		if( is_array( $arr ) && isset( $arr[ $var ] ) && !is_null( $arr[ $var ] ) )
		{
			$val = null;

			if( $trim )
				$val = trim( $arr[ $var ] );
			else
				$val = &$arr[ $var ] ;

			if( strlen( $val ) > 0 )
				return $val;
		}

		return null;
	}
	function valid_request( $var, $trim = true )
	{
		return valid( $var, $_REQUEST, $trim );
	}




	function describeMessage( $code, $arr = array() )
	{

		$globStatusCode = array(
							R_GLOB_ERR_UNDEFINED		=> 'Erro Indefinido',
							R_GLOB_ERR_SAVE_UNABLE		=> 'Impossível Salvar',

							R_GLOB_ERR_MUST_AUTH		=> 'Utilizador não autenticado',
							R_GLOB_ERR_MUST_NOT_AUTH	=> 'Utilizador não pode estar autenticado',
						);
		
		if( is_null( $code ) )
			$code = R_GLOB_ERR_UNDEFINED ;

		return isset( $globStatusCode[$code] ) ?
							$globStatusCode[$code]
							: ( ( is_array( $arr ) && isset( $arr[$code] ) ) ?
										$arr[$code] :
										null );
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
				$renderCode = $auth ? R_GLOB_ERR_MUST_AUTH : R_GLOB_ERR_MUST_NOT_AUTH ;

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



		
		public static function registerAuthFunction( $func )
		{
			Controller::$authFunction = &$func;
		}
	}
?>