<?php

	DEFINE( 'R_BADGE_ERR_PARAMS'			, 0x10 );
	DEFINE( 'R_BADGE_ERR_USER_NOT_FOUND'	, 0x20 );

	
	DEFINE( 'BADGE_IMG_SRC_DIR', 'img/9cc030d4cccab8c52273613ef010120eb9e3228c/' );



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
							array(  'pid' => $badge->getBID(),
									'name' => $badge->getName(),
									'description' => $badge->getDescription(),
									'image' => Controller::formatURL( $badge->getImage() ) ));

				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );

			// $this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}
		
	
	}

?>