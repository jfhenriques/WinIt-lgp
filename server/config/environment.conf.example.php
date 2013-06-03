<?php

	/* 
	 *	Must always be set to false on prodution environment
	 */
	DEFINE('DEVELOPMENT_ENVIRONMENT', false);
	
	
	/*
	 *	PDO configuration
	 */
	DEFINE('PDO_DATABASE', 'mysql:host=localhost;dbname=DB_NAME_HERE;charset=UTF-8' );
	DEFINE('PDO_USERNAME', 'DB_USER_HERE');
	DEFINE('PDO_PASSWORD', 'DB_PASS_HERE');
	
	
	/*
	 *	Description of the types of cache available
	 *
	 *	!! Do not edit this values !!
	 */
	DEFINE( 'COMMON_CACHE_DISABLED'	, 0 );	/* It should never be used */
	DEFINE( 'COMMON_CACHE_AUTO'		, 1 );	/* Automatic choose between APC e Memcache(d) */
	DEFINE( 'COMMON_CACHE_APC'		, 2 );	/* Force PHP APC */
	DEFINE( 'COMMON_CACHE_MEMCACHED', 3 );	/* Force Memcache(d) */
	
	/*
	 *	Which type of cache in use
	 *
	 *	Do not ever use COMMON_CACHE_DISABLED, some things may break,
	 *	and there would be so much overhead in every call to the server
	 *	that the performance would be really bad
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
	 *	Forces flush of memcache(d) or APC. This is usefull for test purposes
	 */
	DEFINE( 'COMMON_CACHE_FORCE_FLUSH'	,	false );

	
	/*
	 *	This variable could be used when is needed some prefix to distinguish
	 *	memcached variable names from other common names
	 */
	 
	DEFINE( 'COMMON_CACHE_VAR_PREFIX'	,	'cc_' );
	
	
	/*
	 *	Validity of tokens until they die
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
	 *	If there is an alternative path to access static content, se this to true
	 */
	DEFINE( 'USE_STATIC_URI', false );


	/*
	 *	The base uri or schema+host+uri to access the static content
	 */
	DEFINE( 'BASE_STATIC_URI', '' );

