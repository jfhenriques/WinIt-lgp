<?php
	
	class User extends ActiveRecord {

		private $utilizadorid = null;
		private $nome;
		private $email;
		private $password;
		private $cp4;
		private $cp3;
		private $porta_andar;
		private $token_facebook;
		private $token_twitter;
		
		const TABLE_NAME = 'utilizador' ;


		public function __construct() {}
		

		
		/* GET's and SET's*/

		public function getUtilizadorId() {
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
			$this->password = $password;
		}
		
		public function getCP4() {
			return $this->cp4;
		}
		public function setCP4($cp4) {
			$this->cp4 = $cp4;
		}
		
		public function getCP3() {
			return $this->cp3;
		}
		public function setCP3($cp3) {
			$this->cp3 = $cp3;
		}
		
		public function getPortaAndar() {
			return $this->porta_andar;
		}
		public function setPortaAndar($porta_andar) {
			$this->porta_andar = $porta_andar;
		}
		
		public function getTokenFacebook() {
			return $this->token_facebook;
		}
		public function setTokenFacebook($token_facebook) {
			$this->token_facebook = $token_facebook;
		}
		
		public function getTokenTwitter() {
			return $this->token_twitter;
		}
		public function setTokenTwitter($token_twitter) {
			$this->token_twitter = $token_twitter;
		}



		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();
			
			$sth = null;
			
			if( is_null($this->utilizadorid) )
				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME .
										' VALUES(:nome, :email, :password, :cp4, :cp3, :porta_andar, :token_facebook, :token_twitter, NULL)');
			else
				$sth = $dbh->prepare('UPDATE ' . self::TABLE_NAME .
										' SET nome = :nome, email = :email, password = :password, cp4 = :cp4, cp3 =:cp3, porta_andar = :porta_andar, token_facebook = :token_facebook, token_twitter = :token_twitter, NULL)');
			
			$sth->bindParam(':nome', $this->nome, PDO::PARAM_STR);
			$sth->bindParam(':email', $this->email, PDO::PARAM_STR);
			$sth->bindParam(':password', $this->password, PDO::PARAM_STR);
			$sth->bindParam(':cp4', $this->cp4, PDO::PARAM_INT);
			$sth->bindParam(':cp3', $this->cp3, PDO::PARAM_INT);
			$sth->bindParam(':porta_andar', $this->porta_andar, PDO::PARAM_STR);
			$sth->bindParam(':token_facebook', $this->token_facebook, PDO::PARAM_STR);
			$sth->bindParam(':token_twitter', $this->token_twitter, PDO::PARAM_STR);			
			
			$ret = $sth->execute();
			
			if( $ret && is_null($this->utilizadorid) )
				$this->utilizadorid = $dbh->lastInsertId();

			return $ret;
		}





		public static function saltPass( $pass, $salt = null )
		{
			$salt = is_null($salt) ? md5(uniqid(mt_rand(), true)) : $salt ;
			return ":${salt}:" . hash( 'sha256', $salt.$pass ) . ':';
		}

		private static function fillUser($arr)
		{
			if( is_array( $arr ) && count( $arr ) > 0 )
			{
				$user = new User();
				
				$user->utilizadorid = $arr['utilizadorid'];
				$user->nome = $arr['nome'];
				$user->email = $arr['email'];
				$user->password = $arr['password'];
				$user->cp4 = $arr['cp4'];
				$user->cp3 = $arr['cp3'];
				$user->porta_andar = $arr['porta_andar'];
				$user->token_facebook = $arr['token_facebook'];
				$user->token_twitter = $arr['token_twitter'];
				
				return $user;
			}
			
			return null;
		}

		public static function findById($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE utilizadorid = ? LIMIT 1;',
									  array( $id ) );

							
			return static::fillUser( $result );
		}

		public static function findByCredentials($email, $pass)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE email = ? LIMIT 1;',
									  array( $email ) );

			$user = static::fillUser( $result );

			if( !is_null( $user ) && !is_null( $user->password ) )
			{
				$exp = explode(':', $user->password, 3);
				if( count( $exp ) === 3 &&
						static::saltPass( $pass , $exp[1] ) === $user->password )
					return $user;
			}

			return null;
		}

	}

?>