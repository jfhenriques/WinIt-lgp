<?php

	DEFINE( 'R_BADGE_ERR_PARAMS'			, 0x10 );
	DEFINE( 'R_BADGE_ERR_USER_NOT_FOUND'	, 0x20 );




	class BadgeController extends Controller {
	
		private static $status = array(
			R_BADGE_ERR_PARAMS			=> 'Parâmetros não definidos',
			R_BADGE_ERR_USER_NOT_FOUND	=> 'Badge não encontrada',
		);
	
		/*
		 * Ensure that the user is logged
		 */
		public function __configure()
		{
			$this->requireAuth();
		}
		
		public function index()
		{
		
			/*$pid = (int)valid_request_var('promotion');
			$promo = null;

			if( $pid <= 0 )
				$this->respond->setJSONCode( R_PROM_ERR_PARAMS );

			elseif( is_null( $promo = Promotion::findByPID($pid) ) )
				$this->respond->setJSONCode( R_PROM_ERR_USER_NOT_FOUND );

			else
			{
				

			}
			*/
			// $this->respond->renderJSON( $resposta, $render_code, describeMessage( $render_code, static::$status ) );			
		}



		
		public function show()
		{			
			$bid = (int)valid_request_var('badge');
			$badge = null;

			if( $bid <= 0 )
				$this->respond->setJSONCode( R_BADGE_ERR_PARAMS );

			elseif( is_null( $badge = Badge::findByPID($bid) ) )
				$this->respond->setJSONCode( R_BADGE_ERR_USER_NOT_FOUND );

			else
			{
				$this->respond->setJSONResponse( 
							array(  'bid' => $badge->getBID(),
									'name' => $badge->getName(),
									'description' => $badge->getDescription(),
									'image' => Controller::formatURL( $badge->getImageSRC() ) ));

				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );

		}
		
	
	}

?>