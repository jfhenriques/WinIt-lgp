<?php

	class  UserController extends Controller {
	
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
	
		public function create() {
			
			$nome = valid_request('name');
			$email = valid_request('email');
			$cp4 = valid_request('cp4');
			$cp3 = valid_request('cp3');
			$door = valid_request('portaAndar');
			$t_fb = valid_request('token_fb');
			$t_tw = valid_request('token_tw');
			$password = valid_request('password');
			
			if( is_null($nome) || is_null($email) || is_null($cp4) || is_null($cp3) || is_null($door) || is_null($t_fb) || is_null($t_tw) || is_null($password)) {
				$render_code = 1;
			} else {
				$this->respond->renderJSON( array('name' => $name, 'email' => $email, 'cp4' => $cp4, 'cp3' => $cp3, 'door' => $porta_andar, 'token_fb' => $t_fb, 'token_tw' => $t_tw, 'uid' => $userId, 'password' => $passaword) );
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
?>