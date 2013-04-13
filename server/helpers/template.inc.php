<?php

	DEFINE('VIEWS_DIR', ROOT . '/views' );
	DEFINE('PAGE_HEADER', VIEWS_DIR . '/header.html.php');
	DEFINE('PAGE_FOOTER', VIEWS_DIR . '/footer.html.php');

	class Template {
		
		private $data = array();
		
		private $include_headers = true;
		
		
		public function includeHeaders( $inc )
		{
			if( is_bool( $inc ) )
				$this->include_headers = $inc;
		}
		
		
		public function render( $view )
		{
			$router = Router::getInstance();
			
			$view_file = VIEWS_DIR . "/{$router->getControllerName()}/{$view}.html.php" ;
			
			if( !is_file( $view_file ) )
				throw new Exception( "View file '{$view_file}' does not exist" );
				
			$data = &$this->data ;
			
			if( $this->include_headers )
				include_once( PAGE_HEADER );

			include_once( $view_file );
			
			if( $this->include_headers )
				include_once( PAGE_FOOTER );
		}
		
		public function renderJSON( $arr )
		{
			$jsonEnc = null;
			
			if( is_null( $arr ) || !is_array( $arr ) || ($jsonEnc = @json_encode( $arr ) ) === false )
				throw new Exception("Cannot encode array as json");
			
			else
			{
				header('Content-Type: application/json', true);
				echo $jsonEnc;
			}
		}
	
	
		public function get( $key, $default = null )
		{
			if( isset( $this->data[ $key ] ) )
				return $this->data[ $key ];
			
			return $default;
		}
		
		public function set( $key, $value )
		{
			$this->data[$key] = $value ;
		}
	}
?>