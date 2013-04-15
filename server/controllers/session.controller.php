<?php

	class SessionController extends Controller {
	

		public function __configure()
		{
			//$this->checkAuth();
		}
		
		
		public function create()
		{
			$render_code = 1;
			$resp = array();
			
			if( $this->checkAuth() )
				$render_code = 2;
				
			else
			{
				$mail = valid_request( 'email' );
				$pass = valid_request( 'password' );

				if( is_null( $mail ) || is_null( $pass ) )
					$render_code = 3;
				
				else
				{
					$user = User::findByCredentials( $mail, $pass );

					if( is_null( $user ) )
						$render_code = 4;
					
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
						}
						else
						{
							$token = hash('sha256', uniqid(rand(), true)) ;
						
							$sess = new Session();
							
							$sess->setToken( $token );
							$sess->setUserId( $userId );
							$sess->setValidity( TOKEN_VALIDITY == 0 ? 0 : ( time() + TOKEN_VALIDITY ) );
							
							$render_code = ( $success = $sess->save() ) ? 0 : 5 ;
						}
						
						if( $success )
							$resp = array('token' => $token, 'uid' => $userId, 'validity' => $validity );
					}
				}
			}
			
			$this->respond->renderJSON( $resp, $render_code );
		}




		public function destroy()
		{
			$render_code = 1;
			$resp = array();
			
			if( $this->checkAuth() )
				$render_code = 2;

			else
			{
				$token = valid_request( 'session' );
				$sess = null;

				if( is_null( $token ) || is_null( $sess = Session::findById( $token ) ) ) 
					$render_code = 3;

				else
				{
					$sess->setValidity(-1);
					$render_code = ( $success = $sess->save() ) ? 0 : 4 ;
				}
			}

			$this->respond->renderJSON( $resp, $render_code );
		}
	
	
	}
?>