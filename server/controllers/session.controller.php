<?php


	DEFINE( 'RESPOND_ERROR_PARAM'			, 0x10 );
	DEFINE( 'RESPOND_ERROR_USER_NOT_FOUND'	, 0x11 );

	DEFINE( 'RESPOND_ERROR_SESSION_NOT_FOUND'	, 0x20 );



	class SessionController extends Controller {

		private static $status = array(
				RESPOND_ERROR_PARAM				=> 'Utilizador e/ou password não especificado',
				RESPOND_ERROR_USER_NOT_FOUND	=> 'Utilizador e/ou password não encontrado',

				RESPOND_ERROR_SESSION_NOT_FOUND	=> 'Sessão não encontrado',
			);

	

		public function __configure()
		{
			//$this->checkAuth();
		}
		
		
		public function create()
		{
			$this->requireNoAuth();


			$render_code = RESPOND_ERROR_UNDEFINED;
			$resp = array();
			
			$mail = valid_request( 'email' );
			$pass = valid_request( 'password' );

			if( is_null( $mail ) || is_null( $pass ) )
				$render_code = RESPOND_ERROR_PARAM;
			
			else
			{
				$user = User::findByCredentials( $mail, $pass );

				if( is_null( $user ) )
					$render_code = RESPOND_ERROR_USER_NOT_FOUND;
				
				else
				{
					$userId = $user->getUtilizadorId();

					$token		= null;
					$success	= false;
					$validity	= 0;
					
					$sess = Session::findByUserId( $userId );

					if( !is_null( $sess ) )
					{
						$token = $sess->getToken();
						$validity = $sess->getValidity();
						
						$success = true;
						$render_code = RESPOND_STATUS_OK;
					}
					else
					{
						$token = hash('sha256', uniqid(rand(), true)) ;
					
						$sess = new Session();
						
						$sess->setToken( $token );
						$sess->setUserId( $userId );
						$sess->setValidity( TOKEN_VALIDITY == 0 ? 0 : ( time() + TOKEN_VALIDITY ) );
						
						$render_code = ( $success = $sess->save() ) ? RESPOND_STATUS_OK : RESPOND_ERROR_SAVE_UNABLE ;
					}
					
					if( $success )
						$resp = array('token' => $token, 'uid' => $userId, 'validity' => $validity );
				}
			}
			
			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}




		public function destroy()
		{
			$this->requireAuth();

			$render_code = RESPOND_ERROR_UNDEFINED;
			$resp = array();
			
			$token = valid_request( 'session' );
			$sess = null;

			if( is_null( $token ) || is_null( $sess = Session::findById( $token ) ) ) 
				$render_code = RESPOND_ERROR_SESSION_NOT_FOUND;

			else
			{
				$sess->setValidity(-1);
				$render_code = ( $success = $sess->save() ) ? RESPOND_STATUS_OK : RESPOND_ERROR_SAVE_UNABLE ;
			}
			

			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}
	
	
	}
?>