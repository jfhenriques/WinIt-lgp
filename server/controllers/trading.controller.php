<?php


	DEFINE( 'R_TRAD_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_TRAD_ERR_NO_MATCH_USER'				, 0x20 );
	DEFINE( 'R_TRAD_ERR_PROMO_USED'				, 0x30 );
	DEFINE( 'R_TRAD_ERR_PROMO_UNTRANSFERABLE'				, 0x40 );
	DEFINE( 'R_TRAD_ERR_TRADE_FAIL'				, 0x50 );

	class TradingController extends Controller {

		private static $status = array(
				R_TRAD_ERR_PARAM				=> 'Erro de parâmetros',
				
				R_TRAD_ERR_NO_MATCH_USER				=> 'Erro de utilizador não coincide',
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
		
		public function send_to_trading() {
		
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
				
				$prizecode->setTrading(true);
				$result = $prizecode->send_promo($pcid, $uid);
				
				if($result === false) {
					$this->respond->setJSONCode( R_TRAD_ERR_TRADE_FAIL );
				}
				
				// $this->respond->setJSONResponse( PrizeCode::send_promo($pcid, $uid) );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		
		}


	}


?>