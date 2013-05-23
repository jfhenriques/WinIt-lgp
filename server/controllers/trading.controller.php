<?php


	DEFINE( 'R_TRAD_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_TRAD_ERR_NO_MATCH_USER'				, 0x20 );



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
		
			
		
		}


	}


?>