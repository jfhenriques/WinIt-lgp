<?php


	$GLOBALS['routes'] = array(
		
		/*'resources' => array(
				':notes' => array( 'only' => array( 'edit', 'index', 'show' ) ),
				':user' => array(
					':account' => array(
						':bill' => array( 'only' => array( 'index' ) )
					),
					':bill' => null,
				)
		),*/
		
		
		/*****************************************************************************************
		 *	Namespace e resources
		 *	Os namespace podem ser nested
		 *****************************************************************************************/
		 
		'namespace' => array(
			'name' => 'api',		// Sem barras nem espaços, de preferência só letras

			'resources' => array(
				
			),
		),
		
		
		/*****************************************************************************************
		 *	Qual o caminho que será a raíz.
		 *	Nesta raíz será chamado o método "show", como GET.
		 *	Só funciona na raíz de todo o projecto, e não funciona para namespaces
		 *****************************************************************************************/
		
		'root' => 'home',
			
		
		
		/*****************************************************************************************
		 *	Aqui são definidos todos a caminhos que sejam necessários,
		 *	e que não respeitem se identifiquem com a lógica dos controladores.
		 *	Pode ser feito o match de caminhos e automaticamente definir variáveis,
		 *	p.ex: ao fazer match disto /qlqrcoisa/home/01-01-1988 nisto "/qlqrcoisa/:page/:data"
		 *	instanciará as variáves page e data com o respectivos valores
		 *****************************************************************************************/
		 
		'matches' => array(
		
			array( 'match' => '/home', 'controller' => 'home', 'via' => 'get', 'action' => 'index' ),
			
		),
	
	);

?>