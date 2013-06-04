<?php

	//session_start();

	$init_time = microtime(true);
	
	// Força o output a ser redirecionado para o buffer,
	// e sendo enviado para o browser do utulizador apenas no final.
	ob_start();
	
	//header('Content-type: text/html; charset=utf-8', true);
		
	DEFINE('ROOT',  dirname(__FILE__));
	
	if( defined('ENVIRONMENT_CONFIG') )
		require_once( ENVIRONMENT_CONFIG );
	else
		require_once(ROOT .'/config/environment.conf.php');
	
	require_once(ROOT .'/helpers/commons.inc.php');
	
	include_once(ROOT .'/helpers/router.inc.php');
	include_once(ROOT .'/helpers/template.inc.php');
	include_once(ROOT .'/helpers/model.inc.php');
	
	include_once(ROOT .'/helpers/controller.inc.php');
	
	include_once(ROOT .'/helpers/plugin.inc.php');
	//include_once(ROOT .'/helpers/authentication.inc.php');

	Plugin::loadPlugins();

	$router = Router::getInstance();
	$router->route();
