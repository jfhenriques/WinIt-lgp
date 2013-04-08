<?php

	class  HomeController extends Controller {
	

		public function index()
		{

			$home = new Home();
			
			$this->respond->render('home');
		
		}
	
	
	}
?>