<?php


	DEFINE( 'R_USER_ERR_USER_NOT_FOUND'	, 0x10 );

	DEFINE( 'R_USER_ERR_PARAMS'			, 0x20 );
	DEFINE( 'R_USER_ERR_EMAIL_EXISTS'	, 0x21 );
	DEFINE( 'R_USER_ERR_INV_ADID'		, 0x22 );

	DEFINE( 'R_USER_EMAIL_MISSING'		, 0x30 );



	class  UserController extends Controller {

		private static $status = array(
				R_USER_ERR_USER_NOT_FOUND => 'Utilizador não encontrado',

				R_USER_ERR_PARAMS		=> 'Parâmetros não definidos',
				R_USER_ERR_EMAIL_EXISTS	=> 'Email já existente',

				R_USER_ERR_INV_ADID		=> 'Código de Endereço inválido',

				R_USER_EMAIL_MISSING		=> 'Deve fornecer o e-mail para fazer reset à password',
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


			$auth = Authenticator::getInstance(); // retorna uma class com o id do user logado
			$userId = $auth->getUserId();

			$user = User::findById($userId);
			
			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$cp4 = $cp3 = $street = $locality = $district = null ;

				$address = $user->getADID() ? Address::findByADID( $user->getADID() ) : null ;
				if( !is_null( $address ) )
				{
					$cp4 = $address->getCP4();
					$cp3 = $address->getCP3();

					$street = $address->getStreet();

					$locality = $address->getLocality();
					$district = $address->getDistrict();
				}

				$this->respond->setJSONResponse( array( 'uid' => $userId,
														  'name' => $user->getName(),
														  'email' => $user->getEmail(),
														  'adid' => $user->getADID(),
														  'birth' => $user->getBirth(),
														  'cp4' => $cp4,
														  'cp3' => $cp3,
														  'locality' => $locality,
														  'district' => $district,
														  'address' => $street,
														  'address2' => $user->getAddress2(),
														  'token_fb' => $user->getTokenFacebook(),
														  'token_tw' => $user->getTokenTwitter(),
														  'level' => 0,
														  'points' => 0 ) );

				$this->respond->setJSONCode( R_STATUS_OK );
				
			}

			$this->respond->renderJSON( static::$status );
		}
		
		public function edit()
		{
		
			$this->requireAuth(); // nao passa daqui se o user nao estiver logado


			$auth = Authenticator::getInstance(); // retorna o id do user logado
			$userId = $auth->getUserId();

			$resp = array();
			
			$user = User::findById($userId);
			
			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				
				$name = valid_request_var('name');
				$email = valid_request_var('email');
				$adid = valid_request_var('adid');
				$address2 = valid_request_var('address2');
				$birth = valid_request_var('birth');
				//$token_fb = valid_request_var('token_fabebook');
				//$token_tw = valid_request_var('token_twitter');
				$password = valid_request_var('password', false);
				
				if( !is_null($email) && !is_null( User::findByEmail( $email ) ) )
					$this->respond->setJSONCode( R_USER_ERR_EMAIL_EXISTS );

				else
				{
					if( !is_null($email) )
						$user->setEmail($email);

					if( !is_null($name) )
						$user->setName($name);
				
					if( !is_null($adid) )
						$user->setADID($adid);

					if( !is_null($address2) )
						$user->setAddress2($address2);

					if( !is_null($birth) )
						$user->setBirth($birth);

					// if( !is_null($token_fb) )
					// 	$user->setTokenFacebook($token_fb);
				
					// if( !is_null($token_tw))
					// 	$user->setTokenTwitter($token_tw);

					if( !is_null( $user->getADID() ) && is_null( Address::findByADID($user->getADID()) ) )
						$this->respond->setJSONCode( R_USER_ERR_INV_ADID );

					else
					{
					
						if( !is_null($password) )
							$user->setPassword( User::saltPass( $password ) );
					
						$this->respond->setJSONCode( $user->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );

					}
				}
							
			}

			$this->respond->renderJSON( static::$status );
		}
			
		
	
		public function create()
		{
			$this->requireNoAuth();

			$render_code = null;
			$resp = array();
			
			$name	= valid_request_var('name');
			$email	= valid_request_var('email');
			$adid	= valid_request_var('adid');
			$address2 = valid_request_var('address2');
			$birth = valid_request_var('birth');
			$password = valid_request_var('password', false);

			if( is_null($name) || is_null($email) || is_null($password) || is_null($birth) )
				$this->respond->setJSONCode( R_USER_ERR_PARAMS );

			else
			{
				if( !is_null( User::findByEmail( $email ) ) )
					$this->respond->setJSONCode( R_USER_ERR_EMAIL_EXISTS );

				elseif( !is_null( $adid ) && is_null( Address::findByADID($adid) ) )
					$this->respond->setJSONCode( R_USER_ERR_INV_ADID );

				else
				{
					$user = new User();
					
					$user->setName($name);
					$user->setEmail(strtolower($email));
					$user->setADID($adid);
					$user->setAddress2($address2);
					$user->setBirth($birth);
					$user->setPassword( User::saltPass($password) );

					$success = $user->save();

					if( $success )
						$this->respond->setJSONResponse( array( 'uid' => $user->getUID() ) );

					$this->respond->setJSONCode( $success ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
				}
			}
			
			$this->respond->renderJSON( static::$status );
		}
		
		public function list_promotions_won() {
		
			$this->requireAuth();
			
			$auth = Authenticator::getInstance(); // retorna o id do user logado
			$userId = $auth->getUserId();
			
			$user = User::findById($userId);
			
			if( is_null( $user ) ) {
			
				$this->respond->setJSONCode ( R_USER_ERR_USER_NOT_FOUND );
				
			} else {
			
				$resp = $user->list_promotions_won();				
				$response = array();
				
				foreach ( $resp as $linha ) {
					array_push($response, $linha);
				}
				
				$this->respond->setJSONResponse( $response );
				$this->respond->setJSONCode( R_STATUS_OK );
			}
			$this->respond->renderJSON( static::$status );
		}



		public function reset_password()
		{
			$email = valid_request_var('email');
			$user = null;

			if( is_null( $email ) )
				$this->respond->setJSONCode( R_USER_EMAIL_MISSING );
			
			elseif( is_null( $user = User::findByEmail( $email ) ) )
					$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$token = hash('sha512', uniqid(rand(), true)) ;

				$user->setResetToken($token);
				$user->setResetTokenValidity(time()+600); // 10 minutos para fazer reset

				$success = $user->save();

				if( $success )
				{
					$texto = "Foi pedido que fosse feito reset da password da sua conta na aplicação Tlantic PromGame Mobile.\n\n".
							 "Por favor siga o link: https://lgptlantic.fe.up.pt/reset_password/${token} \n\n".
							 "Se o pedido não efectuado por si, por favor ignore este e-mail\n\n".
							 "Atenciosamente,\nTlantic PromGame Mobile";

					@mail($user->getEmail(), "Tlantic PromGame Mobile - Reset da Password", $texto);
				}

				$this->respond->setJSONCode( $success ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
			}

			$this->respond->renderJSON( static::$status );
		}



		public function reset_password_confirmation()
		{
			$reset_token = valid_request_var('reset_token');
			$renderText = "Erro desconhecido";

			if( is_null( $reset_token ) )
				$renderText = "Token não definido";
			
			else
			{
				$user = User::findByResetToken( $reset_token );

				if( is_null( $user ) )
					$renderText = "Token de reset inválido";

				else
				if( $user->getResetTokenValidity() < time() )
					$renderText = "Excedeu o tempo para fazer reset à password";

				else
				{

 					$pass = substr(str_shuffle(str_repeat("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ#()+", 3)), 10, 8);


 					$user->setPassword( User::saltPass($pass) );

					if( !$user->save() )
						$renderText = "Erro: Impossível salvar";

					else
					{
						$renderText = "Nova password de acesso temporária enviada para o seu e-mail.\n<br>".
									  "Atenção: Deve mudá-la a sua password de imediato.";
						
						$texto = "No seguimento do pedido de reset da password de acesso à sua conta,\n" .
								 "enviamos-lhe a sua nova password temporária.\n".
								 "Atenção: deve alterar esta password de imediato.\n\n".
								 "Password: + $pass \n\n".
								 "Atenciosamente,\nTlantic PromGame Mobile";

						@mail($user->getEmail(), "Tlantic PromGame Mobile - Reset da Password", $texto);
					}

				
					
				}
			
			}

			$this->respond->renderHTML( $renderText );
		}
	}
	
	
?>