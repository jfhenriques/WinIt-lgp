<?php

	DEFINE( 'R_PROM_ERR_PARAMS'			, 0x10 );

	DEFINE( 'R_PROM_ERR_USER_NOT_FOUND'	, 0x20 );

	DEFINE( 'R_PROM_CANNOT_REDEEM'		, 0x30 );
	



	class PromotionController extends Controller {
	
		private static $status = array(
			R_PROM_ERR_PARAMS			=> 'Parâmetros não definidos',
			R_PROM_ERR_USER_NOT_FOUND	=> 'Promoção não encontrada',

			R_PROM_CANNOT_REDEEM		=> 'Impossível utilizar a promoção selecionada',
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

			$uid = AuthenticatorPlugin::getInstance()->getUID();
			$promos = null;

			if( $uid <= 0 || is_null( $promos = Promotion::findValidPromotions( $uid ) ) || !is_array( $promos ) )
				$this->respond->setJSONCode( R_PROM_ERR_USER_NOT_FOUND );

			else
			{
				$resp = array();

				foreach( $promos as $p )
				{
					$active = $p->getActiveUPID();

					$resp[] = array( 'pid' => $p->getPID(),
									 'name' => $p->getName(),
									 'active_upid' => ( $active <= 0 ) ? null : $active ,
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

		}




		public function naive_redeem()
		{
			$pcid = (int)valid_request_var( 'prizecode' );
		

			$uid = AuthenticatorPlugin::getInstance()->getUID();
			$time = time();

			if( $uid <= 0 || $pcid <= 0 )
				$this->respond->setJSONCode( R_PROM_ERR_PARAMS );

			else
			{
				$unused = null;

				if(    !TradingController::only_one_arr( PrizeCode::findOwnUnused( $uid, $time, $pcid ) , $unused )
					|| is_null( $unused ) )
					$this->respond->setJSONCode( R_PROM_CANNOT_REDEEM );

				else
				{

					$unused->setUtilizationDate( $time );
					$unused->setTrading(false);

					$this->respond->setJSONCode( $unused->save() ? R_STATUS_OK : R_GLOB_ERR_SAVE_UNABLE );

				}
			}

			$this->respond->renderJSON( static::$status );
		}
		
	}
