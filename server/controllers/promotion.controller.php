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
		
		/*public function index() {
		
			*//*
			 * Certeficar-se que alguém está logado
			 *//*
			this->requireAuth();
		
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
		}*/
		
		public function show() {
			$render_code = null;
			
			$resp = Promotion::getPromotion();
		
			$render_code = R_STATUS_OK ;	

			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );			
		}
	
	}

?>