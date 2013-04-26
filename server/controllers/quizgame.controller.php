<?php

	DEFINE( 'R_QUIZ_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_QUIZ_ERR_QUIZ_NOT_FOUND'		, 0x11 );

	// DEFINE( 'R_QUIZ_ERR_SESSION_NOT_FOUND'	, 0x20 );


	class QuizgameController extends Controller {

		private static $status = array(
				R_QUIZ_ERR_PARAM				=> 'ID da promoção não definida',
				R_QUIZ_ERR_QUIZ_NOT_FOUND		=> 'Quiz Game não encontrado',

				// R_SESS_ERR_SESSION_NOT_FOUND	=> 'Sessão não encontrado',
			);

		protected function __configure()
		{
			//$this->requireAuth();
		}


		public function show()
		{

			$pid = valid_request_var( 'promotion' );

			
			if( is_null( $pid ) )
				$this->respond->setJSONCode( R_QUIZ_ERR_PARAM );

			else
			{
				$quiz = QuizGame::findByPID( $pid );

				if( is_null( $quiz ) )
					$this->respond->setJSONCode( R_QUIZ_ERR_QUIZ_NOT_FOUND );

				else
				{
					$questionsResult = $quiz->findQuestions();
					$quests = array();

					foreach($questionsResult as $q)
					{
						$quests[] = array( 'qid' => $q->getQID(),
										   'question' => $q->getQuestion(),
										   'type' => $q->getAnswerType(),
										   'answer_choices' => $q->getAnswerChoices() );
					}

					$this->respond->setJSONCode( R_STATUS_OK );
					$this->respond->setJSONResponse( array( 'name' => $quiz->getName(),
															'is_quiz' => $quiz->isQuiz(),
															'questions' => $quests ) );
				}
			}


			$this->respond->renderJSON( static::$status );
		}


	}


?>