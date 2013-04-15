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
				
				$this->respond->renderJSON( array('name' => $name, 'email' => $email, 'cp4' => $cp4, 'cp3' => $cp3, 'door' => $porta_andar, 'token_fb' => $t_fb, 'token_tw' => $t_tw, 'uid' => $userId) );
				
				
			}				
		}
	
		public function create() {
			
			//$_REQUEST();
		}
	}
?>