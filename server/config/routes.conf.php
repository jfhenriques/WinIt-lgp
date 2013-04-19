<?php


	$GLOBALS['routes'] = array(
		
		
		/*****************************************************************************************
		 *	Namespace e resources
		 *	Os namespace podem ser nested
		 *****************************************************************************************/
		 
		'namespace' => array(
			'name' => 'api',		// Sem barras nem espaços, de preferência só letras

			'resources' => array(
			
				':session' => array( 'only' => array( 'create', 'destroy' ) ),
				
				':user' => array( 'only' => array( 'create', 'index' ),
					':tag' => null,
					':promotion' => null,
					':badge' => null,
				), 
				
				// considerar o id do user que esta logado
				':promotion' => array( 'only' => array( 'index', 'show' ),

					':tag'  => array( 'only' => array( 'index', 'show' ) ),
					// ':game' => array(
					// 	':quizgame' => array(
					// 		':question' => null,
					// 		':answer' => null,
					// 	),
					// 	':proximtygame' => null,
					// ),
				),
				
				':tag' => array( 'only' => array( 'index', 'show' ) ),
				
				// ':trade' => array(
				// 	':promotion' => null,
				// 	':sugestion' => null,
				// ),
				
			),
		),
		
		
		/*****************************************************************************************
		 *	Qual o caminho que será a raíz.
		 *	Nesta raíz será chamado o método "show", como GET.
		 *	Só funciona na raíz de todo o projecto, e não funciona para namespaces
		 *****************************************************************************************/
		
		//'root' => 'home',
			
		
		
		/*****************************************************************************************
		 *	Aqui são definidos todos a caminhos que sejam necessários,
		 *	e que não respeitem se identifiquem com a lógica dos controladores.
		 *	Pode ser feito o match de caminhos e automaticamente definir variáveis,
		 *	p.ex: ao fazer match disto /qlqrcoisa/home/01-01-1988 nisto "/qlqrcoisa/:page/:data"
		 *	instanciará as variáves page e data com o respectivos valores
		 *****************************************************************************************/
		 
		'matches' => array(
		
			//array( 'match' => '/home', 'controller' => 'home', 'via' => 'get', 'action' => 'index' ),

			array( 'match' => '/api',
				   'matches' => array(

						array( 'match' => '/user', 'controller' => 'user', 'via' => 'put', 'action' => 'edit',
							   'matches' => array(

									array( 'match' => '/tags', 'controller' => 'user', 'via' => 'get', 'action' => 'list_tags' ),
									array( 'match' => '/promotions', 'controller' => 'user', 'via' => 'get', 'action' => 'list_promotions' ),

								),
						),

						array( 'match' => '/address/:cp4/:cp3', 'controller' => 'address', 'via' => 'get', 'action' => 'list_cp' ),
				   	),
			),
			
		),
	
	);

?>