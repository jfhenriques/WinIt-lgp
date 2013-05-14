<?php


	DEFINE( 'R_TRAD_ERR_PARAM'				, 0x10 );




	class TradingController extends Controller {

		private static $status = array(
				R_TRAD_ERR_PARAM				=> 'Erro de parâmetros',
			);

	

		protected function __configure()
		{
			$this->requireAuth();
		}



		public function index()
		{
			//$uid = (int)valid_request_var('pid');
			$user = Authenticator::getInstance()->getUser();

			if( is_null( $user ) )
				$this->respond->setJSONCode( R_TRAD_ERR_PARAM );
			else
			{
				//$prizes = PrizeCode::findOthersTradable( $user->getUID() );
				$prizes = PrizeCode::findOwnTrading( $user->getUID() );
				//$prizes = PrizeCode::findOwnTradable( $user->getUID() );

				var_dump( $prizes );

			}

		}

		public function _index()
		{

			$pc = new PrizeCode();
			$pc->setEmissionDate(time());
			$pc->genValidCode("12345678901234567890123456789012");
			$pc->setUPID(1);
			$pc->setOwnerUID(1);
			$pc->save();



		}


	}


?>