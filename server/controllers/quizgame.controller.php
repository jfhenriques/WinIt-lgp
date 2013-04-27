<?php

	DEFINE( 'R_QUIZ_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_QUIZ_ERR_QUIZ_NOT_FOUND'		, 0x11 );
	DEFINE( 'R_QUIZ_ERR_USERPROM_NOT_FOUND'	, 0x12 );

	DEFINE( 'R_QUIZ_ERR_PARAM_2'			, 0x20 );
	DEFINE( 'R_QUIZ_ERR_QUEST_NOT_FOUND'	, 0x21 );
	DEFINE( 'R_QUIZ_ERR_ANSWERS_MISSING'	, 0x22 );

	// DEFINE( 'R_QUIZ_ERR_SESSION_NOT_FOUND'	, 0x20 );


	class QuizgameController extends Controller {

		private static $status = array(
				R_QUIZ_ERR_PARAM				=> 'ID da promoção não definida',
				R_QUIZ_ERR_QUIZ_NOT_FOUND		=> 'Quiz Game não encontrado',
				R_QUIZ_ERR_USERPROM_NOT_FOUND	=> 'Promoção de utilizador não encontrada, ou não pertence ao utilizador',

				R_QUIZ_ERR_PARAM_2				=> 'Parâmetros de resposta não definidos correctamente',
				R_QUIZ_ERR_QUEST_NOT_FOUND		=> 'Perguntas não encontradas, ou a promoção não é do tipo quizgame',
				R_QUIZ_ERR_ANSWERS_MISSING		=> 'Nem todas as perguntas foram respondidas',

				// R_SESS_ERR_SESSION_NOT_FOUND	=> 'Sessão não encontrado',
			);

		protected function __configure()
		{
			$this->requireAuth();
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



		public function submit_answers()
		{
			//$qid = valid_request_var( 'qid' );
			$pid = (int)valid_request_var( 'promotion' );
			$upid = (int)valid_request_var( 'upid' );
			$answers = valid_request_var( 'answers' );


			if( is_null( $pid ) || is_null( $upid ) || is_null( $answers ) || !is_array($answers) )
				$this->respond->setJSONCode( R_QUIZ_ERR_PARAM_2 );

			else
			{
				$authUID = (int)Authenticator::getInstance()->getUserId();
				$userProm = UserPromotion::findByUPID( $upid ) ;

				if( is_null( $authUID ) || is_null( $userProm )
						|| $authUID <= 0 || $userProm->getUID() !== $authUID )
					$this->respond->setJSONCode( R_QUIZ_ERR_USERPROM_NOT_FOUND );

				else
				{
					$quiz = $questions = null;

					if( !is_null( $quiz = QuizGame::findByPID( $pid ) ) )
						$questions = QuizGameQuestion::findByPID( $pid );
					
					if( is_null( $questions ) || !is_array( $questions ) || count( $questions ) == 0
						 || $pid <= 0 || $userProm->getPID() !== $pid )
						$this->respond->setJSONCode( R_QUIZ_ERR_QUEST_NOT_FOUND );

					else
					{
						$dbh = DbConn::getInstance()->getDB();
						$verifiedAll = true;

						try {

							$dbh->beginTransaction();

							foreach($questions as $q)
							{
								$qid = $q->getQID() ;

								if( !isset($answers[$qid]) || strlen( $answers[$qid] ) <= 0 )
								{
									$verifiedAll = false;
									$this->respond->setJSONCode( R_QUIZ_ERR_ANSWERS_MISSING );

									break;
								}
								elseif( $quiz->isQuiz() )
								{
									// verificar por tipos!!!!

								}

								$qga = new QuizGameAnswer( $q, $userProm );
								$qga->setAnswer( $answers[$qid] );

								if( !$qga->save() )
								{
									$verifiedAll = false;
									$this->respond->setJSONCode( R_GLOB_ERR_SAVE_UNABLE );
									
									break;
								}
							}


							if( !$verifiedAll )
								$dbh->rollBack();

							else
							{
								$this->respond->setJSONCode( R_STATUS_OK );
								
								$dbh->commit();
							}
							

						} catch (Exception $e) {
	  						$dbh->rollBack();

	  						throw $e;
	  					}
	  				}

				}

			}

			$this->respond->renderJSON( static::$status );

		}


	}


?>