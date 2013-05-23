<?php


	DEFINE( 'R_TRAD_ERR_PARAM'					, 0x10 );

	DEFINE( 'R_TRAD_ERR_NO_MATCH_USER'			, 0x20 );
	DEFINE( 'R_TRAD_ERR_PROMO_USED'				, 0x21 );
	DEFINE( 'R_TRAD_ERR_PROMO_UNTRANSFERABLE'	, 0x22 );
	DEFINE( 'R_TRAD_ERR_TRADE_FAIL'				, 0x23 );

	DEFINE( 'R_TRAD_ERR_ALREADY_TRADING'		, 0x30 );
	DEFINE( 'R_TRAD_ERR_NOT_TRADING'			, 0x31 );

	DEFINE( 'R_TRAD_ERR_PROMO_SELECT'			, 0x60 );
	DEFINE( 'R_TRAD_ERR_PROMO_SAME'				, 0x61 );
	DEFINE( 'R_TRAD_ERR_USER_SAME'				, 0x62 );


	class TradingController extends Controller {

		private static $status = array(
				R_TRAD_ERR_PARAM				=> 'Erro de parâmetros',
				
				R_TRAD_ERR_NO_MATCH_USER		=> 'Erro de utilizador não coincide',

				R_TRAD_ERR_PROMO_SELECT			=> 'Erro a encontrar a promoção selecionada ou de sugestão',
				R_TRAD_ERR_PROMO_SAME			=> 'A promoção selecionada e de sugestão não podem ser as mesmas',
				R_TRAD_ERR_USER_SAME			=> 'Não pode trocar promoções consigo próprio',

				R_TRAD_ERR_ALREADY_TRADING		=> 'Promoção já se encontra em trading',
				R_TRAD_ERR_NOT_TRADING			=> 'Promoção não se encontra em trading',
			);

	

		protected function __configure()
		{
			$this->requireAuth();
		}


		public function index()
		{
			$user = Authenticator::getInstance()->getUser();

			if( is_null( $user ) )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else
			{
				$prizes = PrizeCode::findOthersTradable( $user->getUID() );

				$this->respond->setJSONResponse( PrizeCode::_fillTradablePrizes( $prizes ) );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		}

		/*public function _index()
		{

			$pc = new PrizeCode();
			$pc->setEmissionDate(time());
			$pc->genValidCode("12345678901234567890123456789012");
			$pc->setUPID(1);
			$pc->setOwnerUID(1);
			$pc->save();



		}*/
		
		/*public function sendPromotionToTrading($pid) {
		
			$user = Authenticator::getInstance()->getUser();

			if( is_null( $user ) ) 
			{
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );
			} 
			else if($user->getUID() != PrizeCode::getOwnerUID()) 
			{
				$this->respond->setJSONCode( R_TRAD_ERR_NO_MATCH_USER );
			}
			else
			{

				$this->respond->setJSONResponse( PrizeCode::sendPromoToTrading() );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		
		}*/

		public function suggest_trading()
		{
		
			$pcid = (int)valid_request_var( 'prizecode' );
			$suggestId = (int)valid_request_var( 'suggestion' );
			$time = time();

		
			$user = Authenticator::getInstance()->getUser();

			$prizecodeOthers = null;
			$prizecodeSuggest = null;
			
			if( is_null( $user ) || $pcid <= 0 || $suggestId <= 0 )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else if( $pcid == $suggestId )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SAME );

			else if( 	is_null( $prizecodeOthers  = PrizeCode::findOthersTradable( $user->getUID(), $time, $pcid      ) )
					 || !is_array( $prizecodeOthers ) || count( $prizecodeOthers ) !== 1
					 || is_null( $prizecodeSuggest = PrizeCode::findOwnTradable   ( $user->getUID(), $time, $suggestId ) )
					 || !is_array( $prizecodeSuggest ) || count( $prizecodeSuggest ) !== 1 )
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_SELECT );

			else if( $prizecodeOthers[0]->getOwnerUID() === $prizecodeSuggest[0]->getOwnerUID() ) // verificar se o utilizador coincide
					$this->respond->setJSONCode( R_TRAD_ERR_USER_SAME );
				
			else
			{

				$traddSuggest = new TradingSuggestion($prizecodeSuggest[0], $prizecodeOthers[0]);
				$traddSuggest->setDate( $time );

				$this->respond->setJSONCode( $traddSuggest->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );

			}

			$this->respond->renderJSON( static::$status );
		
		}



		private function _send_remove_trading( $sendTo )
		{
		
			$pcid = (int)valid_request_var( 'prizecode' );
		
			$user = Authenticator::getInstance()->getUser();

			$prizecode = null;
			$upromo = null;
			$promotion = null;
			
			if( is_null( $user )
					|| is_null( $prizecode = PrizeCode::findByPCID($pcid) )
					|| is_null( $upromo = UserPromotion::findByUPID($prizecode->getUPID() ) )
					|| is_null( $promotion = Promotion::findByPID( $upromo->getPID() ) ) )  // verificar se utilizador é válido
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );

			else if($user->getUID() != $prizecode->getOwnerUID()) // verificar se o utilizador coincide
				$this->respond->setJSONCode( R_TRAD_ERR_NO_MATCH_USER );

			else if($promotion->isTransferable() === false)
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_UNTRANSFERABLE );

			else if($prizecode->getUtilizationDate() !== 0) // verificar se nao foi utilizada
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_USED );

			else if( $prizecode->inTrading() === $sendTo ) // verificar se nao foi utilizada
				$this->respond->setJSONCode( $sendTo ? R_TRAD_ERR_ALREADY_TRADING : R_TRAD_ERR_NOT_TRADING );
			
			else
			{
				
				$prizecode->setTrading( $sendTo );

				$this->respond->setJSONCode( $prizecode->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );
		
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

		
		/*public function remove_from_trading() {
		
			$pcid = (int)valid_request_var( 'prizecode' );
		
			$user = Authenticator::getInstance()->getUser();

			$prizecode = PrizeCode::findByPCID($pcid);			
			$upromoId = $prizecode->getUPID();
			
			$upromo = UserPromotion::findByUPID($upromoId);
			$promoId = $upromo->getPID();
			
			$promotion = Promotion::findByPID($promoId);
			
			if( is_null( $user ) ) // verificar se utilizador é válido
			{
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );
			} 
			else if($user->getUID() != $prizecode->getOwnerUID()) // verificar se o utilizador coincide
			{
				$this->respond->setJSONCode( R_TRAD_ERR_NO_MATCH_USER );
			} 
			else if($prizecode->getUtilizationDate() != 0) // verificar se nao foi utilizada
			{
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_USED );
			}
			else if($promotion->isTransferable() === false) {
				$this->respond->setJSONCode( R_TRAD_ERR_PROMO_UNTRANSFERABLE );
			}
			else
			{
				$uid = $user->getUID();
				
				$prizecode->setTrading(false);
				$result = $prizecode->remove_promo($pcid, $uid);
				
				if($result === false) {
					$this->respond->setJSONCode( R_TRAD_ERR_TRADE_FAIL );
				}
				
				// $this->respond->setJSONResponse( PrizeCode::send_promo($pcid, $uid) );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		
		}*/
		
	}


?>