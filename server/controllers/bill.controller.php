<?php

	require_once("funcs/funcs.inc.php");
	require_once("funcs/m.inc.php");
	
	
	$router = Router::getInstance();
	$router->addBreadcrumbPath("bill", "Billing");
	
	switch( $router->getControllerBlock() )
	{
		case "new":
		
			$router->addBreadcrumbPath("new", "New");	
		
		
			$router->setInterfacePage( "new.view.php" );
		
			break;
			
		case PAGE_INDEX:
		
			$router->setInterfacePage( "main.view.php" );
			
			break;
		default:
			$router->setPageFound(false);
			break;
	
	}
	
	
	$router->includeInterface();

?>