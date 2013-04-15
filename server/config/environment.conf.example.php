<?php

	/*
	 *	O URI onde se for encontrar o website.
	 *	Se tiver na raiz do domínio deve ficar em branco
	 */
	DEFINE('INCLUSION_PATH_SIMPLE', '');
	
	
	/* 
	 *	Deve sempre ficar em 'false' quando lançado em produção
	 */
	DEFINE('DEVELOPMENT_ENVIRONMENT', false);
	
	
	/*
	 *	Configurações do pdo para a base de dados
	 */
	DEFINE('PDO_DATABASE', 'mysql:host=localhost;dbname=;charset=UTF-8' );
	DEFINE('PDO_USERNAME', '');
	DEFINE('PDO_PASSWORD', '');
	
	
	/*
	 *	Configuração de qual tipo de cache a utilizar
	 *	para guardar informação.
	 *
	 *	!! Não alterar nem o snomes nem os valores directamente abaixo !!
	 */
	DEFINE( 'COMMON_CACHE_DISABLED'	, 0 );	/* Não deve ser usado */
	DEFINE( 'COMMON_CACHE_AUTO'		, 1 );	/* Escolha automática entre APC e Memcache(d) */
	DEFINE( 'COMMON_CACHE_APC'		, 2 );	/* Força PHP APC */
	DEFINE( 'COMMON_CACHE_MEMCACHED', 3 );	/* Força Memcache(d) */
	
	/*
	 *	Tipo de cache a utilizar
	 *
	 *	Nunca usar COMMON_CACHE_DISABLED ou trará graves problemas de performance
	 *	Quando escolhido COMMON_CACHE_AUTO, deve estar garantida a instalação de pelo menos o APC ou de Memcache(d).
	 *	A ordem de verificação neste modo é de primeiro pelo memcache(d) e depois pelo APC
	 *
	 *	NOTA: AINDA EXISTE UM BUG POR RESOLVER NO APC AO GUARDAR ARRAYS, NÃO USAR!
	 */
	DEFINE( 'COMMON_CACHE_SET_MODE'	, COMMON_CACHE_MEMCACHED );
	
	
	/*
	 *	Configurações do servidor de memcache(d)
	 */
	DEFINE( 'MEMCACHED_SERVER_ADDR'	,	'127.0.0.1' );
	DEFINE( 'MEMCACHED_SERVER_PORT'	,	11211 );
	
	
	/*
	 *	Força o flush do memcache(d) ou do apc quando 'true'
	 */
	DEFINE( 'COMMON_CACHE_FORCE_FLUSH'	,	false );
	 
	DEFINE( 'COMMON_CACHE_VAR_PREFIX'	,	'cc_' );
	 
	 
	 
	 
	/*
	 * !! Não alterar nada daqui para nada !!
	 */
	DEFINE("INCLUSION_PATH", INCLUSION_PATH_SIMPLE . '/');

?>