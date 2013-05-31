<?php

	DEFINE('GCM_API_KEY', 'AIzaSyBGkNrEiiIcaEoXI5u3PFM9Ak5xCrGmImQ');
	DEFINE('GCM_API_KEY_AUTH', 'Authorization: key=' . GCM_API_KEY);

	DEFINE('GCM_API_URL', 'https://android.googleapis.com/gcm/send');

	DEFINE( 'GCM_CA_BUNDLE', dirname(__FILE__) . '/fb_ca_chain_bundle.crt' );



	class GCMPlugin extends Plugin {
	

		const API_KEY = 'AIzaSyCB0FuuCiPYK-aLZ2gdohLTQ8nhhLv5ToM';


		private static $opts = array( CURLOPT_URL			 => GCM_API_URL,
									  CURLOPT_HEADER		 => false,
									  CURLOPT_SSL_VERIFYHOST => true,
									  CURLOPT_SSL_VERIFYPEER => true,
									  CURLOPT_POST			 => true,
									  CURLOPT_CAINFO		 => GCM_CA_BUNDLE,
									  CURLOPT_HTTPHEADER	 => array( 'Host: android.googleapis.com',
									  								   'Content-Type: application/json',
									  								   GCM_API_KEY_AUTH ),
									  CURLOPT_RETURNTRANSFER => true,
									  CURLOPT_BINARYTRANSFER => true
									  );


		
		public static function send(array $registrationIDs, array $values)
		{

			if( false !== ( $ch = curl_init() ) )
			{

				$fields = array( 'registration_ids'	=> $registrationIDs,
				                 'data'				=> $values 			 );

				curl_setopt_array( $ch, static::$opts );
				curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $fields ) );


				$return = @curl_exec($ch);
				curl_close($ch);

				if( $return !== false )
					return true;

			}

			return false;
		}
		

		
	
	}


?>