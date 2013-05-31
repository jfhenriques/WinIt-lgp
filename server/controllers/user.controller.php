<?php


	DEFINE( 'R_USER_ERR_USER_NOT_FOUND'	, 0x10 );

	DEFINE( 'R_USER_ERR_PARAMS'			, 0x20 );
	DEFINE( 'R_USER_ERR_EMAIL_EXISTS'	, 0x21 );
	DEFINE( 'R_USER_ERR_INV_ADID'		, 0x22 );

	DEFINE( 'R_USER_EMAIL_MISSING'		, 0x30 );
	DEFINE( 'R_USER_SENDMAIL_ERROR'		, 0x31 );
	DEFINE( 'R_USER_MUST_SEND_OLD_P'	, 0x32 );
	DEFINE( 'R_USER_BAD_OLD_PASS'		, 0x33 );

	DEFINE( 'R_USER_BAD_PROMO'			, 0x40 );
	DEFINE( 'R_USER_PROMO_EXPIRED'		, 0x41 );

	DEFINE( 'R_USER_DONT_OWN_GCM'		, 0x50 );
	DEFINE( 'R_USER_GCM_NOT_FOUND'		, 0x51 );

	DEFINE( 'R_USER_RESET_FACE_USER'	, 0x60 );



	DEFINE( 'MAIL_SUBJECT_RESET_PASS' , 'Tlantic PromGame Mobile - Password Reset' );
	DEFINE( 'MAIL_SIGNATURE'   , "\r\n\r\n\r\nAtenciosamente,\nA Equipa Tlantic PromGame Mobile" );



	class  UserController extends Controller {

		private static $status = array(
			
				R_USER_ERR_USER_NOT_FOUND => 'Utilizador não encontrado',

				R_USER_ERR_PARAMS		=> 'Parâmetros não definidos',
				R_USER_ERR_EMAIL_EXISTS	=> 'Email já existente',

				R_USER_ERR_INV_ADID		=> 'Código de Endereço inválido',

				R_USER_EMAIL_MISSING	=> 'Deve fornecer o e-mail para fazer reset à password',
				R_USER_SENDMAIL_ERROR	=> 'Não foi possível enviar o e-mail para o endereço de destino',

				R_USER_MUST_SEND_OLD_P	=> 'É necessário enviar a password antiga para alterar a password',
				R_USER_BAD_OLD_PASS		=> 'A password antiga está errada',

				R_USER_BAD_PROMO		=> 'Promoção não encontrada',
				R_USER_PROMO_EXPIRED	=> 'A promoção expirou ou excedeu o limite de utilizações possíveis ou já se encontra a participar na mesma',

				R_USER_DONT_OWN_GCM		=> 'O Token GCM não pertece ao utilizador',
				R_USER_GCM_NOT_FOUND	=> 'O Token GCM não foi encontrado',

				R_USER_RESET_FACE_USER	=> 'Não é possível fazer reset à password de um user registado pelo facebook',
			);

		
		/*
		 * Ensure that the user is logged
		 */
		protected function __configure()
		{
		}




		private static function calculateLevel($points)
		{
			return (int)( 1 + (25 * log10( 1 + ( (double)$points ) / 2000 ) ) );
		}

		private static function calculatePointsByLevel($level)
		{
			return - (int)( 1824.02 * ( 1.09648 - exp( 0.0921034 * (double)$level) ) );
		}
		
		public function index()
		{
			$this->requireAuth(); // nao passa daqui se o user nao estiver logado

			$uid = (int)AuthenticatorPlugin::getInstance()->getUID();
			$user = null;
			//$auth = AuthenticatorPlugin::getInstance(); // retorna uma class com o id do user logado
			//$userId = $auth->getUserId();

			//$user = User::findByUID($userId);
			
			if( $uid <= 0 || is_null( $user = User::findByUIDWithPoints( $uid ) ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$cp4 = $cp3 = $street = $locality = $district = null ;

				$address = ( $user->getADID() > 0 ) ? Address::findByADID( $user->getADID() ) : null ;

				if( !is_null( $address ) )
				{
					$cp4 = $address->getCP4();
					$cp3 = $address->getCP3();

					$street = $address->getStreet();

					$locality = $address->getLocality();
					$district = $address->getDistrict();
				}

				$points = $user->getTotalPoints();
				$level = UserController::calculateLevel( $points ) ;
				$next_level_points = UserController::calculatePointsByLevel( 1 + $level ) ;

				$this->respond->setJSONResponse( array( 'uid' => $user->getUID(),
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
														'facebook_uid' => $user->getFacebookUID(),
														'level' => $level,
														'next_level_points' => $next_level_points,
														'points' => $points ) );

				$this->respond->setJSONCode( R_STATUS_OK );
				
			}

			$this->respond->renderJSON( static::$status );
		}
		
		public function edit()
		{
			$this->requireAuth(); // nao passa daqui se o user nao estiver logado


			$user = AuthenticatorPlugin::getInstance()->getUser();
			//$auth = AuthenticatorPlugin::getInstance(); // retorna o id do user logado
			//$userId = $auth->getUserId();

			$resp = array();
			
			//$user = User::findByUID($userId);
			
			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$isInHouse = is_null( $user->getFacebookUID() ) ;

				$name = valid_request_var('name');
				$email = valid_request_var('email');
				$adid = valid_request_var('adid');
				$address2 = valid_request_var('address2');
				$birth = valid_request_var('birth');
				$password = valid_request_var('password', false);
				$password_old = valid_request_var('password_old', false);
				

				// Must verifiy if mail is not taken
				if( $isInHouse && !is_null($email) && $email !== $user->getEmail() && !is_null( User::findByEmail( $email ) ) )
					$this->respond->setJSONCode( R_USER_ERR_EMAIL_EXISTS );

				// If changing password, must provide old password
				elseif( $isInHouse && !is_null($password) && is_null( $password_old ) )
					$this->respond->setJSONCode( R_USER_MUST_SEND_OLD_P );

				// Old password must be correct
				elseif( $isInHouse && !is_null($password) && User::compareWithHashedPass($password_old, $user->getPassword()) === false )
					$this->respond->setJSONCode( R_USER_BAD_OLD_PASS );

				// if changic address, must check if adid exists
				elseif( !is_null( $adid ) && is_null( Address::findByADID( (int)$adid ) ) )
					$this->respond->setJSONCode( R_USER_ERR_INV_ADID );

				else
				{
				
					if( !is_null($adid) )
						$user->setADID((int)$adid);

					if( !is_null($address2) )
						$user->setAddress2($address2);

					if( $isInHouse )
					{
						if( !is_null($name) )
							$user->setName($name);

						if( !is_null($birth) )
							$user->setBirth($birth);

						if( !is_null($email) )
							$user->setEmail($email);

						if( !is_null($password) )
							$user->setPassword( User::saltPass( $password ) );
					}

					if( !$user->save() )
						$this->respond->setJSONCode( R_GLOB_ERR_SAVE_UNABLE );

					else
					{
						if( $isInHouse && !is_null($password) )
							Session::resetUserTokens( $user->getUID() );
						
						$this->respond->setJSONCode( R_STATUS_OK );
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


			if( is_null($name) || is_null($birth) || is_null( $email ) || is_null( $password ) )
				$this->respond->setJSONCode( R_USER_ERR_PARAMS );

			else if( !is_null( User::findByEmail( $email ) ) )
				$this->respond->setJSONCode( R_USER_ERR_EMAIL_EXISTS );

			elseif( !is_null( $adid ) && is_null( Address::findByADID($adid) ) )
				$this->respond->setJSONCode( R_USER_ERR_INV_ADID );

			else
			{
				$user = new User();
				
				$user->setName($name);
				$user->setADID($adid);
				$user->setAddress2($address2);
				$user->setBirth($birth);

				$user->setEmail( strtolower($email) );
				$user->setPassword( User::saltPass($password) );

				$user->setSeed( Controller::genRand('sha256', true) );

				$success = $user->save();

				if( !$success )
					$this->respond->setJSONCode(  R_GLOB_ERR_SAVE_UNABLE );

				else
				{
					$this->respond->setJSONResponse( array( 'uid' => $user->getUID() ) );
					$this->respond->setJSONCode( R_STATUS_OK  );
				}
					
			}
			
			$this->respond->renderJSON( static::$status );
		}
		
		public function list_promotions_won()
		{
			$this->requireAuth();
			
			$user = AuthenticatorPlugin::getInstance()->getUser();
			$wonPrizes = null;
			//$auth = AuthenticatorPlugin::getInstance(); // retorna o id do user logado
			//$userId = $auth->getUserId();
			
			//$user = User::findByUID($userId);
			
			if( is_null( $user ) )
				$this->respond->setJSONCode ( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$prizes = PrizeCode::findOwnUnused( $user->getUID() );

				$this->respond->setJSONResponse( PrizeCode::_fillTradablePrizes( $prizes ) );
				$this->respond->setJSONCode( R_STATUS_OK );
			}
			$this->respond->renderJSON( static::$status );
		}

		public function list_badges_won()
		{
			$this->requireAuth();

			$user = AuthenticatorPlugin::getInstance()->getUser();
			//$userId = $auth->getUID();

			// $user = User::findByUID($userId);

			
			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$resp = Badge::findByUID( $user->getUID() );
				$response = array();
					
				foreach ( $resp as $b)
				{
					$response[] = array('bid' => $b->getBID(),
										'name' => $b->getName(),
										'description' => $b->getDescription(),
										'image' => Controller::formatURL( $b->getImageSRC() ),
										'aquis_date' => $b->getAquisDate() );
										
				}

				$this->respond->setJSONResponse( $response );
				$this->respond->setJSONCode( R_STATUS_OK );
			}
			$this->respond->renderJSON( static::$status);
		}



		public function promotion_enroll()
		{
			$this->requireAuth();
			
			$pid = (int)valid_request_var('promotion');

			$user = AuthenticatorPlugin::getInstance()->getUser();
			$promo = null;

			
			if( is_null( $user ) || is_null( $pid ) )
				$this->respond->setJSONCode ( R_USER_ERR_PARAMS );

			elseif( is_null( $promo = Promotion::findByPID($pid) ) )
				$this->respond->setJSONCode ( R_USER_BAD_PROMO );
			
			else
			{
				$userProm = new UserPromotion();

				$userProm->participate($pid, $user->getUID() );
				$userProm->setInitDate(time());

				try {
					if( $success = $userProm->save() )
						$this->respond->setJSONResponse( array('upid' => $userProm->getUPID()) );

					$this->respond->setJSONCode( $success ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
				} catch(PDOException $e) {
					$this->respond->setJSONCode( R_USER_PROMO_EXPIRED );
				}
				
			}

			$this->respond->renderJSON( static::$status );
		}




		public function list_prizes_trading()
		{
			$this->requireAuth();

			$user = AuthenticatorPlugin::getInstance()->getUser();

			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$prizes = PrizeCode::findOwnTrading( $user->getUID() );

				$this->respond->setJSONResponse( PrizeCode::_fillTradablePrizes( $prizes ) );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		}
		
		public function list_prizes_tradable()
		{
			$this->requireAuth();

			$user = AuthenticatorPlugin::getInstance()->getUser();

			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$prizes = PrizeCode::findOwnTradable( $user->getUID() );

				$this->respond->setJSONResponse( PrizeCode::_fillTradablePrizes( $prizes ) );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		}



		public function reset_password()
		{
			$this->requireNoAuth();


			$email = valid_request_var('email');
			$user = null;

			if( is_null( $email ) )
				$this->respond->setJSONCode( R_USER_EMAIL_MISSING );
			
			elseif( is_null( $user = User::findByEmail( $email ) ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			elseif( !is_null( $user->getFacebookUID() ) )
				$this->respond->setJSONCode( R_USER_RESET_FACE_USER );

			else
			{
				$token = Controller::genRand64() ;

				$user->setResetToken($token);
				$user->setResetTokenValidity(time()+600); // 10 minutos para fazer reset

				if( !$user->save() )
					$this->respond->setJSONCode( R_GLOB_ERR_SAVE_UNABLE );

				else
				{
					$ret = Controller::sendCustomMail($user->getEmail(), MAIL_SUBJECT_RESET_PASS, "text/plain",
							"Foi pedido que fosse feito reset da password de acesso da sua conta na aplicação Tlantic PromGame Mobile.\r\n\r\n".
							"Por favor siga o link: https://" . $_SERVER['SERVER_NAME'] . "/" . BASE_URI ."reset_password/${token}\r\n\r\n".
							"Se este pedido não foi efectuado por si, por favor ignore este e-mail" . MAIL_SIGNATURE, true );

					$this->respond->setJSONCode( $ret ? R_STATUS_OK : R_USER_SENDMAIL_ERROR );
				}
			}

			$this->respond->renderJSON( static::$status );
		}



		public function reset_password_confirmation()
		{
			// function dump()
			// {
			//     $memcache = new Memcache();
			//     $memcache->connect('127.0.0.1', 11211) or die ("Could not connect to memcache server");

			//     $list = array();
			//     $allSlabs = $memcache->getExtendedStats('slabs');
			//     $items = $memcache->getExtendedStats('items');
			//     foreach($allSlabs as $server => $slabs) {
			//         foreach($slabs AS $slabId => $slabMeta) {
			//            $cdump = $memcache->getExtendedStats('cachedump',(int)$slabId);
			//             foreach($cdump AS $keys => $arrVal) {
			//                 if (!is_array($arrVal)) continue;
			//                 foreach($arrVal AS $k => $v) {                   
			//                     $list[] = $k;
			//                 }
			//            }
			//         }
			//     }

			//     var_dump($list);
			// }

			$this->requireNoAuth();


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
					$renderText = "Excedeu o tempo permitido para fazer reset à password";

				else
				{
					Session::resetUserTokens( $user->getUID() );

 					$pass = substr(str_shuffle(str_repeat("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ#()+", 3)), 10, 8);

 					$user->setPassword( User::saltPass($pass) );
 					$user->setResetToken(null);
 					$user->setResetTokenValidity(0);

					if( !$user->save() )
						$renderText = "Erro: Impossível salvar";

					else
					{
						$ret = Controller::sendCustomMail($user->getEmail(), MAIL_SUBJECT_RESET_PASS, "text/plain",
								"No seguimento do pedido de reset da password de acesso à sua conta,\r\n" .
								"enviamos-lhe uma password temporária, que deverá ser alterada de imediato, logo após o login.\r\n\r\n".
								"E-mail: {$user->getEmail()}\r\nPassword: {$pass}" . MAIL_SIGNATURE, true );

						if( !$ret )
							$renderText = static::$status[R_USER_SENDMAIL_ERROR];
						else
							$renderText = "Nova password de acesso temporária enviada para o seu e-mail.\r\n<br>".
										  "Atenção: Deve alterá-la de imediato, logo após o login.";
							
					}
				
					
				}
			
			}

			$this->respond->renderHTML( $renderText );
		}




		public function register_gcm()
		{
			$this->requireAuth(); // nao passa daqui se o user nao estiver logado

			$user = AuthenticatorPlugin::getInstance()->getUser();
			$token_gcm = valid_request_var('token_gcm');
			$gcm = null;

			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else if( is_null( $token_gcm ) )
				$this->respond->setJSONCode( R_USER_ERR_PARAMS );

			else
			{
				if ( !is_null( $gcm = UserGCM::findByToken( $token_gcm ) ) )
				{
					if( $gcm->getUID() !== $user->getUID() )
						$this->respond->setJSONCode( R_USER_DONT_OWN_GCM );

					else
						$this->respond->setJSONCode( R_STATUS_OK );
				}
				else
				{
					$gcm = UserGCM::instantiate( $user, $token_gcm );

					$this->respond->setJSONCode( $gcm->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
				}

			}

			$this->respond->renderJSON( static::$status );

		}



		public function unregister_gcm()
		{
			$this->requireAuth(); // nao passa daqui se o user nao estiver logado

			$user = AuthenticatorPlugin::getInstance()->getUser();
			$token_gcm = valid_request_var('token_gcm');
			$gcm = null;

			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else if( is_null( $token_gcm ) )
				$this->respond->setJSONCode( R_USER_ERR_PARAMS );

			else if ( is_null( $gcm = UserGCM::findByToken( $token_gcm ) ) )
				$this->respond->setJSONCode( R_USER_GCM_NOT_FOUND );

			else if( $gcm->getUID() !== $user->getUID() )
				$this->respond->setJSONCode( R_USER_DONT_OWN_GCM );

			else
				$this->respond->setJSONCode( $gcm->delete() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );

			$this->respond->renderJSON( static::$status );

		}
		
		
		public function showPoints() {
			
			$this->requireAuth();

			$user = AuthenticatorPlugin::getInstance()->getUser();

			$u = $user->getUID();
			var_dump($u);
			
			if( is_null( $user ) )
				$this->respond->setJSONCode( R_USER_ERR_USER_NOT_FOUND );

			else
			{
				$points = UserPoints::showUserPoints( $user->getUID() );

				$this->respond->setJSONResponse( $points );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		
		}

	}
	
	
?>