<?php


	DEFINE( 'R_FACEBOOK_ERR_PARAM'			, 0x10 );
	
	DEFINE('FACEBOOK_VERIFY_TOKEN', '363da8015756e991c3e27970252e179a0b1962c03114155351f1157f2e2966d71101edbacd9cb698fbc155bfa258d4b5d904875722a8f67681316e3dc0a86141');
	DEFINE('FACEBOOK_EXPECTED_HUB_MODE', 'subscribe');


	class FacebookController extends Controller {

		private static $status = array(
				R_FACEBOOK_ERR_PARAM			=> 'Parâmetros não definidos',

			);

	

		protected function __configure()
		{
			$this->requireNoAuth();
		}
		
		
		public function echo_back()
		{
			$mode = valid_request_var( 'hub_mode' );
			$challenge = valid_request_var( 'hub_challenge' );
			$verify_token = valid_request_var( 'hub_verify_token' );

			if(    is_null( $mode )
				|| is_null( $challenge )
				|| is_null( $verify_token )
				|| $mode !== FACEBOOK_EXPECTED_HUB_MODE
			    || $verify_token !== FACEBOOK_VERIFY_TOKEN )
			{
				header('HTTP/1.0 403 Forbidden', true);
			}
			else
			{
				echo $challenge;
			}
		}

		public function users_update()
		{



			
		}
	
	}
?>