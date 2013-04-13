<?php

	class  UserController extends Controller {
	

		public function show()
		{

			$home = new Home();
			
			$this->respond->render('home');
		
		}
	
	
	}
?>