<?php

	/* 
	 *	Must always be set to false on prodution environments
	 */
	DEFINE('DEVELOPMENT_ENVIRONMENT', false);
	
	
	/*
	 *	PDO configuration
	 */
	DEFINE('PDO_DATABASE', 'mysql:host=localhost;dbname=!!DB_NAME_HERE!!;charset=UTF-8' );
	DEFINE('PDO_USERNAME', '!!DB_USER_HERE!!');
	DEFINE('PDO_PASSWORD', '!!DB_PASS_HERE!!');
	
	
	/*
	 *	Description of the types of cache available
	 *
	 *	!! Do not edit this values !!
	 */
	DEFINE( 'COMMON_CACHE_DISABLED'	, 0 );	/* Should never be used */
	DEFINE( 'COMMON_CACHE_AUTO'		, 1 );	/* Automatic choose between APC e Memcache(d) */
	DEFINE( 'COMMON_CACHE_APC'		, 2 );	/* Force PHP APC */
	DEFINE( 'COMMON_CACHE_MEMCACHED', 3 );	/* Force Memcache(d) */
	
	/*
	 *	Which type of cache in use
	 *
	 *	Do not ever use COMMON_CACHE_DISABLED or you will suffer from very bad performance.
	 *
	 *	COMMON_CACHE_AUTO will search first for memcache(d) and then for APC
	 *
	 *	NOTE: there is a bug in APC, please do not use!
	 */
	DEFINE( 'COMMON_CACHE_SET_MODE'	, COMMON_CACHE_MEMCACHED );
	
	
	/*
	 *	Memcache(d) server configuration
	 */
	DEFINE( 'MEMCACHED_SERVER_ADDR'	,	'127.0.0.1' );
	DEFINE( 'MEMCACHED_SERVER_PORT'	,	11211 );
	
	
	/*
	 *	Forces flush of memcache(d) or APC. Usefull for test purposes
	 */
	DEFINE( 'COMMON_CACHE_FORCE_FLUSH'	,	false );

	
	/*
	 *	This variable should be set to something unique, so that
	 * 	cached variables/arrays/etc can be distinguished from other applications
	 * 	who are using the cache
	 */
	 
	DEFINE( 'COMMON_CACHE_VAR_PREFIX'	,	'cc_' );
	
	
	/*
	 *	Validity of authentication tokens until they die
	 */
	DEFINE( 'TOKEN_VALIDITY', 604800 ); // 3600*24*7


	/*
	 *	E-mail from address, used by Controller::sendCustomMail(...)
	 */
	DEFINE( 'MAIL_FROM_ADDRESS', 'noreply@localhost' );


	/*
	 *	Leave empy if the website is in the root (namespaces don't count)
	 */
	DEFINE( 'BASE_URI', '' );


	/*
	 *	If there is an alternative path to access static content, set this to true
	 *
	 *		If the framework is being used as an API (for ex: Android),
	 *		you must set this to true and define BASE_STATIC_URI
	 *		or yon't be able to access content like images.
	 */
	DEFINE( 'USE_STATIC_URI', true );


	/*
	 *	The base uri or schema+host+uri to access the content
	 */
	DEFINE( 'BASE_STATIC_URI', "http://{$_SERVER['SERVER_NAME']}/" . BASE_URI . ( strlen(BASE_URI) > 0 ?  ';' : '' ) );

