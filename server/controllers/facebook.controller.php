<?php


	DEFINE( 'R_FACEBOOK_ERR_PARAM'			, 0x10 );
	
	DEFINE('FACEBOOK_VERIFY_TOKEN', '3ff83e034de766e427b9c784c8fb340e6f0a84fb01945807da6578b03c9807e7');




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
				|| $mode !== 'subscribe'
			    || $verify_token !== FACEBOOK_VERIFY_TOKEN )
			{
				header('HTTP/1.0 403 Forbidden', true);
			}
			else
			{
				$this->respond->renderText( $challenge );
			}
		}



		public function receive_update_list()
		{
			//FacebookPlugin::getUsersInfo(array("100002664202285", "100006002378496"));

			$signature = valid_var( 'HTTP_X_HUB_SIGNATURE', $_SERVER );
			$payload = null;

			if( !is_null( $signature ) )
			{
				$payload = file_get_contents( 'php://input' );

				if( !empty( $payload ) && FacebookPlugin::verifyPayload( $signature, $payload ) )
				{



				}
			}

		}
	
	}
?>