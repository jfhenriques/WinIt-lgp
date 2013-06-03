<?php


	$GLOBALS['routes'] = array(
		
		
		/*****************************************************************************************
		 *	Namespace e resources
		 *	Os namespace podem ser nested
		 *****************************************************************************************/
		 
		'namespace' => array(
			
			array( 'name' => 'api',		// Sem barras nem espaços, de preferência só letras
				   
				   'resources' => array(
					
						':session' => array( 'only' => array( 'create', 'destroy' ) ),
						
						':user' => array( 'only' => array( 'create', 'index' ) ),
						
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

						':badge' => array( 'only' => array( 'index', 'show' ) ),
						
						// ':trade' => array(
						// 	':promotion' => null,
						// 	':sugestion' => null,
						// ),
						
					),
			),

			/*array( 'name' => 'admin',

					'resources' => array(

						':promotion' => array( 'controller' => 'admin_promotion'),

					),

				),*/
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

				   		array( 'match' => '/test', 'controller' => 'test', 'via' => 'get', 'action' => 'test' ),

				   		array( 'match' => '/facebook', 'controller' => 'facebook', 'via' => 'get', 'action' => 'echo_back' ),
				   		array( 'match' => '/facebook', 'controller' => 'facebook', 'via' => 'post', 'action' => 'receive_update_list' ),
				   		//array( 'match' => '/facebook/deauth', 'controller' => 'facebook', 'via' => 'get', 'action' => 'echo_back' ),
				   		array( 'match' => '/facebook/deauth', 'controller' => 'facebook', 'via' => 'post', 'action' => 'deauth_user' ),

						array( 'match' => '/user', 'controller' => 'user', 'via' => 'put', 'action' => 'edit',
							   'matches' => array(

							   		array( 'match' => '/reset_password', 'controller' => 'user', 'via' => 'post', 'action' => 'reset_password' ),

/*									array( 'match' => '/tags', 'controller' => 'user', 'via' => 'get', 'action' => 'list_tags' ),
									array( 'match' => '/tags', 'controller' => 'user', 'via' => 'post', 'action' => 'assoc_tags' ),
									array( 'match' => '/tags', 'controller' => 'user', 'via' => 'delete', 'action' => 'remove_tags' ),*/

									array( 'match' => '/gcm', 'controller' => 'user', 'via' => 'post', 'action' => 'register_gcm' ),
									array( 'match' => '/gcm', 'controller' => 'user', 'via' => 'delete', 'action' => 'unregister_gcm' ),

									//array( 'match' => '/points', 'controller' => 'user', 'via' => 'get', 'action' => 'showPoints' ),
									
									array( 'match' => '/promotions',
										   'matches' => array(

										   		array( 'match' => '/suggested', 'controller' => 'user', 'via' => 'get', 'action' => 'sent_trading_suggestions'),

											   	array( 'match' => '/trading', 'controller' => 'user', 'via' => 'get', 'action' => 'list_prizes_trading' ),
										   		array( 'match' => '/tradable', 'controller' => 'user', 'via' => 'get', 'action' => 'list_prizes_tradable' ),

												array( 'match' => '/won', 'controller' => 'user', 'via' => 'get', 'action' => 'list_promotions_won' ),
												//array( 'match' => '/active', 'controller' => 'user', 'via' => 'get', 'action' => 'list_promotions_active' ),

											),
									),

									array( 'match' => '/badges', 'controller' => 'user', 'via' => 'get', 'action' => 'list_badges_won' ),

								),
						),

						array( 'match' => '/promotion/:promotion',
							   'matches' => array(

							   		array( 'match' => '/enroll', 'controller' => 'user', 'via' => 'post', 'action' => 'promotion_enroll' ),

							   		array( 'match' => '/quizgame', 'controller' => 'quizgame', 'via' => 'get', 'action' => 'show',
							   			   'matches' => array(

												array( 'match' => '/:upid', 'controller' => 'quizgame', 'via' => 'get', 'action' => 'get_submited_answer' ),
							   			   		array( 'match' => '/:upid', 'controller' => 'quizgame', 'via' => 'post', 'action' => 'submit_answers' ),

							   			   	),

							   			),
										

							   	),
						),

						array( 'match' => '/address/:cp4/:cp3', 'controller' => 'address', 'via' => 'get', 'action' => 'list_cp' ),


						array( 'match' => '/trading', 'controller' => 'trading', 'via' => 'get', 'action' => 'index',
							   'matches' => array(
							   
									array( 'match' => '/:prizecode', 'controller' => 'trading', 'via' => 'get', 'action' => 'in_trading_suggestions'),
									array( 'match' => '/:prizecode', 'controller' => 'trading', 'via' => 'put', 'action' => 'send_to_trading'),
									array( 'match' => '/:prizecode', 'controller' => 'trading', 'via' => 'delete', 'action' => 'remove_from_trading'),

									array( 'match' => '/:prizecode',
										   'matches' => array(
							
												array( 'match' => '/suggest/:suggest', 'controller' => 'trading', 'via' => 'post', 'action' => 'suggest_trading'),
												array( 'match' => '/suggest/:suggest', 'controller' => 'trading', 'via' => 'delete', 'action' => 'unsuggest_trading'),
												array( 'match' => '/suggested/:suggest', 'controller' => 'trading', 'via' => 'get', 'action' => 'accept_trading'),
												array( 'match' => '/suggested/:suggest', 'controller' => 'trading', 'via' => 'delete', 'action' => 'reject_trading'),

												
											),
											
									),
									
								),
						),

				   	),

			),
			
			array( 'match' => '/reset_password/:reset_token', 'controller' => 'user', 'via' => 'get', 'action' => 'reset_password_confirmation' ),
			
		),
	
	);

