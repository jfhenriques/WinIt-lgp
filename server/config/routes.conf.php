<?php

	$GLOBALS['routes'] = array(
		
		/*'resources' => array(
				':notes' => array( 'only' => array( 'edit', 'index', 'show' ) ),
				':user' => array(
					':account' => array(
						':bill' => array( 'only' => array( 'index' ) )
					),
					':bill' => null,
				)
		),*/
		
		'matches' => array(
		
			array( 'match' => '/home', 'controller' => 'home', 'via' => 'get', 'action' => 'index' ),
			
		),
	
	);

?>