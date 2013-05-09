<?php

	DEFINE( 'R_QUIZ_ERR_PARAM'				, 0x10 );
	DEFINE( 'R_QUIZ_ERR_QUIZ_NOT_FOUND'		, 0x11 );
	DEFINE( 'R_QUIZ_ERR_USERPROM_NOT_FOUND'	, 0x12 );

	DEFINE( 'R_QUIZ_ERR_PARAM_2'			, 0x20 );
	DEFINE( 'R_QUIZ_ERR_QUEST_NOT_FOUND'	, 0x21 );
	DEFINE( 'R_QUIZ_ERR_ANSWERS_MISSING'	, 0x22 );
	DEFINE( 'R_QUIZ_ERR_USERPROM_PARTIC'	, 0x23 );

	DEFINE( 'R_QUIZ_ERR_PROM_AUTOFVALID'	, 0x30 );

	// DEFINE( 'R_QUIZ_ERR_SESSION_NOT_FOUND'	, 0x20 );


	class QuizgameController extends Controller {

		private static $status = array(
				R_QUIZ_ERR_PARAM				=> 'ID da promoção não definida',
				R_QUIZ_ERR_QUIZ_NOT_FOUND		=> 'Quiz Game não encontrado',
				R_QUIZ_ERR_USERPROM_NOT_FOUND	=> 'Promoção de utilizador não encontrada, ou não pertence ao utilizador',

				R_QUIZ_ERR_PARAM_2				=> 'Parâmetros de resposta não definidos correctamente',
				R_QUIZ_ERR_QUEST_NOT_FOUND		=> 'Perguntas não encontradas, ou a promoção não é do tipo quizgame',
				R_QUIZ_ERR_ANSWERS_MISSING		=> 'Nem todas as perguntas foram respondidas',
				R_QUIZ_ERR_USERPROM_PARTIC		=> 'O questionário não pode ser respondido',

				R_QUIZ_ERR_PROM_AUTOFVALID		=> 'A promoção não é válida, ou expirou',

				// R_SESS_ERR_SESSION_NOT_FOUND	=> 'Sessão não encontrado',
			);

		protected function __configure()
		{
			$this->requireAuth();
		}


		public function show()
		{

			$pid = valid_request_var( 'promotion' );
			$quiz = null;

			if( is_null( $pid ) )
				$this->respond->setJSONCode( R_QUIZ_ERR_PARAM );

			elseif( is_null( $quiz = QuizGame::findByPID( $pid ) ) )
				$this->respond->setJSONCode( R_QUIZ_ERR_QUIZ_NOT_FOUND );

			else
			{
				$questionsResult = $quiz->getQuestions();
				$quests = array();

				foreach($questionsResult as $q)
				{
					$type = $q->getQuestionType() ;
					$choices = $q->getAnswerChoices();

					switch( $type )
					{
						case QuizGameQuestion::TYPE_RADIO:
						case QuizGameQuestion::TYPE_MULTI:

							$choices = explode(';', $choices);

							break;
					}
					
					$quests[] = array( 'qid' => $q->getQID(),
									   'question' => $q->getQuestion(),
									   'type' => $type,
									   'answer_choices' => $choices );
				}

				$this->respond->setJSONResponse( array( 'name' => $quiz->getName(),
														'is_quiz' => $quiz->isQuiz(),
														'questions' => $quests ) );
				$this->respond->setJSONCode( R_STATUS_OK );
			}

			$this->respond->renderJSON( static::$status );
		}



		public function submit_answers()
		{
			//$qid = valid_request_var( 'qid' );
			$pid = (int)valid_request_var( 'promotion' );
			$upid = (int)valid_request_var( 'upid' );
			$answers = valid_request_var( 'answer' );


			if( $pid == 0 || $upid == 0 || is_null( $answers ) || !is_array($answers) )
				$this->respond->setJSONCode( R_QUIZ_ERR_PARAM_2 );

			else
			{
				$authUID = (int)Authenticator::getInstance()->getUserId();
				$userProm = UserPromotion::findByUPID( $upid ) ;
				$user = null;
				$promo = null;
				$time = time();
				$quiz = $questions = null;


				// UserPromotion participation not found
				if( is_null( $authUID ) || is_null( $userProm ) || is_null( $user = User::findByUID( $authUID ) )
					|| $authUID <= 0 || $userProm->getUID() !== $authUID )
						$this->respond->setJSONCode( R_QUIZ_ERR_USERPROM_NOT_FOUND );

				// Check promotion exepiration date
				elseif( is_null( $promo = $userProm->getPromotion() )
						|| !$promo->isActive() || $pid !== $promo->getPID()
						|| $promo->getEndDate() > 0 && $promo->getEndDate() < $time )
							$this->respond->setJSONCode( R_QUIZ_ERR_PROM_AUTOFVALID );

				// UserPromotion not available to be answered
				elseif( !$userProm->inMotion() )
					$this->respond->setJSONCode( R_QUIZ_ERR_USERPROM_PARTIC );

				// QuizGame or quizgame questions not found
				elseif( is_null( $quiz = QuizGame::findByPID( $pid ) )
						|| is_null( $questions = $quiz->getQuestions() )
						|| !is_array( $questions ) || count( $questions ) == 0
						|| $userProm->getPID() !== $pid )
							$this->respond->setJSONCode( R_QUIZ_ERR_QUEST_NOT_FOUND );

				else
				{
					$dbh = DbConn::getInstance()->getDB();
					$hasError = false;
					$rightAnswers = 0;
					$totalQuestions = count( $questions ) ;
					$isQuiz = $quiz->isQuiz() ;

					try {

						// Start transaction
						$dbh->beginTransaction();

						foreach($questions as $q)
						{
							$qid = $q->getQID() ;
							$ans = null;

							if( !isset($answers[$qid]) || strlen( $ans = $answers[$qid] ) <= 0 )
							{
								$hasError = true;
								$this->respond->setJSONCode( R_QUIZ_ERR_ANSWERS_MISSING );

								break;
							}
							elseif( $quiz->isQuiz() )
							{
								if( $q->validateAnswer( $ans ) )
									$rightAnswers++;
							}

							$qga = new QuizGameAnswer( $q, $userProm );
							$qga->setAnswer( $answers[$qid] );

							if( !$qga->save() )
							{
								$hasError = true;
								$this->respond->setJSONCode( R_GLOB_ERR_SAVE_UNABLE );
								
								break;
							}
						}

						$resp = null;

						if( !$hasError )
						{
							$won = ( !$isQuiz || $rightAnswers === $totalQuestions ) ;
							$prizecode = null;
							$userProm->setEndDate( $time );

							if( $won )
								$userProm->setState( UserPromotion::STATE_WON );

							if( $userProm->save() ) // need to save now and get the auto-increment ID
							{
								if( $won )
								{
									$pc = new PrizeCode();
									$pc->setEmissionDate($time);
									$pc->setUPID($userProm->getUPID());
									$pc->setOwnerUID($authUID);

									$pc->genValidCode($user->getSeed());
									$prizecode = $pc->getCode();

									if( !$pc->save() )
										$hasError = true;
								}
							}
							else
								$hasError = true;

							if( !$hasError )
								$resp = array('won' => $won, 'correct' => $rightAnswers, 'prizecode' => $prizecode);
							else
								$this->respond->setJSONCode( R_GLOB_ERR_SAVE_UNABLE );
						}
							
						if( $hasError )
							$dbh->rollBack();

						else
						{
							$this->respond->setJSONResponse( $resp );
							$this->respond->setJSONCode( R_STATUS_OK );
							
							// Commit
							$dbh->commit();
						}
						

					} catch (Exception $e) {
						// Failed, so lets rollback
  						$dbh->rollBack();

  						throw $e;
  					}
  				}

			}

			$this->respond->renderJSON( static::$status );

		}


	}


?>