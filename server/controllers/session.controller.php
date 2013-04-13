<?php

	class  SessionController extends Controller {
	

		public function __configure()
		{
			
		}
		
		
		public function index()
		{
			
			$this->checkAuth();
			
			$auth = Authenticator::getInstance();
			echo "t: " . $auth->getToken();
			
			//$this->respond->renderJSON();
		
		}
	
	
	}
?>