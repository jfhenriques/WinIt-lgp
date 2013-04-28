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
			//$this->requireAuth();
		}
		
		public function index() {
		
			// /*
			//  * Certeficar-se que alguém está logado
			//  */
			// // 		
			// $render_code = null;
			
			// $resp = array(
			// 	'pid' => $this->getPID(),
			// 	'name' => $this->getName(),
			// 	'init_date' => $this->getInitDate(),
			// 	'end_date' => $this->getEndDate(),
			// 	'user_limit' => $this->getUserLimit(),
			// 	'valid_coord' => $this->getValidCoord(),
			// 	'valid_coord_radius' => $this->getValidCoordRadius(),
			// 	'transferable' => $this->getTransferable(),
			// 	'active' => $this->getActive(),
			// 	'win_points' => $this->getWinPoints(),
			// 	'rid' => $this->getRid(),
			// 	'ptid' => $this->getPtid(),
			// 	'func_type' => $this->getFuncType(),
			// 	'grand_limit' => $this->getGrandLimit(),
			// );
		
			// $render_code = R_STATUS_OK ;	

			// $this->respond->renderJSON( $resposta, $render_code, describeMessage( $render_code, static::$status ) );			
		}
		
		public function show()
		{
						
			// $resp = Promotion::findByPId($pid);
			
			// $promotionId = ;
			// $name = ;
			// $init_date = $resp->getInitDate();
			// $end_date = $resp->getEndDate();
			// $user_limit = $resp->getUserLimit();
			// $valid_coord = $resp->getValidCoord();
			// $valid_coord_radius = $resp->getValidCoordRadius();
			// $transferable = $resp->isTransferable();
			// $active = $resp->isActive();
			// $win_points = $resp->getWinPoints();
			// $rid = $resp->getRid();
			// $ptid = $resp->getPtid();
			// $func_type = $resp->getFuncType();
			// $grand_limit = $resp->getGrandLimit();

			
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
									//'active' => $promo->isActive(),
									'init_date' => $promo->getInitDate(),
									'end_date' => $promo->getEndDate(),
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