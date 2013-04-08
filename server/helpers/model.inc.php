<?php

	class DbConn {
	
		private static $instance = null;
		private $db = null;
		
		private function __clone() {}
		private function __construct()
		{
			$this->db = new PDO(PDO_DATABASE, PDO_USERNAME, PDO_PASSWORD, array(
						PDO::ATTR_PERSISTENT => true
					) );
			$this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			$this->db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
		}
		
		public static function getInstance()
		{
			if( is_null( self::$instance ) )
				self::$instance = new DbConn();
			
			return self::$instance;
		}
		
		public function getDB()
		{
			return $this->db ;
		}
	}

	class SQLModel {
		
		private $_db = null;
		private $_table = '';
		
		private function __contrusct()
		{
			$this->_db = DbConn::getInstance()->getDB();
		}
	
	
	}

?>