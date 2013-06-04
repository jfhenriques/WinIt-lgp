<?php

	class TestController extends Controller {


		public function test()
		{

/*			var_dump( GCMPlugin::send( array('APA91bFN5scyCp9K8TMJE_6eJ4X1Rmox2QzDTr3_U4RQAW1sOr1VYjsPR8awRbDeMAuYTAxUlJMC81ifDEP7rt50RV1c5eHWZgkZK8BxFQV9zURccer-q4CcZwub6j7Qos2fsMCbLWph332vRP6cJ9r3FXGxHTL_Pw', 'APA91bFN5scyCp9K8TMJE_6eJ4X1Rmox2QzDTr3_U4RQAW1sOr1VYjsPR8awRbDeMAuYTAxUlJMC81ifDEP7rt50RV1c5eHWZgkZK8BxFQV9zURccer-q4CcZwub6j7Qos2fsMCbLWph332vRP6cJ9r3FXGxHTL_Pw'), array( 'data' => 'merdinhas' ) ) );

			/*$sig = 'sha1=ce9827cce81a40db0f27ac93c111feffe6de7730';
			$payload = '{"object":"user","entry":[{"uid":"100005987078533","id":"100005987078533","time":1369811224,"changed_fields":["first_name"]}]}';
			

			var_dump( GCMPlugin::send( $sig, $payload ) );
*/

    

			$time_start = microtime(true);

			$forceTransferable = false;
			$owned = true;
			$restrict = null;

			$key = 'cenas.estupidas';

			for($i = 0; $i < 100000; $i++)
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


		}


	}
