<?php

	class QuizGameQuestion extends ActiveRecord {

		const TABLE_NAME = "qgquestion";


		const TYPE_UNKNOWN = 0;
		const TYPE_STRING = 1;
		const TYPE_RADIO  = 2;
		const TYPE_MULTI  = 3;



		private function __construct() { }


		function getQID()
		{
			return (int)$this->getData('qid');
		}
		function getQuestion()
		{
			return $this->getData('question');
		}
		function getQuestionType()
		{
			return (int)$this->getData('question_type', self::TYPE_UNKNOWN);
		}
		function getAnswerChoices()
		{
			return $this->getData('answer_choices');
		}
		function getExpectedAnswer()
		{
			return $this->getData('expected_answer');
		}
		function getPID()
		{
			return (int)$this->getData('pid');
		}


		public function validateAnswer($answer)
		{

			$expected = $this->getExpectedAnswer() ;


			switch( $this->getQuestionType() )
			{
				case QuizGameQuestion::TYPE_STRING:

					$a1 = strtolower( trim( $expected ) );
					$a2 = strtolower( trim( $answer ) );

					if( strlen($a1) > 0 && $a1 === $a2 )
						return true;

					break;

				case QuizGameQuestion::TYPE_RADIO:

					if( is_numeric($answer) && (int)$expected === (int)$answer )
						return true;

					break;

				case QuizGameQuestion::TYPE_MULTI:

					$expectedArr = explode(';', $expected );
					$totalExpected = count($expectedArr) ;

					$answerArr = explode(';', $answer );
					
					$multis = 0;

					if( $totalExpected > 0 && count($answerArr) === $totalExpected )
					{
						for($i = 0; $i < $totalExpected; $i++)
							$answerArr[$i] = trim( $answerArr[$i] );

						foreach( $expectedArr as $g )
						{
							$g = trim( $g );

							if( strlen($g) <= 0 )
								continue;

							for($i = 0; $i < $totalExpected; $i++)
							{
								if( !is_null($answerArr[$i]) && $g === $answerArr[$i] )
								{
									$answerArr[$i] = null;

									$multis++;
								}
							}
						}

						if( $multis === $totalExpected )
							return true;
					}

					break;
			}

			return false;
		}


		public static function findByPID($pid)
		{
			$questions = array();
			$return = static::executeQuery( 'SELECT * FROM ' . self::TABLE_NAME . ' WHERE pid = ? ORDER BY qid ASC;',
									  			array( $pid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $res = static::fillModel( $row, new QuizGameQuestion() ) ) )
						$questions[] = $res ;
				}
			}

			return $questions;
		}

	}
