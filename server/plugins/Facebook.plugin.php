<?php

	DEFINE( 'FACEBOOK_WINIT_ID', '104075696465886' );
	DEFINE( 'FACEBOOK_WINIT_SECRET', '46906989554c3ce179e8148c03b4d09b' );

	DEFINE( 'FACEBOOK_WINIT_ACCESS_CODE', FACEBOOK_WINIT_ID . '|' . FACEBOOK_WINIT_SECRET );

	DEFINE( 'FACEBOOK_CA_BUNDLE', dirname(__FILE__) . '/fb_ca_chain_bundle.crt' );




	class FacebookPlugin extends Plugin {

		const WINIT_ID = FACEBOOK_WINIT_ID ;
		const WINIT_SECRET = FACEBOOK_WINIT_SECRET ;

		const WINIT_ACCESS_CODE = FACEBOOK_WINIT_ACCESS_CODE ;


		private static $opts = array( CURLOPT_RETURNTRANSFER => true,
									  CURLOPT_BINARYTRANSFER => true,
									  CURLOPT_HEADER		 => false,
									  CURLOPT_SSL_VERIFYPEER => true,
									  CURLOPT_CAINFO		 => FACEBOOK_CA_BUNDLE,
									  CURLOPT_HTTPHEADER	 => array('Host: graph.facebook.com') );


		public static function verifyPayload($signature, $payload)
		{
			$hash = 'sha1='. hash_hmac('sha1', $payload, self::WINIT_SECRET );

			return $signature === $hash ;
		}


		public static function validadeUserToken($accessToken)
		{

			if( false !== ( $ch = curl_init() ) )
			{

				$url = "https://graph.facebook.com/me?fields=id&access_token=" . $accessToken;

				curl_setopt($ch, CURLOPT_URL, $url);
				curl_setopt_array( $ch, static::$opts );

				$return = curl_exec($ch);
				curl_close($ch);

				if( $return !== false )
				{
					if ( false !== ( $json = @json_decode( $return, true ) )
							&& isset( $json['id'] ) )
						return $json['id'] ;
				}

			}

			return false;
		}



		public static function getUsersInfo(array $uidsList)
		{

			if( false !== ( $ch = curl_init() ) )
			{
				$uidList = implode(",", $uidsList);
				
				$sql = "SELECT uid,name,birthday,email FROM user WHERE uid IN({$uidList}) ;";

				$params = http_build_query(
										array(
											'q' => $sql,
											'access_token' => self::WINIT_ACCESS_CODE
										)
										, null, '&' );

				$url = "https://graph.facebook.com/fql?{$params}" ;

				curl_setopt($ch, CURLOPT_URL, $url);
				curl_setopt_array( $ch, static::$opts );

				$return = curl_exec($ch);
				var_dump($return);
				curl_close($ch);

				if( $return !== false )
				{
					//TODO: php:/input

				}

			}

			return false;
		}



	}

?>