<?php

	class TestController extends Controller {


		public function test()
		{

			//GCMPlugin::send( array('APA91bFN5scyCp9K8TMJE_6eJ4X1Rmox2QzDTr3_U4RQAW1sOr1VYjsPR8awRbDeMAuYTAxUlJMC81ifDEP7rt50RV1c5eHWZgkZK8BxFQV9zURccer-q4CcZwub6j7Qos2fsMCbLWph332vRP6cJ9r3FXGxHTL_Pw'), array( 'data' => 'merdinhas' ) );

			$sig = 'sha1=ce9827cce81a40db0f27ac93c111feffe6de7730';
			$payload = '{"object":"user","entry":[{"uid":"100005987078533","id":"100005987078533","time":1369811224,"changed_fields":["first_name"]}]}';
			

			var_dump( FacebookPlugin::verifyPayload( $sig, $payload ) );


		}


	}
