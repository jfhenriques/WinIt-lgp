<?php

	class QuizGame extends ActiveRecord {

		const TABLE_NAME = "quizgame";


		private function __construct() { }


		public function getPID()
		{
			return (int)$this->getData('pid');
		}
		public function getName()
		{
			return $this->getData('name');
		}
		public function isQuiz()
		{
			return (bool)$this->getData('is_quiz', true);
		}


		public function getQuestions()
		{
			$pid = $this->getPID();

			return $pid > 0 ? QuizGameQuestion::findByPID( $pid ) : array() ;
		}



		public static function findByPID($pid)
		{

			$result = static::query( 'SELECT * FROM ' . self::TABLE_NAME . ' WHERE pid = ? LIMIT 1;',
									  		array( $pid ) );

			return static::fillModel( $result, new QuizGame() );
		}

	}

?>