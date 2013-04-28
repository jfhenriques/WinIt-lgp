<?php

	class QuizGameQuestion extends ActiveRecord {

		const TABLE_NAME = "qgquestion";


		const TYPE_UNKNOWN = 0;
		const TYPE_STRING = 1;
		const TYPE_RADIO  = 2;
		const TYPE_MULTI  = 3;


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
			return (int)$this->getData('answer_type', self::TYPE_UNKNOWN); // needs to be changed in the database to question_type
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

					$givenArr = explode(';', $expected );
					$expArr = explode(';', $answer );
					$totalMultis = count($expArr) ;
					$multis = 0;

					if( $totalMultis > 0 && count($givenArr) === $totalMultis )
					{
						foreach( $givenArr as $g )
						{
							if( in_array( $g, $expArr ) )
								$multis++;
						}

						if( $multis === $totalMultis )
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
					if( ( $res = static::fillModel( $row, new QuizGameQuestion() ) ) !== null )
						$questions[] = $res ;
				}
			}

			return $questions;
		}


		public function save() { }

	}

?>