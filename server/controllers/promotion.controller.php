<?php

	DEFINE( 'R_PROM_ERR_PARAMS'			, 0x10 );

	DEFINE( 'R_PROM_ERR_USER_NOT_FOUND'	, 0x20 );



	class PromotionController extends Controller {
	
		private static $status = array(
			R_PROM_ERR_PARAMS			=> 'Parâmetros não definidos',
			R_PROM_ERR_USER_NOT_FOUND	=> 'Promoção não encontrada',
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

			$authUID = (int)Authenticator::getInstance()->getUID();
			$promos = null;

			if( $authUID <= 0 || is_null( $promos = Promotion::findValidPromotions( $authUID ) ) || !is_array( $promos ) )
				$this->respond->setJSONCode( R_PROM_ERR_USER_NOT_FOUND );

			else
			{
				$resp = array();

				foreach( $promos as $p )
				{
					$resp[] = array( 'pid' => $p->getPID(),
									 'name' => $p->getName(),
									 'image' => Controller::formatURL( $p->getImageSRC() ) );
				}
				$this->respond->setJSONResponse( $resp );
				$this->respond->setJSONCode( R_STATUS_OK );
			}
			
			$this->respond->renderJSON( static::$status );

		}
		
		public function show()
		{
			
			$pid = (int)valid_request_var('promotion');
			$promo = null;

			if( $pid <= 0 )
				$this->respond->setJSONCode( R_PROM_ERR_PARAMS );

			elseif( is_null( $promo = Promotion::findByPID($pid) ) )
				$this->respond->setJSONCode( R_PROM_ERR_USER_NOT_FOUND );

			else
			{
				$items = array();
				foreach($promo->getItems() as $i)
				{
					$items[] = array( 'iid' => $i->getIID(),
									  'name' => $i->getName(),
									  'img_src' => Controller::formatURL( $i->getImageSRC() ),
									  'percent' => $i->getPercent() );
				}

				$this->respond->setJSONResponse( 
							array(  'pid' => $promo->getPID(),
									'name' => $promo->getName(),
									'desc' => $promo->getDescription(),
									'image' => Controller::formatURL( $promo->getImageSRC() ),
									'active' => $promo->isActive(),
									'init_date' => $promo->getInitDate(), 
									'end_date' => $promo->getEndDate(),
									'max_util_date' => $promo->getMaxUtilizationDate(),
									'user_limit' => $promo->getUserLimit(),
									'valid_coord' => $promo->getValidCoord(),
									'valid_coord_radius' => $promo->getValidCoordRadius(),
									'transferable' => $promo->isTransferable(),
									'win_points' => $promo->getWinPoints(),
									'rid' => $promo->getRID(),
									'ret_name' => $promo->getRetailerName(),
									'ret_img' => Controller::formatURL( $promo->getRetailerImageSRC() ),
									'ptid' => $promo->getPTID(),
									'promo_type' => $promo->getPromotionType(),
									'items' => $items ) );

				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );

			// $this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}
	
	}

?>