<?php


	DEFINE( 'R_SESS_ERR_USER_NOT_FOUND'	, 0x10 );

	DEFINE( 'R_SESS_ERR_PARAMS'			, 0x20 );
	DEFINE( 'R_SESS_ERR_EMAIL_EXISTS'	, 0x21 );



	class  UserController extends Controller {

		private static $status = array(
				R_SESS_ERR_PARAMS		=> 'Parâmetros não definidos',
				R_SESS_ERR_EMAIL_EXISTS	=> 'Email já existente',
			);

	
		// $_REQUEST
		
		/*
		 * Ensure that the user is logged
		 */
		protected function __configure()
		{
		}

		
		
		public function index()
		{
			$this->requireAuth(); // nao passa daqui se o user nao estiver logado

			$render_code = null ;
			$resp = array();

			$auth = Authenticator::getInstance(); // retorna uma class com o id do user logado
			$userId = $auth->getUserId();

			$user = User::findById($userId);
			
			if( is_null( $user ) )
				$render_code = R_SESS_ERR_USER_NOT_FOUND;

			else
			{
				$resp = array( 'uid' => $userId,
							  'name' => $user->getName(),
							  'email' => $user->getEmail(),
							  'adid' => $user->getADID(),
							  'cp4' => 0,
							  'cp3' => 0,
							  'address' => '< address to be >',
							  'door' => $user->getDoor(),
							  'token_fb' => $user->getTokenFacebook(),
							  'token_tw' => $user->getTokenTwitter(),
							  'level' => 0,
							  'points' => 0 );

				$render_code = R_STATUS_OK ;
			}

			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}
		
		public function edit()
		{
		
			$this->requireAuth(); // nao passa daqui se o user nao estiver logado

			$auth = Authenticator::getInstance(); // retorna o id do user logado
			$userId = $auth->getUserId();

			$render_code = null;
			$resp = array();
			
			$user = User::findById($userId);
			
			if( is_null( $user ) )
				$render_code = R_SESS_ERR_USER_NOT_FOUND;

			else
			{
				
				$name = valid_request('name');
				$email = valid_request('email');
				$adid = valid_request('adid');
				$door = valid_request('door');
				//$token_fb = valid_request('token_fabebook');
				//$token_tw = valid_request('token_twitter');
				$password = valid_request('password', false);
				
				if( !is_null($email) && !is_null( User::findByEmail( $email ) ) )
					$render_code = R_SESS_ERR_EMAIL_EXISTS;

				else
				{
					if( !is_null($email) )
						$user->setEmail($email);

					if( !is_null($name) )
						$user->setName($name);
				
					if( !is_null($adid) )
						$user->setCP3($adid);

					if( !is_null($door) )
						$user->setDoor($door);
				
					// if( !is_null($token_fb) )
					// 	$user->setTokenFacebook($token_fb);
				
					// if( !is_null($token_tw))
					// 	$user->setTokenTwitter($token_tw);
				
					if( !is_null($password) )
						$user->setPassword( User::saltPass( $password ) );
				
					$render_code = ( $success = $user->save() ) ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE ;
				
					// if( $success )
					// {
					// 	$resp = array( 'uid' => $userId,
					// 	  'name' => $user->getnome(),
					// 	  'email' => $user->getEmail(),
					// 	  'cp4' => $user->getCP4(),
					// 	  'cp3' => $user->getCP3(),
					// 	  'door' => $user->getPortaAndar(),
					// 	  'token_fb' => $user->getTokenFacebook(),
					// 	  'token_tw' => $user->getTokenTwitter(),
					// 	  'level' => 0,
					// 	  'points' => 0 );
					// }
				}
							
			}

			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}
			
		
	
		public function create()
		{
			$this->requireNoAuth();

			$render_code = null;
			$resp = array();
			
			$name	= valid_request('name');
			$email	= valid_request('email');
			$adid	= valid_request('adid');
			$door	= valid_request('door');
			$password = valid_request('password', false);

			
			if( is_null($name) || is_null($email) || is_null($adid) || is_null($door) || is_null($password) )
				$render_code = R_SESS_ERR_PARAMS ;

			else
			{
				if( !is_null( User::findByEmail( $email ) ) )
					$render_code = R_SESS_ERR_EMAIL_EXISTS ;

				else
				{
					$success = false;
					$user = new User();
					
					$user->setName($name);
					$user->setEmail($email);
					$user->setADID($adid);
					$user->setDoor($door);
					$user->setPassword( User::saltPass($password) );
					
					$render_code = ( $success = $user->save() ) ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE ;

					if( $success )
						$resp['uid'] = $user->getID();
				}
			}
			
			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}
	}
	
	
?>