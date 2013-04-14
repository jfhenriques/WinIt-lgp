<?php
	
	class User extends SQLModel {
		private $utilizadorid = null;
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
			
			$dbh = DbConn::getInstance()->getDB();
			
			$sth = $dbh->prepare('SELECT * FROM utilizador WHERE utilizadorid = :id');
			$sth->bindParam(':id', $id, PDO::PARAM_INT);
			$sth->execute();
				
			$result = $sth->fetch();
			
			$user = new User();
			
			$this->nome = $result['nome'];
			$this->email = $result['email'];
			$this->password = $result['password'];
			$this->cp4 = $result['cp4'];
			$this->cp3 = $result['cp3'];
			$this->porta_andar = $result['porta_andar'];
			$this->token_facebook = $result['token_facebook'];
			$this->token_twitter = $result['token_twitter'];
			this->utilizadorid = $id;
			
			return $user;
		}
		
		public function save() {
			$dbh = DbConn::getInstance()->getDB();
			
			$sth = null;
			
			if( is_null($utilizadorid))
				$sth = $dbh->prepare('INSERT INTO users VALUES(:nome, :email, :password, :cp4, :cp3, :porta_andar, :token_facebook, :token_twitter, NULL)');
			else
				$sth = $dbh->prepare('UPDATE users SET nome = :nome, email = :email, password = :password, cp4 = :cp4, cp3 =:cp3, porta_andar = :porta_andar, token_facebook = :token_facebook, token_twitter = :token_twitter, NULL)');
			
			$sth->bindParam(':nome', $this->nome, PDO::PARAM_STR);
			$sth->bindParam(':email', $this->email, PDO::PARAM_STR);
			$sth->bindParam(':password', $this->password, PDO::PARAM_STR);
			$sth->bindParam(':cp4', $this->cp4, PDO::PARAM_INT);
			$sth->bindParam(':cp3', $this->cp3, PDO::PARAM_INT);
			$sth->bindParam(':porta_andar', $this->porta_andar, PDO::PARAM_STR);
			$sth->bindParam(':token_facebook', $this->token_facebook, PDO::PARAM_STR);
			$sth->bindParam(':token_twitter', $this->token_twitter, PDO::PARAM_STR);			
			
			$sth->execute();
			
			if( is_null($utilizadorid))
				$this->utilizadorid = $dbh->lastInsertId();
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