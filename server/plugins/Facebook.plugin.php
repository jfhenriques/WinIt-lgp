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

		private static $appsecret_proof = null;


		public static function verifyPayload($signature, $payload)
		{
			$hash = 'sha1='. hash_hmac('sha1', $payload, self::WINIT_SECRET );

			return $signature === $hash ;
		}

		public static function getAppSecretProof($accessToken)
		{
			return hash_hmac('sha256', $accessToken, self::WINIT_SECRET);
		}

		private static function _buil_query($array)
		{
			return http_build_query( $array , null, '&' );
		}


		public static function fetchInfoByUserAccessToken($accessToken)
		{

			if( false !== ( $ch = curl_init() ) )
			{
				$params = self::_buil_query( array( 'fields' => 'id,installed,birthday,email,name',
													'access_token' => $accessToken,
													//'appsecret_proof' => FacebookPlugin::getAppSecretProof($accessToken)
													) );


				$url = "https://graph.facebook.com/me?{$params}";

				curl_setopt($ch, CURLOPT_URL, $url);
				curl_setopt_array( $ch, static::$opts );

				$return = curl_exec($ch);
				curl_close($ch);

				if( $return !== false )
				{
					if ( false !== ( $json = @json_decode( $return, true ) ) )
						return $json;
				}

			}

			return false;
		}



		public static function getUsersInfo(array $uidsList)
		{

			if( false !== ( $ch = curl_init() ) && count($uidsList) > 0 )
			{
				$_uidList = implode(",", $uidsList);

				$sql = "SELECT uid,name,birthday_date,email FROM user WHERE uid IN({$_uidList}) ;";

				$params = self::_buil_query( array( 'q' => $sql,
													'access_token' => self::WINIT_ACCESS_CODE,
													//'appsecret_proof' => FacebookPlugin::getAppSecretProof(self::WINIT_ACCESS_CODE)
													) );

				$url = "https://graph.facebook.com/fql?{$params}" ;

				curl_setopt($ch, CURLOPT_URL, $url);
				curl_setopt_array( $ch, static::$opts );

				$return = curl_exec($ch);
				curl_close($ch);

				var_dump( $return );

				return $return;
			}

			return false;
		}



	}

?>