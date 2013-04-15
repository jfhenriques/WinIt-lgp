<?php

	class  UserController extends Controller {
	
		// $_REQUEST
		
		/*
		 * Ensure that the user is logged
		 */
		public function __configure()
		{
			$this->requireAuth(true);
		}

		
		
		public function show()
		{
			$userId = 1; // cenas - pôr aqui a função que o joao ainda nao acabou
			// devolve o id do user logado
			
			$user = User::findById($userId);
			
			if( $user !== false ) {
			
				$name = $user->getnome();
				$email = $user->getEmail();
				$pass = $user->getPassword();
				$cp4 = $user->getCp4();
				$cp3 = $user->getCp3();
				$porta_andar = $user->getPorta_andar();
				$t_fb = $user->getToken_facebook();
				$t_tw = $user->getToken_twitter();
				
				$this->respond->renderJSON( array($name, $email, $pass, $cp4, $cp3, $porta_andar, $t_fb, $t_tw, $userId) );
			}				
		}
	
		public function create() {
			
			//$_REQUEST();
		}
	}
?>