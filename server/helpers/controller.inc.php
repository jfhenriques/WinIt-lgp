<?php

	class Controller {
	
		public $respond = null;
		
		public function __construct()
		{
			$this->respond = new Template();
		}
	
	}
?>