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
		
			$render_code = null ;
			
			$resp = array();
			
			$resp = array(
				'promocaoid' => getID(),
				'nome' => getNome(),
				'data_inicio' => getData_inicio(),
				'data_fim' => getData_fim(),
				'max_utilizacoes' => getMax_utilizacoes(),
				'coord_validas' => getCoord_validas(),
				'coord_raio' => getCoord_raio(),
				'transmissivel' => getTransmissivel(),
				'activa' => getActiva(),
				'pontos_ao_ganhar' => getPontos_ao_ganhar(),
				'retalhistaid' => getRetalhistaid(),
				'tipopromocaoid' => getTipopromocaoid(),
			);
			$render_code = R_STATUS_OK ;	

			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );			
		}
		
		/*public function show() {
		}*/
	
	}

?>