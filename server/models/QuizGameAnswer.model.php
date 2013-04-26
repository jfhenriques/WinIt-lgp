<?php

	class QuizGameAnswer extends ActiveRecord {

		const TABLE_NAME = "quizgame";



		function getPID()
		{
			return $this->getData('pid');
			return @$this->data['pid'] ;
		}
		function getName()
		{
			return $this->getData('name');
			return @$this->data['name'] ;
		}
		function isQuiz()
		{
			return $this->getData('is_quiz');
		}




		public static function findByPID($pid)
		{

			$result = static::executeQuery( 'SELECT * FROM ' . self::TABLE_NAME . ' WHERE pid = ? LIMIT 1;',
									  		array( $pid ), $stmt );

			if( $resul)
			return static::fillModel( $result, new Quizgame() );
		}


		public function save() { }

	}

?>