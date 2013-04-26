<?php

	class QuizGameQuestion extends ActiveRecord {

		const TABLE_NAME = "qgquestion";


		function getQID()
		{
			return (int)$this->getData('qid');
		}
		function getQuestion()
		{
			return $this->getData('question');
		}
		function getAnswerType()
		{
			return (int)$this->getData('answer_type');
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