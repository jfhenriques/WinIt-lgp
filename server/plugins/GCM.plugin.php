<?php

	DEFINE('GCM_API_KEY', 'AIzaSyBGkNrEiiIcaEoXI5u3PFM9Ak5xCrGmImQ');
	DEFINE('GCM_API_KEY_AUTH', 'Authorization: key=' . GCM_API_KEY);

	DEFINE('GCM_API_URL', 'https://android.googleapis.com/gcm/send');

	DEFINE( 'GCM_CA_BUNDLE', dirname(__FILE__) . '/fb_ca_chain_bundle.crt' );



	class GCMPlugin extends Plugin {
	

		const API_KEY = 'AIzaSyCB0FuuCiPYK-aLZ2gdohLTQ8nhhLv5ToM';


		const STATE_UNKNOWN		= 0;
		const STATE_ACCEPT		= 1;
		const STATE_REJECT		= 2;
		const STATE_NEW_SUGGEST	= 3;


		private static $opts = array( CURLOPT_URL			 => GCM_API_URL,
									  CURLOPT_HEADER		 => false,
									  CURLOPT_SSL_VERIFYHOST => 2,
									  CURLOPT_SSL_VERIFYPEER => 1,
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

				return ( $return !== false );
			}

			return false;
		}


		public static function sendUser($uid, array $values)
		{
			if( !is_null( $uid ) && !is_null( $values) )
			{
				$gcms = null;

				if(    is_array( $gcms = UserGCM::findByUID( $uid ) )
					&& count( $gcms ) > 0 )
				{
					$codes = array();

					foreach( $gcms AS $g )
					{
						$token = null;

						if(    !is_null( $g )
							&& !is_null( $token = $g->getTokenGCM() )
							&& strlen( $token ) > 0 )
							$codes[] = $token;
					}

					if( count( $codes ) > 0 )
						return GCMPlugin::send( $codes, $values );

				}

			}

			return false;
		}


		private static function sendAcceptReject( $uid, $type, $pcid, $pid, $name, $image, $time = null )
		{
			$arr = array( 'type' => $type,
						  'time' => is_null( $time ) ? time() : $time ,
						  'uid' => $uid,
						  'pcid' => $pcid,
						  'pid' => $pid,
						  'name' => $name,
						  'image' => $image );

			return GCMPlugin::sendUser( $uid, $arr );
		}


		public static function sendAccept( $uid, $pcid, $pid, $name, $image, $time = null )
		{
			return GCMPlugin::sendAcceptReject( $uid, self::STATE_ACCEPT, $pcid, $pid, $name, $image, $time );
		}
		public static function sendReject( $uid, $pcid, $pid, $name, $image, $time = null )
		{
			return GCMPlugin::sendAcceptReject( $uid, self::STATE_REJECT, $pcid, $pid, $name, $image, $time );
		}

		public static function sendNewSuggestion( $uid, $pcid_my, $pid_my, $name_my, $image_my, $pcid_w, $pid_w, $name_w, $image_w, $time = null )
		{
			$arr = array( 'type' =>self::STATE_NEW_SUGGEST ,
						  'time' => is_null( $time ) ? time() : $time ,
						  'uid' => $uid,
						  'pcid_my' => $pcid_my,
						  'pid_my' => $pid_my,
						  'name_my' => $name_my,
						  'image_my' => $image_my,
						  'pcid_w' => $pcid_w,
						  'pid_w' => $pid_w,
						  'name_w' => $name_w,
						  'image_w' => $image_w );

			return GCMPlugin::sendUser( $uid, $arr );
		}
	
	}


?>