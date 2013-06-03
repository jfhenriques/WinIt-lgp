<?php

	class QuizGameAnswer
		extends ActiveRecord
		implements SavableActiveRecord {

		const TABLE_NAME = "qganswer";


		public function __construct( QuizGameQuestion $qgq = null, UserPromotion $up = null )
		{
			if( !is_null($qgq) && !is_null($up) )
			{
				$this->data['qid'] = $qgq->getQID();
				$this->data['upid'] = $up->getUPID();
			}
		}

		function getQID()
		{
			return (int)$this->getData('qid');
		}
		// function setQID($qid)
		// {
		// 	$this->data['qid'] = $qid;
		// }
		function getUPID()
		{
			return (int)$this->getData('upid');
		}
		function setUPID($upid)
		{
			$this->data['upid'] = $upid;
		}
		function getAnswer()
		{
			return $this->getData('answer');
		}
		function setAnswer($answer)
		{
			$this->data['answer'] = $answer;
		}



		public static function findByQUP($qid, $upid)
		{
			$result = static::query( 'SELECT * FROM ' . self::TABLE_NAME . ' WHERE upid = ? AND qid = ? LIMIT 1;',
									  		array( $qid, $upid ) );

			return static::fillModel( $result, new ActiveRecord() );
		}


		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();

			$qid = $this->getQID() ;
			$upid = $this->getUPID() ;
			$answer = $this->getAnswer() ;

			if( $qid <= 0 || $upid <= 0 )
				throw new Exception("QID e/ou UPID têm de estar definidos");
			
			$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (qid,upid,answer) VALUES(:qid, :upid, :answer) ON DUPLICATE KEY UPDATE answer = :answer ;');	
			
			$sth->bindParam(':qid', $qid, PDO::PARAM_INT);
			$sth->bindParam(':upid', $upid, PDO::PARAM_INT);
			$sth->bindParam(':answer', $answer, PDO::PARAM_STR);

			return $sth->execute();

		}

	}
