<?php

	class SessionController extends Controller {
	

		public function __configure()
		{
			//$this->checkAuth();
		}
		
		
		
		public function create()
		{
			$render_code = 0;
			$resp = array();
			
			if( $this->checkAuth() )
				$render_code = 1;
				
			else
			{
				$mail = valid_request( 'email' );
				$pass = valid_request( 'password' );

				if( is_null( $mail ) || is_null( $pass ) )
					$render_code = 2;
				
				else
				{
					

				}
			}
			
			$this->respond->renderJSON( $resp, $render_code );
		
		}
	
	
	}
?>