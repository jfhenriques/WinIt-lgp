<?php

	DEFINE( 'R_SESS_ERR_USER_NOT_FOUND'	, 0x10 );

	DEFINE( 'R_SESS_ERR_PARAMS'			, 0x20 );
	DEFINE( 'R_SESS_ERR_EMAIL_EXISTS'	, 0x21 );

	class PromotionController extends Controller {
	
		private static $status = array(
			R_SESS_ERR_PARAMS		=> 'Parâmetros não definidos',
			R_SESS_ERR_EMAIL_EXISTS	=> 'Email já existente',
		);
	
		/*
		 * Ensure that the user is logged
		 */
		public function __configure()
		{
		}
		
		public function index() {
		
			/*
			 * Certeficar-se que alguém está logado
			 */
			// this->requireAuth();
		
			$render_code = null;
			
			$resp = array(
				'pid' => $this->getID(),
				'name' => $this->getName(),
				'init_date' => $this->getInit_date(),
				'end_date' => $this->getEnd_date(),
				'user_limit' => $this->getUser_limit(),
				'valid_coord' => $this->getValid_coord(),
				'valid_coord_radius' => $this->getValid_coord_radius(),
				'transferable' => $this->getTransferable(),
				'active' => $this->getActive(),
				'win_points' => $this->getWin_points(),
				'rid' => $this->getRid(),
				'ptid' => $this->getPtid(),
				'func_type' => $this->getFunc_type(),
				'grand_limit' => $this->getGrand_limit(),
			);
		
			$render_code = R_STATUS_OK ;	

			$this->respond->renderJSON( $resposta, $render_code, describeMessage( $render_code, static::$status ) );			
		}
		
		public function show() {
			$render_code = null;
			
			$pid = valid_request_var('promotion');
			
			$resp = Promotion::findById($pid);
			
			$promotionId = $resp->getID();
			$name = $resp->getName();
			$init_date = $resp->getInit_date();
			$end_date = $resp->getEnd_date();
			$user_limit = $resp->getUser_limit();
			$valid_coord = $resp->getValid_coord();
			$valid_coord_radius = $resp->getValid_coord_radius();
			$transferable = $resp->getTransferable();
			$active = $resp->getActive();
			$win_points = $resp->getWin_points();
			$rid = $resp->getRid();
			$ptid = $resp->getPtid();
			$func_type = $resp->getFunc_type();
			$grand_limit = $resp->getGrand_limit();
		
			$render_code = R_STATUS_OK;
					
			$this->respond->setJSONCode( $render_code );			
			$this->respond->setJSONResponse( array( 'pid' => $promotionId,
													'name' => $name,
													'active' => $active,
													'init_date' => $init_date,
													'end_date' => $end_date,
													'user_limit' => $user_limit,
													'valid_coord' => $valid_coord,
													'valid_coord_radius' => $valid_coord_radius,
													'transferable' => $transferable,
													'win_points' => $win_points,
													'rid' => $rid,
													'ptid' => $ptid,
													'func_type' => $func_type,
													'grand_limit' => $grand_limit
													) );
			$this->respond->renderJSON( static::$status );
			
			
			// $this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );
		}
	
	}

?>