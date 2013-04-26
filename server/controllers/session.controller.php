<?php


	DEFINE( 'R_SESS_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_SESS_ERR_USER_NOT_FOUND'		, 0x11 );

	DEFINE( 'R_SESS_ERR_SESSION_NOT_FOUND'	, 0x20 );



	class SessionController extends Controller {

		private static $status = array(
				R_SESS_ERR_PARAM				=> 'Utilizador e/ou password não especificado',
				R_SESS_ERR_USER_NOT_FOUND		=> 'Utilizador e/ou password não encontrado',

				R_SESS_ERR_SESSION_NOT_FOUND	=> 'Sessão não encontrado',
			);

	

		protected function __configure()
		{
			//$this->checkAuth();
		}
		
		
		public function create()
		{
			$this->requireNoAuth();
			
			$mail = valid_request_var( 'email' );
			$pass = valid_request_var( 'password' );

			if( is_null( $mail ) || is_null( $pass ) )
				$this->respond->setJSONCode( R_SESS_ERR_PARAM );
			
			else
			{
				$user = User::findByCredentials( $mail, $pass );

				if( is_null( $user ) )
					$this->respond->setJSONCode( R_SESS_ERR_USER_NOT_FOUND );
				
				else
				{
					$userId = $user->getID();

					$token		= null;
					$success	= false;
					$validity	= 0;
					
					$sess = Session::findByUserId( $userId );

					if( !is_null( $sess ) )
					{
						$token = $sess->getToken();
						$validity = $sess->getValidity();
						
						$success = true;
						$this->respond->setJSONCode( R_STATUS_OK );
					}
					else
					{
						$token = hash('sha256', uniqid(rand(), true)) ;
					
						$sess = new Session();
						
						$sess->setToken( $token );
						$sess->setUserId( $userId );
						$sess->setValidity( TOKEN_VALIDITY == 0 ? 0 : ( time() + TOKEN_VALIDITY ) );
						
						$this->respond->setJSONCode( ( $success = $sess->save() ) ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
					}
					
					if( $success )
						$this->respond->setJSONResponse( array('token' => $token, 'uid' => $userId, 'validity' => $validity ) );
				}
			}
			
			$this->respond->renderJSON( static::$status );
		}




		public function destroy()
		{
			$this->checkAuth();

			$resp = array();
			
			$token = valid_request_var( 'session' );
			$sess = null;

			if( is_null( $token ) || is_null( $sess = Session::findById( $token ) ) ) 
				$this->respond->setJSONCode( R_SESS_ERR_SESSION_NOT_FOUND );

			else
			{
				$sess->setValidity(-1);
				$this->respond->setJSONCode( $sess->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
			}
			

			$this->respond->renderJSON( static::$status );
		}
	
	
	}
?>