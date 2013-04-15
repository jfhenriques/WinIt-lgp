<?php


	DEFINE( 'RESPOND_ERROR_PARAM'			, 0x10 );
	DEFINE( 'RESPOND_ERROR_USER_NOT_FOUND'	, 0x11 );

	DEFINE( 'RESPOND_ERROR_SESSION_NOT_FOUND'	, 0x20 );



	class  UserController extends Controller {

		private static $status = array(
				RESPOND_ERROR_PARAM				=> 'Utilizador e/ou password não especificado',
				RESPOND_ERROR_USER_NOT_FOUND	=> 'Utilizador e/ou password não encontrado',

				RESPOND_ERROR_SESSION_NOT_FOUND	=> 'Sessão não encontrado',
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
			
			$this->requireAuth(true); // nao passa daqui se o user nao estiver logado
			
			$auth = Authenticator::getInstance(); // retorna o id do user logado
			
			$userId = $auth->getUserId(); 
			
			$user = User::findById($userId);
			
			if( $user !== false ) {
			
				$name = $user->getnome();
				$email = $user->getEmail();
				$cp4 = $user->getCP4();
				$cp3 = $user->getCP3();
				$porta_andar = $user->getPortaAndar();
				$t_fb = $user->getTokenFacebook();
				$t_tw = $user->getTokenTwitter();
				$level = 0;
				$points = 0;
				
				
				$this->respond->renderJSON( array('name' => $name, 'email' => $email, 'cp4' => $cp4, 'cp3' => $cp3,
													'door' => $porta_andar, 'token_fb' => $t_fb, 'token_tw' => $t_tw,
													'uid' => $userId, 'level' => $level, 'points' => $points) );
				
				
			}				
		}
	
		public function create()
		{
			
			$nome = valid_request('name');
			$email = valid_request('email');
			$cp4 = valid_request('cp4');
			$cp3 = valid_request('cp3');
			$door = valid_request('door');
			$password = valid_request('password');
			
			// $t_fb = valid_request('token_fb');
			// $t_tw = valid_request('token_tw');
			
			
			if( is_null($nome)|| is_null($email) || is_null($cp4) || is_null($cp3) || is_null($door) || is_null($password)) {
				$render_code = 1;
			} else {
				echo 'atum';
				$user = new User();
				
				$user->setnome($nome);
				$user->setEmail($email);
				$user->setCP4($cp4);
				$user->setCP3($cp3);
				$user->setPortaAndar($door);
				$user->setPassword(User::saltPass($password));
				
				
				$user->save();
				$this->respond->renderJSON('ok');
			}
			
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
?>