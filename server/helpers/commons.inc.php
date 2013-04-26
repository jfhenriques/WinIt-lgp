<?php

	/**********************************************************************************
	 *	Error treatment
	 **********************************************************************************/
	 
	error_reporting( E_ALL | E_STRICT );
	
	date_default_timezone_set('Europe/Lisbon');
	

	ini_set('display_errors', DEVELOPMENT_ENVIRONMENT !== false);


	function my_exception_handler($exception)
	{
		require_once( ROOT .'/helpers/print_error.inc.php' );
		
		error_log($exception);
		dumpException( $exception );
		
		exit(1);
	}
	set_exception_handler("my_exception_handler");
	
	set_error_handler(function($errno, $errstr, $errfile, $errline ) {
		if ( (ini_get('error_reporting') & $errno) !== 0 )
		{
			$exc = new ErrorException($errstr, $errno, 0, $errfile, $errline);
			my_exception_handler( $exc );
		}
	});
	
	register_shutdown_function(function() {
		$lErr = error_get_last();
		
		if ( !is_null( $lErr ) && ( $lErr['type'] & ( E_ERROR | E_USER_ERROR | E_PARSE ) ) !== 0 )
		{
			$exc = new ErrorException($lErr['message'], $lErr['type'], 0, $lErr['file'], $lErr['line']);
			my_exception_handler( $exc );
		}	
	});
	
	ini_set('log_errors', 1);
	ini_set('ignore_repeated_errors', 1);
	ini_set('error_log', '../tmp/error.log.txt' );
	
	
	/**********************************************************************************
	 *	Common memcached
	 **********************************************************************************/	
	
	class CommonCache {
		
		static private $instance = null;
		private $TYPE = COMMON_CACHE_SET_MODE;
		
		private $mc = null;
		
		private function __clone() { }
		private function __construct()
		{
			$apc_avail   = extension_loaded('apc') && ini_get('apc.enabled') ;
			$memc_avail  = class_exists("Memcache", false) ;
			$memcd_avail = class_exists("Memcached", false) ;
			
			if( COMMON_CACHE_SET_MODE === COMMON_CACHE_AUTO )
				$this->TYPE = ( $memcd_avail || $memc_avail ) ? COMMON_CACHE_MEMCACHED :
												( $apc_avail ? COMMON_CACHE_APC : COMMON_CACHE_DISABLED ) ;
			
			if( $this->TYPE === COMMON_CACHE_MEMCACHED )
			{
				if( $memcd_avail )
					$this->mc = new Memcached();
				elseif( $memc_avail  )
					$this->mc = new Memcache();
					
				if( is_null( $this->mc ) )
				{
					$this->TYPE = COMMON_CACHE_DISABLED ;
					
					throw new Exception("No Memcache(d) found in your php installation");
				}
				
				$this->mc->addServer(MEMCACHED_SERVER_ADDR, MEMCACHED_SERVER_PORT);
			}
			elseif( $this->TYPE === COMMON_CACHE_APC )
			{
				if( !$apc_avail )
					$this->TYPE = COMMON_CACHE_DISABLED ;
			}
			
			if( COMMON_CACHE_FORCE_FLUSH )
			{
				switch( $this->TYPE )
				{
					case COMMON_CACHE_APC:
						apc_clear_cache();
						break;
						
					case COMMON_CACHE_MEMCACHED:
						$this->mc->flush();
						break;
				}
			}
			
			
		}
		
		public static function getInstance()
		{
			if( is_null( static::$instance ) )
				static::$instance = new CommonCache();
				
			return static::$instance;
		}

		public static function buildVarName($var_suff, $var)
		{
			return COMMON_CACHE_VAR_PREFIX . ".${var_suff}.${var}";
		}
		
		public function getMemcached()
		{
			return $this->mc;
		}
		
		public function get($var)
		{
			switch( $this->TYPE )
			{
				case COMMON_CACHE_APC:
					return unserialize( apc_fetch( $var ) );
					
				case COMMON_CACHE_MEMCACHED:
					return $this->mc->get( $var ) ;
					
				default:
					return false;
			}
		}
		
		public function delete($var)
		{
			switch( $this->TYPE )
			{
				case COMMON_CACHE_APC:
					return apc_delete( $var );
					
				case COMMON_CACHE_MEMCACHED:
					return $this->mc->delete( $var ) ;
					
				default:
					return false;
			}
		}
		
		/*public function get($var)
		{
			$st = microtime( true );
			$v = $this->_get($var);
			print( 'Time: ' . (1000*(microtime(true)-$st)) . " ms\n<br>" );
			return $v;
		}*/
		public function set($var, $val)
		{
			switch( $this->TYPE )
			{
				case COMMON_CACHE_APC:
					return apc_store( $var, serialize( $val ) );
					
				case COMMON_CACHE_MEMCACHED:
					return $this->mc->set($var, $val) ;
					
				default:
					return false;
			}
		}
	
	}
	
	/**********************************************************************************
	 *	Class autoloader
	 **********************************************************************************/
	
	class AutoLoader {
		
		static private $instance = null;
		const NAMES_KEY = "500593615.names";
		
		private $cachedNames = array();
		private $cc = null;
		
		private $class_search_path = array( 'models' => array('name' => 'model', 'incName' => false ),
											'controllers' => array('name' => 'controller', 'incName' => true ) );
		
		private function __clone() { }
		private function __construct()
		{
			$this->cc = CommonCache::getInstance();

			$this->cachedNames = $this->cc->get( self::NAMES_KEY );
			
			if( $this->cachedNames === false || !is_array( $this->cachedNames ) )
			{
				$this->cachedNames = $this->getList();
				$this->saveMemcacheArray();
			}
		}
		
		public static function getInstance()
		{
			if( is_null( static::$instance ) )
				static::$instance = new AutoLoader();
				
			return static::$instance;
		}
		
		private function saveMemcacheArray()
		{
			return $this->cc->set( self::NAMES_KEY , $this->cachedNames );
		}
		private function getList()
		{
			$output = array();
			
			foreach( $this->class_search_path as $k => $v )
			{
			
				if( is_null( $k ) || is_null( $v ) || strlen( $k ) == 0 
						|| !is_array( $v ) || !isset( $v['name'] ) || !isset( $v['incName'] ) )
					continue;
				
				$dir = ROOT . '/' . $k . '/' ;
				
				$k = strtolower( $k );
				$name = strtolower( $v['name'] );
				$classType = ucfirst( $name );
				
				if( $handle = @opendir( $dir ) )
				{
					while ( false !== ($entry = readdir($handle)) )
					{
						if ( $entry[0] != '.' )
						{
							$exp = explode('.', $entry, 3);
							
							if( count( $exp ) == 3 && strtolower($exp[1]) == $name )
							{
								$key = ucfirst( $exp[0] ) . ( $v['incName'] ? $classType : '' ) ;
								$file = $dir . $entry ;
								
								$output[$key] = $file;
							}	
						}
					}
					@closedir($handle);
				}
			}

			return $output;
		}
		
		public function loadClass($name)
		{
			$loaded = false;
			if( isset( $this->cachedNames[$name] ) )
			{
				$file = $this->cachedNames[$name] ;
				if( !is_readable( $file ) || !is_file( $file ) )
				{
					unset( $this->cachedNames[$name] );
					$this->saveMemcacheArray();
				}
				else
				{
					include_once( $file );
					
					$loaded = class_exists( $name ) ;
				}
			}
			
			if( !$loaded )
				throw new Exception("Class '$name' not found in autoload list!");
		}
	
	}
	
	spl_autoload_register(function($className) {
	
		$al = AutoLoader::getInstance();
		$al->loadClass( $className );
		
	});
	
?>