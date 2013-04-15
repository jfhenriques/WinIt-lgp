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
		public function __configure()
		{
		}

		
		
		public function index()
		{
			$this->requireAuth(); // nao passa daqui se o user nao estiver logado

			
			$render_code = null ;
			$resp = array();

			$auth = Authenticator::getInstance(); // retorna o id do user logado
			$userId = $auth->getUserId();
			
			$user = User::findById($userId);
			
			if( is_null( $user ) )
				$render_code = R_SESS_ERR_USER_NOT_FOUND;

			else
			{
				$resp = array( 'uid' => $userId,
							  'name' => $user->getnome(),
							  'email' => $user->getEmail(),
							  'cp4' => $user->getCP4(),
							  'cp3' => $user->getCP3(),
							  'door' => $user->getPortaAndar(),
							  'token_fb' => $user->getTokenFacebook(),
							  'token_tw' => $user->getTokenTwitter(),
							  'level' => 0,
							  'points' => 0 );

				$render_code = R_STATUS_OK ;
			}

			$this->respond->renderJSON( $resp, $render_code, describeMessage( $renderCode, static::$status ) );
		}
	
		public function create()
		{
			$this->requireNoAuth();

			$render_code = null;
			$resp = array();


			$nome	= valid_request('name');
			$email	= valid_request('email');
			$cp4	= valid_request('cp4');
			$cp3	= valid_request('cp3');
			$door	= valid_request('door');
			$password = valid_request('password', false);

			
			if( is_null($nome) || is_null($email) || is_null($cp4)
					|| is_null($cp3) || is_null($door) || is_null($password) )
				$render_code = R_SESS_ERR_PARAMS ;

			else
			{
				if( !is_null( User::findByEmail( $email ) ) )
					$render_code = R_SESS_ERR_EMAIL_EXISTS ;

				else
				{
					$success = false;
					$user = new User();
					
					$user->setnome($nome);
					$user->setEmail($email);
					$user->setCP4($cp4);
					$user->setCP3($cp3);
					$user->setPortaAndar($door);
					$user->setPassword( User::saltPass($password) );
					
					$render_code = ( $success = $user->save() ) ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE ;

					if( $success )
						$resp['uid'] = $user->getUtilizadorId();
				}
			}
			
			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}
	}
	
	
?>