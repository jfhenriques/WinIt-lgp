<?php

	
	DEFINE('MEASURE_TIME', true);
	$st = 0;
	
	if( MEASURE_TIME ) $st = microtime(true);
	/*define('INCLUSION_PATH', '/billsystem/');
	require('../views/header.html.php');
	require('../views/home/home.html.php');
	require('../views/footer.html.php');*/
	
	require('../bootstrap.php');

	if( MEASURE_TIME )
	{
		$end = 1000 * (microtime(true) - $st);	
		print("\n\n<br>Time: $end ms\n");
	}
?>
