<?php
	
	class User extends SQLModel {
		private $utilizadorid;
		private $nome;
		private $email;
		private $password;
		private $cp4;
		private $cp3;
		private $porta_andar;
		private $token_facebook;
		private $token_twitter;
		
		function __construct() {}
		
		public static function getUser($id) {
			if($id = $utilizadorid) {
				return $this;
			}
		}
		
		public static function showUser($id) {
			
			$dbh = DbConn::getInstance()->getDB();
			
			$sth = $dbh->prepare('SELECT * FROM utilizador WHERE utilizador.id = :id');
			$sth->bindParam(':id', $id, PDO::PARAM_INT);
			$sth->execute();
				
			$result = $sth->fetch();
			
			echo json_encode($result);
		}
		
		public static function newUser($nome, $email, $password, $cp4, $cp3, $porta_andar, $token_facebook, $token_twitter) {
			$dbh = DbConn::getInstance()->getDB();
			
			$sth = $dbh->prepare('INSERT INTO users VALUES(:nome, :email, :password, :cp4, :cp3, :porta_andar, :token_facebook, :token_twitter, NULL)');
			$sth->bindParam(':nome', $nome, PDO::PARAM_STR);
			$sth->bindParam(':email', $email, PDO::PARAM_STR);
			$sth->bindParam(':password', $password, PDO::PARAM_STR);
			$sth->bindParam(':cp4', $cp4, PDO::PARAM_INT);
			$sth->bindParam(':cp3', $cp3, PDO::PARAM_INT);
			$sth->bindParam(':porta_andar', $porta_andar, PDO::PARAM_STR);
			$sth->bindParam(':token_facebook', $token_facebook, PDO::PARAM_STR);
			$sth->bindParam(':token_twitter', $token_twitter, PDO::PARAM_STR);			
			
			$sth->execute();
				
			$result = $sth->fetch();
		}
		
		/* GET's and SET's*/

		public function getUtilizadorid() {
			return $this->utilizadorid;
		}
		
		public function getnome() {
			return $this->nome;
		}		
		public function setnome($nome) {
			$this->nome = $nome;
		}
		
		public function getEmail() {
			return $this->email;
		}
		public function setEmail($email) {
			$this->email = $email;
		}
		
		public function getPassword() {
			return $this->password;
		}
		public function setPassword($password) {
			$this->password = password;
		}
		
		public function getCp4() {
			return $this->cp4;
		}
		public function setCp4($cp4) {
			$this->cp4 = $cp4;
		}
		
		public function getCp3() {
			return $this->cp3;
		}
		public function setCp3($cp3) {
			$this->cp3 = $cp3;
		}
		
		public function getPorta_andar() {
			return $this->porta_andar;
		}
		public function setPorta_andar($porta_andar) {
			$this->porta_andar = $porta_andar;
		}
		
		public function getToken_facebook() {
			return $this->token_facebook;
		}
		public function setToken_facebook($token_facebook) {
			$this->token_facebook = $token_facebook;
		}
		
		public function getToken_twitter() {
			return $this->token_twitter;
		}
		public function setToken_twitter($token_twitter) {
			$this->token_twitter = $token_twitter;
		}
	}
?>