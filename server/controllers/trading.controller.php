<?php


	DEFINE( 'R_SESS_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_SESS_ERR_USER_NOT_FOUND'		, 0x11 );

	DEFINE( 'R_SESS_ERR_SESSION_NOT_FOUND'	, 0x20 );



	class TradingController extends Controller {

		private static $status = array(
				R_SESS_ERR_PARAM				=> 'Utilizador e/ou password não especificado',
				R_SESS_ERR_USER_NOT_FOUND		=> 'Utilizador e/ou password não encontrado',

				R_SESS_ERR_SESSION_NOT_FOUND	=> 'Sessão não encontrado',
			);

	

		protected function __configure()
		{
			//$this->checkAuth();
		}




		public function index()
		{

			$pc = new PrizeCode();
			$pc->setEmissionDate(time());
			$pc->genValidCode("12345678901234567890123456789012");
			$pc->setUPID(1);
			$pc->setOwnerUID(1);
			$pc->save();



		}


	}


?>