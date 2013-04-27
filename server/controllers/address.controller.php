<?php


	DEFINE( 'R_ADDR_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_ADDR_ERR_ADDR_NOT_FOUND'		, 0x11 );



	class AddressController extends Controller {

		private static $status = array(
				R_ADDR_ERR_PARAM				=> 'CP4 e CP3 têm de ser definidos',
				R_ADDR_ERR_ADDR_NOT_FOUND		=> 'Código postal não encontrado',
			);

	

		protected function __configure()
		{
			//$this->requireAuth();
		}
		
		
		public function list_cp()
		{
			
			$cp4 = valid_request_var( 'cp4' );
			$cp3 = valid_request_var( 'cp3' );

			$hint = valid_request_var( 'hint' );

			if( is_null( $cp4 ) || is_null( $cp3 ) )
				$this->respond->setJSONCode( R_ADDR_ERR_PARAM );
			
			else
			{
				$streets = Address::findByCP( $cp4, $cp3, $hint );

				if( is_null( $streets ) || !is_array( $streets ) || count( $streets ) == 0 )
					$this->respond->setJSONCode( R_ADDR_ERR_ADDR_NOT_FOUND );
				
				else
				{
					$resp = array();

					foreach( $streets as $str )
					{
						$resp[] = array(
										'adid' => $str->getADID(),

										'address' => $str->getStreet(),

										'locality' => $str->getLocality(),
										'district' => $str->getDistrict(),
									);
					}

					$this->respond->setJSONResponse( $resp );
					$this->respond->setJSONCode( R_STATUS_OK );
				}
			}
			
			$this->respond->renderJSON( static::$status );

		}
	
	}
?>