<?php

	class DbConn {
	
		private static $instance = null;
		private $db = null;
		
		private function __clone() {}
		private function __construct()
		{	
			$this->db = new PDO(PDO_DATABASE, PDO_USERNAME, PDO_PASSWORD, array(
								PDO::ATTR_PERSISTENT => true,
								PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
								PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
							) );
			
			/*$this->db->setAttribute(, );
			$this->db->setAttribute();*/
		}
		
		public static function getInstance()
		{
			if( is_null( static::$instance ) )
				static::$instance = new DbConn();
			
			return static::$instance;
		}
		
		public function getDB()
		{
			return $this->db ;
		}
	}
			
	
	class ActiveRecord {
	
		protected static function cachedQuery($id, $prefix, $sql, $arrExec, $validationFunc = null)
		{
			if( is_null( $id ) || strlen( $id ) == 0 )
				return false;
		
			$id_key = CommonCache::buildVarName($prefix, $id);
			
			$cc = CommonCache::getInstance();
			
			$arr = $cc->get( $id_key );

			if( $arr !== false && is_array( $arr ) )
			{
				if( is_null( $validationFunc ) )
					return $arr;
					
				else
				{
				
					if( $validationFunc( $arr ) )
						return $arr;
						
					else
						$cc->delete( $id_key );

				}
			}	
			else
			{
				$arr = static::query( $sql, $arrExec );
				
				if( is_array( $arr ) && count( $arr ) > 0 )
				{
					if( is_null( $validationFunc ) || $validationFunc( $arr ) )
					{
						$cc->set( $id_key, $arr ) ;
					
						return $arr;
					}
				}
			}
		}
		
		
		protected static function query( $sql, $execArr )
		{
			$dbh = DbConn::getInstance()->getDB();

			$sth = $dbh->prepare($sql);
			
			if( $sth->execute($execArr) )
				return $sth->fetch();
				
			return false;
		}

	}

?>