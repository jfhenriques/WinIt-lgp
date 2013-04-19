<?php


	DEFINE( 'R_ADDR_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_ADDR_ERR_ADDR_NOT_FOUND'		, 0x11 );



	class AddressController extends Controller {

		private static $status = array(
				R_ADDR_ERR_PARAM				=> 'CP4 e CP3 têm de ser definidos',
				R_ADDR_ERR_ADDR_NOT_FOUND		=> 'Código postal não encontrado',
			);

	

		public function __configure()
		{
			$this->requireAuth();
		}
		
		
		public function list_cp()
		{
			$render_code = null;
			$resp = array();
			
			$cp4 = valid_request( 'cp4' );
			$cp3 = valid_request( 'cp3' );

			$hint = valid_request( 'hint' );

			if( is_null( $cp4 ) || is_null( $cp3 ) )
				$render_code = R_ADDR_ERR_PARAM;
			
			else
			{
				$streets = Address::findByCP( $cp4, $cp3, $hint );

				if( is_null( $streets ) || !is_array( $streets ) || count( $streets ) == 0 )
					$render_code = R_ADDR_ERR_ADDR_NOT_FOUND;
				
				else
				{
					foreach( $streets as $str )
						$resp[] = array(
										'adid' => $str->getID(),

										'street' => $str->getStreet(),

										'locality' => $str->getLocality(),
										'district' => $str->getDistrict(),
									);

					$render_code = R_STATUS_OK;
				}
			}
			
			$this->respond->renderJSON( $resp, $render_code, describeMessage( $render_code, static::$status ) );

		}
	
	}
?>