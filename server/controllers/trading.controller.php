<?php


	DEFINE( 'R_TRAD_ERR_PARAM'					, 0x10 );

	DEFINE( 'R_TRAD_ERR_NO_MATCH_USER'			, 0x20 );
	DEFINE( 'R_TRAD_ERR_PROMO_USED'				, 0x21 );
	DEFINE( 'R_TRAD_ERR_PROMO_UNTRANSFERABLE'	, 0x22 );
	DEFINE( 'R_TRAD_ERR_TRADE_FAIL'				, 0x23 );

	DEFINE( 'R_TRAD_ERR_ALREADY_TRADING'		, 0x30 );
	DEFINE( 'R_TRAD_ERR_NOT_TRADING'			, 0x31 );

	DEFINE( 'R_TRAD_ERR_PROMO_NOT_AVAIL'		, 0x40 );
	DEFINE( 'R_TRAD_ERR_PROMO_STUPID'			, 0x41 );

	DEFINE( 'R_TRAD_ERR_PROMO_SELECT'			, 0x60 );
	DEFINE( 'R_TRAD_ERR_PROMO_SAME'				, 0x61 );
	DEFINE( 'R_TRAD_ERR_USER_SAME'				, 0x62 );
	DEFINE( 'R_TRAD_ERR_NOT_OWNER'				, 0x63 );
	DEFINE( 'R_TRAD_ERR_ALREADY_SUGGESTED'		, 0x64 );

	DEFINE( 'R_TRAD_ERR_ALREADY_ACCEPTED'		, 0x70 );
	DEFINE( 'R_TRAD_ERR_ALREADY_REJECTED'		, 0x71 );
	DEFINE( 'R_TRAD_ERR_ALREADY_CANCELED'		, 0x72 );

	DEFINE( 'R_TRAD_ERR_SUGGESTION_CANCELED'	, 0x73 );


	class TradingController extends Controller {

		private static $status = array(
				R_TRAD_ERR_PARAM				=> 'Erro de parâmetros',
				
				R_TRAD_ERR_NO_MATCH_USER		=> 'Erro de utilizador não coincide',

				R_TRAD_ERR_PROMO_SELECT			=> 'Erro ao encontrar a promoção selecionada ou de sugestão',
				R_TRAD_ERR_PROMO_SAME			=> 'A promoção selecionada e de sugestão não podem ser as mesmas',
				R_TRAD_ERR_USER_SAME			=> 'Não pode trocar promoções consigo próprio',
				R_TRAD_ERR_NOT_OWNER			=> 'A promoção não pertence ao utilizador',

				R_TRAD_ERR_ALREADY_SUGGESTED	=> 'Sugestão já feita anteriormente',

				R_TRAD_ERR_PROMO_NOT_AVAIL		=> 'Promoção não disponível por ter expirado, por não pertencer ao utilizador, ou por não se encontrar em trading',

				R_TRAD_ERR_ALREADY_TRADING		=> 'Promoção já se encontra em trading',
				R_TRAD_ERR_NOT_TRADING			=> 'Promoção não se encontra em trading',

				R_TRAD_ERR_ALREADY_ACCEPTED		=> 'A sugestão já foi aceite anteriormente',
				R_TRAD_ERR_ALREADY_REJECTED		=> 'A sugestão já foi rejeitada anteriormente',
				R_TRAD_ERR_ALREADY_CANCELED		=> 'A sugestão já foi cancelada anteriormente',

				R_TRAD_ERR_SUGGESTION_CANCELED	=> 'A sugestão foi cancelada pelo utilizador',
			);


		const STATE_UNREVIEWED	= 0;
		const STATE_REJECTED	= 1;
		const STATE_ACCEPTED	= 2;
		const STATE_CANCELED	= 3;


		protected function __configure()
		{
			$this->requireAuth();
		}



		public function index()
		{
			//$user = AuthenticatorPlugin::getInstance()->getUser();
			$uid = AuthenticatorPlugin::getInstance()->getUID();

			if( $uid <= 0 )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else
			{
				$prizes = PrizeCode::findOthersTrading( $uid );

				$this->respond->setJSONResponse( PrizeCode::_fillTradablePrizes( $prizes ) );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		}




		public function in_trading_suggestions()
		{
		
			$pcid = (int)valid_request_var( 'prizecode' );
			$time = time();
		
			//$user = AuthenticatorPlugin::getInstance()->getUser();
			$uid = AuthenticatorPlugin::getInstance()->getUID();
			$prizeCodeArr = null;

			$sugestions = null;
			
			if( $uid <= 0 || $pcid <= 0 )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else if(	is_null( $prizeCodeArr = PrizeCode::findOwnTrading( $uid, $time, $pcid ) )
					 || !is_array( $prizeCodeArr ) || count( $prizeCodeArr ) !== 1 )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_NOT_AVAIL );
			
			else if(	is_null( $sugestions  = TradingSuggestion::findPrizeSuggestions( $pcid, $time ) )
					 || !is_array( $sugestions ) )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_STUPID );

			else
			{
				$ret = array();

				foreach($sugestions as $s)
				{
					$ret[] = array('pcid' => $s->getPCIDOrig(),
								   'pid' => $s->getPID(),
								   'max_util_date' => $s->getMaxUtilizationDate(),
								   'name' => $s->getPromotionName(),
								   'image' => Controller::formatURL( $s->getPromotionImageSRC() ),
						);

				}

				$this->respond->setJSONResponse( $ret );
				$this->respond->setJSONCode( R_STATUS_OK );

			}

			$this->respond->renderJSON( static::$status );
		
		}


		private function accept_reject_trading( $accept )
		{
		
			$pcid = (int)valid_request_var( 'prizecode' );
			$suggestId = (int)valid_request_var( 'suggest' );
			$time = time();

			//$user = AuthenticatorPlugin::getInstance()->getUser();
			$uid = AuthenticatorPlugin::getInstance()->getUID();

			$suggestion = null;
			$prizeMine = null;
			$prizeSuggest = null;
			
			if( $uid <= 0 || $pcid <= 0 || $suggestId <= 0 )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else if( $pcid == $suggestId )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SAME );

			else if( 	is_null( $prizeMine = PrizeCode::findOwnTrading( $uid, $time, $pcid ) )
					 || !is_array( $prizeMine ) || count( $prizeMine ) !== 1
					 || is_null( $suggestion = TradingSuggestion::findByTransaction(
					 													$pcid,
					 													$prizeMine[0]->getTransactionID(),
					 													$suggestId ) )
					 || is_null( $prizeSuggest = PrizeCode::findOthersTradable( $uid, $time, $suggestId ) )
					 || !is_array( $prizeSuggest ) || count( $prizeSuggest ) !== 1 )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SELECT );

			else if( $prizeSuggest[0]->getOwnerUID() === $prizeMine[0]->getOwnerUID() ) // verificar se o utilizador coincide
				$this->respond->setJSONCode( R_TRAD_ERR_USER_SAME );
				
			else if( $suggestion->getState() === self::STATE_ACCEPTED )
				$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_ACCEPTED );

			else if( $suggestion->getState() === self::STATE_REJECTED )
				$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_REJECTED );

			else if( $suggestion->getState() === self::STATE_CANCELED )
				$this->respond->setJSONCode( R_TRAD_ERR_SUGGESTION_CANCELED );

			else
			{

				$suggestion->setEndDate( $time );

				$pMine = $prizeMine[0];
				$pSuggest = $prizeSuggest[0];

				if( $accept )
				{

					$dbh = DbConn::getInstance()->getDB();
					$dbh->beginTransaction();

					try {

						$sendUID = $pSuggest->getOwnerUID();

						$pMine->setOwnerUID( $pSuggest->getOwnerUID() );
						$pMine->setTrading(false);

						$pSuggest->setOwnerUID( $uid );
						$pSuggest->setTrading(false);

						$suggestion->setState( self::STATE_ACCEPTED );

						$success = $pMine->save() && $pSuggest->save() && $suggestion->save() ;

						if( $success )
						{
							$dbh->commit();

							$this->respond->setJSONCode( R_STATUS_OK );

							GCMPlugin::sendUserSuggest(
											$sendUID,
											GCMPlugin::STATE_ACCEPT,
											$pSuggest->getPCID(),
											$pSuggest->getPID(),
											$pSuggest->getPromotionName(),
											Controller::formatURL( $pSuggest->getPromotionImageSRC() ),
											$pMine->getPCID(),
											$pMine->getPID(),
											$pMine->getPromotionName(),
											Controller::formatURL( $pMine->getPromotionImageSRC() ),
											$time );
						}

						else
						{
							$dbh->rollBack();
							$this->respond->setJSONCode( R_GLOB_ERR_SAVE_UNABLE );
						}

					} catch(PDOException $e) {
						$dbh->rollBack();

						$this->respond->setJSONCode( R_GLOB_ERR_SAVE_UNABLE );
					}

				}
				else
				{
					$suggestion->setState( self::STATE_REJECTED );

					if( !$suggestion->save() )
						$this->respond->setJSONCode( R_GLOB_ERR_SAVE_UNABLE );

					else
					{
						$this->respond->setJSONCode( R_STATUS_OK );

						GCMPlugin::sendUserSuggest(
										$pSuggest->getOwnerUID(),
										GCMPlugin::STATE_REJECT,
										$pSuggest->getPCID(),
										$pSuggest->getPID(),
										$pSuggest->getPromotionName(),
										Controller::formatURL( $pSuggest->getPromotionImageSRC() ),
										$pMine->getPCID(),
										$pMine->getPID(),
										$pMine->getPromotionName(),
										Controller::formatURL( $pMine->getPromotionImageSRC() ),
										$time );
					}
				}

			}

			$this->respond->renderJSON( static::$status );
		
		}

		public function accept_trading()
		{
			$this->accept_reject_trading( true );
		}
		public function reject_trading()
		{
			$this->accept_reject_trading( false );
		}




		public function suggest_trading()
		{
		
			$pcid = (int)valid_request_var( 'prizecode' );
			$suggestId = (int)valid_request_var( 'suggest' );
			$time = time();

			//$user = AuthenticatorPlugin::getInstance()->getUser();
			$uid = AuthenticatorPlugin::getInstance()->getUID();

			$prizeWanted = null;
			$prizeMine = null;
			
			if( $uid <= 0 || $pcid <= 0 || $suggestId <= 0 )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else if( $pcid == $suggestId )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SAME );

			else if( 	is_null( $prizeWanted  = PrizeCode::findOthersTrading( $uid, $time, $pcid ) )
					 || !is_array( $prizeWanted ) || count( $prizeWanted ) !== 1
					 || is_null( $prizeMine = PrizeCode::findOwnTradable( $uid, $time, $suggestId ) )
					 || !is_array( $prizeMine ) || count( $prizeMine ) !== 1 )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SELECT );

			else if( $prizeWanted[0]->getOwnerUID() === $prizeMine[0]->getOwnerUID() ) // verificar se o utilizador coincide
					$this->respond->setJSONCode( R_TRAD_ERR_USER_SAME );

			else
			{

				$success = false;

				$pMine = $prizeMine[0];
				$pWant = $prizeWanted[0];


				if( !is_null( $suggestion = TradingSuggestion::findByTransaction(
																		$pWant->getPCID(),
																		$pWant->getTransactionID(),
																		$pMine->getPCID() ) ) )
				{

					switch( $suggestion->getState() )
					{
						case self::STATE_UNREVIEWED:
							$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_SUGGESTED );
									
							break;

						case self::STATE_ACCEPTED:
							$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_ACCEPTED );
									
							break;

						case self::STATE_REJECTED:
							$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_REJECTED );
									
							break;

						default:

							$suggestion->setState( self::STATE_UNREVIEWED );
							$suggestion->setEndDate( 0 );

							$this->respond->setJSONCode( ( $success = $suggestion->save() ) ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );

							break;
					}

				}
					
				else
				{

					$traddSuggest = TradingSuggestion::instantiate($pMine, $pWant);
					$traddSuggest->setDate( $time );

					$this->respond->setJSONCode( ( $success = $traddSuggest->save() ) ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );

				}

				if( $success )
				{

					GCMPlugin::sendUserSuggest(
									$pWant->getOwnerUID(),
									GCMPlugin::STATE_NEW_SUGGEST,
									$pWant->getPCID(),
									$pWant->getPID(),
									$pWant->getPromotionName(),
									Controller::formatURL( $pWant->getPromotionImageSRC() ),
									$pMine->getPCID(),
									$pMine->getPID(),
									$pMine->getPromotionName(),
									Controller::formatURL( $pMine->getPromotionImageSRC() ),
									$time );

				}

			}

			$this->respond->renderJSON( static::$status );
		
		}

		public function unsuggest_trading()
		{
		
			$pcid = (int)valid_request_var( 'prizecode' );
			$suggestId = (int)valid_request_var( 'suggest' );
			$time = time();

			//$user = AuthenticatorPlugin::getInstance()->getUser();
			$uid = AuthenticatorPlugin::getInstance()->getUID();

			$suggestion = null;
			$prizeWanted = null;
			$prizeMine = null;
			
			if( $uid <= 0 || $pcid <= 0 || $suggestId <= 0 )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else if( $pcid == $suggestId )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SAME );

			else if( 	is_null( $prizeWanted = PrizeCode::findOthersTrading( $uid, $time, $pcid ) )
					 || !is_array( $prizeWanted ) || count( $prizeWanted ) !== 1
					 || is_null( $suggestion = TradingSuggestion::findByTransaction(
					 													$pcid,
					 													$prizeWanted[0]->getTransactionID(),
					 													$suggestId ) )
					 || is_null( $prizeMine = PrizeCode::findOwnTradable( $uid, $time, $suggestId ) )
					 || !is_array( $prizeMine ) || count( $prizeMine ) !== 1 )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SELECT );

			else if( $prizeWanted[0]->getOwnerUID() === $prizeMine[0]->getOwnerUID() ) // verificar se o utilizador coincide
				$this->respond->setJSONCode( R_TRAD_ERR_USER_SAME );
				
			else if( $suggestion->getState() === self::STATE_ACCEPTED )
				$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_ACCEPTED );

			else if( $suggestion->getState() === self::STATE_REJECTED )
				$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_REJECTED );

			else if( $suggestion->getState() === self::STATE_CANCELED )
				$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_CANCELED );

			else
			{
				$suggestion->setEndDate( $time );
				$suggestion->setState( self::STATE_CANCELED );
				
				$this->respond->setJSONCode( $suggestion->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
			}

			$this->respond->renderJSON( static::$status );
		
		}


		public static function only_one_arr( $arr, &$out )
		{
			if( is_array( $arr ) && count( $arr ) == 1 )
			{
				$out = $arr[0];

				return true;
			}

			return false;
		}

		private function _send_remove_trading( $sendTo )
		{
			$pcid = (int)valid_request_var( 'prizecode' );
		

			$uid = AuthenticatorPlugin::getInstance()->getUID();
			$time = time();

			if( $uid <= 0 || $pcid <= 0 )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else
			{
				$not = null;
				$in  = null;

				// Search only if needed, in this case, if not not found in first condition, saving one new database connection
				if( $sendTo )
				{
					if( !self::only_one_arr( PrizeCode::findOwnTradable( $uid, $time, $pcid ) , $not) )
						self::only_one_arr( PrizeCode::findOwnTrading ( $uid, $time, $pcid ) , $in );
				}
				else
				{
					if( !self::only_one_arr( PrizeCode::findOwnTrading( $uid, $time, $pcid ) , $in) )
						self::only_one_arr( PrizeCode::findOwnTradable ( $uid, $time, $pcid ) , $not );	
				}


				if( is_null( $in ) && is_null($not) )
					$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SELECT );

				else if( $sendTo && is_null($not) )
					$this->respond->setJSONCode( R_TRAD_ERR_ALREADY_TRADING );

				else if( !$sendTo && is_null($in) )
					$this->respond->setJSONCode( R_TRAD_ERR_NOT_TRADING );

				else
				{

					$prizecode = $sendTo ? $not : $in;

					$prizecode->setTrading( $sendTo );

					if( $sendTo )
						$not->incTransactionID();

					$this->respond->setJSONCode( $prizecode->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );

				}
			}

			$this->respond->renderJSON( static::$status );
		}

		public function send_to_trading()
		{
			$this->_send_remove_trading( true );
		}

		public function remove_from_trading()
		{
			$this->_send_remove_trading( false );
		}


		
	}
