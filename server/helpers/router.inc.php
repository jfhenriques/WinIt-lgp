<?php

	DEFINE('ROUTES_FILE', ROOT . '/config/routes.conf.php' );
	DEFINE('NOT_FOUND_PAGE', ROOT . '/public/404.html' );
	
	class Router {
	
		const CACHE_ROUTE_KEY = '142124025.cahed_routes';
		
		private static $instance = null;
		
		private $url = null;
		private $cachedRoutes = null;
		private $exception = null;
		private $controller = null;
		private $controllerAction = null;
		private $controllerFound = false;
		private $mc = null;
	
		private function __clone() { }
		
		private function __construct()
		{
			$this->url = ( isset( $_REQUEST['z_url' ] ) && strlen( $_REQUEST['z_url' ] ) > 0 ) ? $_REQUEST['z_url' ] : "" ;
			
			$cc = CommonCache::getInstance();

			$cache = $cc->get( self::CACHE_ROUTE_KEY );
			
			$lastMod = @filemtime(ROUTES_FILE) ;
			
			if( $cache === false || !is_array( $cache ) ||
				!isset( $cache['version'] ) || $cache['version'] !== $lastMod )
			{
				include_once(ROUTES_FILE);
				
				$cache = array();
				
				$this->buildRoutes( $GLOBALS['routes'] , $lastMod !== false ? $lastMod : 0 , $cache);

				$cc->set( self::CACHE_ROUTE_KEY, $cache );
			}

			$this->cachedRoutes = $cache ;
 
		}
		
		public static function getInstance()
		{
			if( is_null( static::$instance ) )
				static::$instance = new Router();
				
			return static::$instance;
		}
		
		private static function getNext(&$i, $arr)
		{
			for( ; $i < count( $arr ); $i++)
			{
				if( strlen( $arr[$i] ) > 0 )
					return $arr[$i++];
			}
			
			return false;
		}
		
		public static function fix_key_is_array( $key, &$arr )
		{
			if( is_array( $arr ) && !is_null( $key ) )
			{
				if( !isset( $arr[$key] ) || !is_array( $arr[$key] ) )
					$arr[$key] = array();
			}
		}
		
		private static function hasNext($i, $arr)
		{
			for( ; $i < count( $arr ); $i++)
			{
				if( strlen( $arr[$i] ) > 0 )
					return $i;
			}
			
			return false;
		}
		
		public function foundController()
		{
			return $this->controllerFound ;
		}
		
		private function buildRoutes($routes, $version, &$cached)
		{
			
			function checkInArray($v, $arr)
			{
				return is_null($arr) || in_array( $v, $arr ) ;
			}
			function verifyName( $name )
			{
				return str_replace( array('#', ':'), array('', ''), $name );
			}
			
			function insertMethod( $controller, $name, $method, $action, &$output )
			{
				if( strlen( $name ) == 0 || !is_array($output) )
					return;
					
				$key = '#' . strtoupper( $method ) ;

				Router::fix_key_is_array( $name, $output );
				Router::fix_key_is_array( $key, $output[$name] );
			
				$output[$name][$key] = array( 'c' => strtolower( $controller ),
											  'a' => $action 		);
			}
			
			function processAtom( $arr, $isResource, $controller, $name, &$output )
			{
				if( !is_array( $arr ) )
					$arr = array();			
				
				$controller = verifyName( isset( $arr['controller'] ) ? $arr['controller'] : $controller ) ;
				
				if( $isResource )
				{
					$id = ":${name}";
								
					$only = ( isset( $arr['only'] ) && count( $arr['only'] ) > 0 ) ? $arr['only'] : null ;
					
					Router::fix_key_is_array( $name, $output );
					
					if( checkInArray('index', $only) )
						insertMethod($controller, $name, 'get', 'index', $output);
						
					if( checkInArray('create', $only) )
						insertMethod($controller, $name, 'post', 'create', $output);
					
					if( checkInArray('new', $only) )	
						insertMethod($controller, 'new', 'get', 'new', $output[$name]);

					if( checkInArray('show', $only) )
						insertMethod($controller, $id, 'get', 'show', $output[$name]);
						
					if( checkInArray('update', $only) )
						insertMethod($controller, $id, 'put', 'update', $output[$name]);

					if( checkInArray('destroy', $only) )
						insertMethod($controller, $id, 'delete', 'destroy', $output[$name]);
						
					if( checkInArray('edit', $only) )
					{
						Router::fix_key_is_array( $id, $output[$name] );
							
						insertMethod($controller, 'edit', 'get', 'edit', $output[$name][$id]);
					}
				}
				else
				{
					if( isset( $arr['action'] ) )
					{
						$via = isset( $arr['via'] ) ? $arr['via'] : 'get' ;
					
						insertMethod($controller, $name, $via, $arr['action'], $output);
					}
				}
				
			}
			
			function processResources($arr, &$output)
			{
				if( !is_array( $arr ) )
					return;
				
				foreach( $arr as $key => $val )
				{
					if( $key[0] == ':' )
					{
						$name = verifyName( substr($key, 1) );
						
						processAtom( $val, true, $name, $name, $output );
						
						Router::fix_key_is_array( $name, $output );
							
						if( is_array( $val ) && count( $val ) > 0 )
						{
							Router::fix_key_is_array( $key, $output[$name] );
								
							processResources( $val, $output[$name][$key] );
						}
					
					}
				
				}	
			}
			
			function recursiveArrayClean( &$arr )
			{
				if( !is_array( $arr ) )
					return;
				
				foreach( $arr as $k => $v )
				{
					if( is_null( $v ) || ( is_array( $v ) && count($v) == 0 ) )
						unset( $arr[$k] );
					else	
						recursiveArrayClean( $v );			
				}
			}
			
			if( !is_array($cached) )
				$cached = array();

				
			$cached['rules'] = array();
			$cached['version'] = $version;
			
			
			if( isset( $routes['resources'] ) && is_array( $routes['resources'] ) )
				processResources( $routes['resources'], $cached['rules'] );
			
			if( isset( $routes['matches'] ) && is_array( $routes['matches'] ) )
			{
				foreach( $routes['matches'] as $rule )
				{
					if( !is_array( $rule ) || !isset( $rule['match'] ) )
						continue;

					$controller = null;
					
					$i = 0;
					$val = null;
					$next = 0;
					
					$exp = explode('/', $rule['match'] ) ;
					
					$lastLevel =& $cached['rules'] ;

					while( ($next = static::hasNext( $i, $exp ) ) !== false )
					{
						$i = 1 + $next;
						$val = $exp[$next] ;

						if( is_null( $controller ) )
							$controller = $val;
						
						static::fix_key_is_array( $val, $lastLevel );
						
						if( static::hasNext( $i, $exp ) === false )
						{
							processAtom( $rule, false, $controller, $val, $lastLevel);
							
							break;
						}
						else
							$lastLevel =& $lastLevel[$val];
							
					}
				}
			}
			
			recursiveArrayClean( $cached );
		}
		
		public function route()
		{
			$i = 0;
			$next = 0;
			$found = false;
			$lastValue = &$this->cachedRoutes['rules'] ;
			$exp = explode('/', $this->url) ;
			
			while( ($next = static::hasNext( $i, $exp )) !== false )
			{
				if( !is_array( $lastValue ) )
					break;
				
				$i = 1 + $next;
				$val = $exp[$next];
				
				// Get format
				if( static::hasNext( $i, $exp ) === false )
				{
					$formatExp = explode( '.', $val , 2 );
					$val = $formatExp[0];
					
					$_REQUEST['z_format'] = ( count( $formatExp ) > 1 ) ? $formatExp[1] : "html" ;
				}
				
				$found = false;
				
				// Existe a chave definida
				if( isset( $lastValue[$val] ) )
				{
					$lastValue = &$lastValue[$val];
					$found = true;
				}
				
				// Em alternativa, se existir uma variável, atribui-se o valor
				// à sua primeira ocurrência
				else
				{	
					foreach( $lastValue as $k => $v )
					{
						if($k[0] == ':')
						{
							$lastValue = &$lastValue[$k];
							$found = true;
							
							$_REQUEST[substr($k, 1)] = $val ;
							
							break;
						}
					}
				}
				
				// Se não foi encontrada nenhuma ocorrência
				// então não existe mais alternativas
				if( !$found )
					break;
			}
			
			if( $found )
			{
				$key = '#' . strtoupper( $_SERVER['REQUEST_METHOD'] );
				
				if( isset( $lastValue[ $key ] ) && is_array( $lastValue[ $key ] ) )
				{
					$act = $lastValue[ $key ] ;
					if( isset( $act['a'] ) && isset( $act['c'] ) )
					{
						$this->controller = $act['c'];
						$this->controllerAction = $act['a'];
						$this->method = $_SERVER['REQUEST_METHOD'];
						
						$this->controllerFound = true;
						
						
					}
				}
			}
			
			if( $this->controllerFound )
				$this->loadController( $this->controller, $this->controllerAction );
			else
			{
				header("HTTP/1.0 404 Not Found");
				
				include_once( NOT_FOUND_PAGE );
			}
			
			return $this->controllerFound ;
		}
		
		private function loadController( $controller, $action )
		{
			$className = ucfirst($controller) . "Controller";

			$instance = new $className();
			
			if( !method_exists( $instance , $action) )
				throw new Exception("Method '${action}' does not exits in class '${className}'.");
				
			else
				$instance->$action();
		
		}
		
		public function getControllerName()
		{
			return $this->controller;
		}
	
	}

?>