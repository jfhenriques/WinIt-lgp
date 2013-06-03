<?php


	DEFINE( 'R_SESS_ERR_PARAM'					, 0x10 );
	DEFINE( 'R_SESS_ERR_USER_NOT_FOUND'			, 0x11 );

	DEFINE( 'R_SESS_ERR_SESSION_NOT_FOUND'		, 0x20 );

	DEFINE( 'R_SESS_FACEBOOK_APP_NOT_INSTALLED'	, 0x30 );
	DEFINE( 'R_SESS_FACEBOOK_APP_ERROR'			, 0x31 );

	DEFINE( 'R_SESS_FACEBOOK_USER_LOGIN'		, 0x40 );



	class SessionController extends Controller {

		private static $status = array(
				R_SESS_ERR_PARAM					=> 'Utilizador e/ou password não especificado',
				R_SESS_ERR_USER_NOT_FOUND			=> 'Utilizador e/ou password não encontrado',

				R_SESS_ERR_SESSION_NOT_FOUND		=> 'Sessão não encontrado',

				R_SESS_FACEBOOK_APP_NOT_INSTALLED 	=> 'Utilizador não instalou a aplicação',
				R_SESS_FACEBOOK_APP_ERROR 			=> 'Facebook: aplicação não autorizada, ou resposta inválida',

				R_SESS_FACEBOOK_USER_LOGIN 			=> 'Necessário efectuar o login pelo facebook em users registados pelo facebook',
			);

	

		protected function __configure()
		{
			$this->requireNoAuth();
		}
		



		private function facebook_open_session($token_fb, &$error)
		{
			$user = null;
			$facebookUID = 0;

			$response = FacebookPlugin::fetchInfoByUserAccessToken( $token_fb );

			if(    !is_array( $response )
				|| 0 >= ($facebookUID = valid_var('id', $response, true, 0 )) )
				$error = R_SESS_FACEBOOK_APP_ERROR;
	
			else
			{
				$isInstalled = (bool)valid_var('installed', $response, true, false ) ;
				$user = User::findByFacebookUID( $facebookUID );

				if( is_null( $user ) )
				{
					$name = null;

					if( !$isInstalled )
						$error = R_SESS_FACEBOOK_APP_NOT_INSTALLED ;

					if( is_null( $name = valid_var('name', $response ) ) )
						$error = R_SESS_FACEBOOK_APP_ERROR;

					else
					{
						$birthday = valid_var('birthday', $response ) ;
						$email = valid_var('email', $response ) ;

						$user = new User();

						$user->setFacebookUID( $facebookUID );
						$user->setName( $name );

						if( !is_null( $birthday ) && !empty( $birthday ) )
							$user->setBirth( DateTime::createFromFormat('n/j/Y', $birthday)->getTimestamp() );

						if( !is_null( $email ) )
							$user->setEmail( $email );

						$user->setSeed( Controller::genRand('sha256', true) );

						if( $user->save() )
							return $user;

						else
							$error = R_GLOB_ERR_SAVE_UNABLE;
					}

				}
				else
				{
					if( $isInstalled )
					{
						if( $user->isActive() )
							return $user;

						else
						{
							$user->setActive( true );

							if( $user->save() )
								return $user;

							else
								$error = R_GLOB_ERR_SAVE_UNABLE;

						}
					}
					else
					{
						$user->setActive( false );

						if( $user->save() )
							$error = R_SESS_FACEBOOK_APP_NOT_INSTALLED ;

						else
							$error = R_GLOB_ERR_SAVE_UNABLE;
					}

				}
			}

			return null;
		}
		
		
		public function create()
		{
			
			$mail = valid_request_var( 'email' );
			$pass = valid_request_var( 'password' );
			$token_fb = valid_request_var( 'token_fb' );

			$isInHouse = is_null( $token_fb );


			if( $isInHouse && ( is_null( $mail ) || is_null( $pass ) ) )
				$this->respond->setJSONCode( R_SESS_ERR_PARAM );
			
			else
			{
				$user = null;
				$errorCode = R_GLOB_ERR_UNDEFINED;
				$canContinue = false;

				
				if( $isInHouse )
				{
					if(    is_null( $user = User::findByCredentials( $mail, $pass ) )
						|| !$user->isActive() )
						$this->respond->setJSONCode( R_SESS_ERR_USER_NOT_FOUND );

					else if( $isInHouse && !is_null( $user->getFacebookUID() ) )
						$this->respond->setJSONCode( R_SESS_FACEBOOK_USER_LOGIN ); // Só chega aqui, se for definida uma password manualmente na base de dados

					else
						$canContinue = true;
				}
				else
				{
					if( is_null( $user = $this->facebook_open_session( $token_fb, $errorCode ) ) )
						$this->respond->setJSONCode( $errorCode );

					else
						$canContinue = true;
				}

				
				if( $canContinue )
				{
					$userId = $user->getUID();

					$token		= null;
					$success	= false;
					$validity	= 0;
					
					$sess = Session::findByUID( $userId );

					if( is_null( $sess ) )
					{
						$token = Controller::genRand64() ;
					
						$sess = new Session();
						
						$sess->setToken( $token );
						$sess->setUID( $userId );
						$sess->setValidity( TOKEN_VALIDITY == 0 ? 0 : ( time() + TOKEN_VALIDITY ) );
						
						$this->respond->setJSONCode( ( $success = $sess->save() ) ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
					}
					else
					{
						$token = $sess->getToken();
						$validity = $sess->getValidity();
						
						$success = true;
						$this->respond->setJSONCode( R_STATUS_OK );

					}
					
					if( $success )
						$this->respond->setJSONResponse( array('token' => $token, 'uid' => $userId, 'validity' => $validity ) );
				}
			}
			
			$this->respond->renderJSON( static::$status );
		}




		public function destroy()
		{
			//$this->checkAuth();

			$resp = array();
			
			$token = valid_request_var( 'session' );
			$sess = null;

			if( is_null( $token ) || is_null( $sess = Session::findByToken( $token ) ) ) 
				$this->respond->setJSONCode( R_SESS_ERR_SESSION_NOT_FOUND );

			else
			{
				$sess->setValidity(-1);
				$this->respond->setJSONCode( $sess->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
			}
			

			$this->respond->renderJSON( static::$status );
		}
	
	
	}
