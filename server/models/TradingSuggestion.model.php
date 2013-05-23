<?php

	class TradingSuggestion
		extends ActiveRecord
		implements SavableActiveRecord {

		const TABLE_NAME = "tradingsuggestion";

		

		public function __construct(PrizeCode $orig, PrizeCode $dest)
		{
			if( !is_null( $orig ) && !is_null( $dest ) )
			{
				$this->data['pcid_orig'] = $orig->getPCID();
				$this->data['pcid_dest'] = $dest->getPCID();

				$this->data['transaction'] = $dest->getTransactionID();

			}
		}



		public function getPCIDOrig()
		{
			return (int)$this->getData('pcid_orig');
		}
		public function getPCIDDest()
		{
			return (int)$this->getData('pcid_dest');
		}


		public function getDate()
		{
			return (int)$this->getData('date');
		}
		public function setDate($date)
		{
			$this->data['date'] = $date;
		}


		public function getTransactionID()
		{
			return (int)$this->getData('transaction');
		}


		public function getState()
		{
			return (int)$this->getData('sate');
		}
		public function setState($sate)
		{
			$this->data['sate'] = $sate;
		}





		public function save()
		{
			$dbh = DbConn::getInstance()->getDB();

			$pcid_orig = $this->getPCIDOrig();
			$pcid_dest = $this->getPCIDDest();
			$date = $this->getDate();
			$transaction = $this->getTransactionID();
			$state = $this->getState();

			if( $pcid_orig <= 0 || $pcid_dest <= 0 )
				throw new Exception("Chave primária não definida");
			
			$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (pcid_orig,pcid_dest,date,transaction,state) ' .
								 ' VALUES(:pcid_orig, :pcid_dest, :date, :transaction, :state) ' .
								 ' ON DUPLICATE KEY UPDATE state = :state, date = :date ;');
			
			$sth->bindParam(':pcid_orig', $pcid_orig, PDO::PARAM_INT);
			$sth->bindParam(':pcid_dest', $pcid_dest, PDO::PARAM_INT);
			$sth->bindParam(':date', $date, PDO::PARAM_INT);
			$sth->bindParam(':transaction', $transaction, PDO::PARAM_INT);
			$sth->bindParam(':state', $state, PDO::PARAM_INT);

			return $sth->execute();
		}


	}

?>