<?php

	class TestController extends Controller {


		public function test()
		{

			var_dump( GCMPlugin::send( array('APA91bHugamPoeOVXhnMPN8CMWccz2TWoS4n9_iJ1eK3zGLVD1Q-HV6NkxKpZU2S1lYOjMzuRA9nH7WEtt0lcj2KMKDFvpimszYEbWlG58ZfoUHE1MC66wz69ugJoIfR4LbujLUeBV3oJ1ddYJ61MmUwiwlJvPPTug' ),
										array('cenas' => 'estupidas')) );

			/*$sig = 'sha1=ce9827cce81a40db0f27ac93c111feffe6de7730';
			$payload = '{"object":"user","entry":[{"uid":"100005987078533","id":"100005987078533","time":1369811224,"changed_fields":["first_name"]}]}';
			

			var_dump( GCMPlugin::send( $sig, $payload ) );
*/

    	UserGCM::deleteAllByUID(1);

		/*	$time_start = microtime(true);

			$forceTransferable = false;
			$owned = true;
			$restrict = null;

			$key = 'cenas.estupidas';

			for($i = 0; $i < 1000000; $i++)
			{

				
				



				$cc = CommonCache::getInstance();

				$sql = $cc->get( $key );

				if( $sql === false )
				{
					$sql =  'SELECT pc.pcid AS pcid, pc.emiss_date AS emiss_date, pc.util_date AS util_date, ' .
							' pc.cur_uid AS cur_uid, pc.valid_code AS valid_code, pc.in_trading AS in_trading, ' .
							' pc.upid AS upid, up.uid AS o_uid, up.pid AS pid, p.util_date AS p_util_date, ' .
							' p.name AS p_name, p.image AS p_image, pc.transaction AS transaction ' .
							' FROM ' . 'cenas' .' AS pc ' .
							' INNER JOIN ' . UserPromotion::TABLE_NAME . ' AS up ON (up.upid = pc.upid) ' .
							' INNER JOIN ' . Promotion::TABLE_NAME . ' AS p ON (p.pid = up.pid)' .
							' WHERE ' . ( is_null( $forceTransferable ) ? '' : 'p.transferable = ? AND ' ) . ' p.active = 1 AND ( p.util_date = 0 OR p.util_date > ? ) ' .
							' AND pc.util_date = 0 AND pc.in_trading = ? AND pc.cur_uid ' . ( $owned ? '=' : '<>' ) . ' ? ' . ( is_null( $restrict ) ? '' : ' AND pc.pcid = ? ' ) . ';' ;

					$cc->set( $key, $sql );
				}

			}

			$time_end = microtime(true) - $time_start;

			echo "Took: {$time_end}";
*/

		}


	}
