<?php
	
	class User extends ActiveRecord {

		private $uid = null;
		private $name;
		private $email;
		private $password;
		private $adid;
		private $birth = 0;
		private $address2;
		private $token_facebook;
		private $token_twitter;
		

		const TABLE_NAME = 'user' ;


		//public function __construct() {}

		
		/* GET's and SET's*/

		public function getID()
		{
			return (int)$this->uid;
		}
		

		public function getName()
		{
			return $this->name;
		}		
		public function setName($name)
		{
			$this->name = $name;
		}
		

		public function getEmail()
		{
			return $this->email;
		}
		public function setEmail($email)
		{
			$this->email = $email;
		}
		

		public function getPassword()
		{
			return $this->password;
		}
		public function setPassword($password)
		{
			$this->password = $password;
		}
		

		public function getADID()
		{
			return (int)$this->adid;
		}
		public function setADID($adid)
		{
			$this->adid = (int)$adid;
		}

		
		public function getAddress2()
		{
			return $this->address2;
		}
		public function setAddress2($address2)
		{
			$this->address2 = $address2;
		}

		
		public function getTokenFacebook()
		{
			return $this->token_facebook;
		}
		public function setTokenFacebook($token_facebook)
		{
			$this->token_facebook = $token_facebook;
		}
		

		public function getTokenTwitter()
		{
			return $this->token_twitter;
		}
		public function setTokenTwitter($token_twitter)
		{
			$this->token_twitter = $token_twitter;
		}

		public function getBirth()
		{
			return (int)$this->birth;
		}
		public function setBirth($birth)
		{
			$this->birth = (int)$birth;
		}




		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			if( is_null($this->uid) )
				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (name, email, password, adid, door, token_facebook, token_twitter, birth) ' .
										' VALUES(:name, :email, :password, :adid, :address2, :token_facebook, :token_twitter, :birth)');
			else
			{
				$sth = $dbh->prepare('UPDATE ' . self::TABLE_NAME . ' SET name = :name , email = :email, password = :password,' .
												' adid = :adid, door = :address2, birth = :birth, token_facebook = :token_facebook,' .
												' token_twitter = :token_twitter WHERE uid = :uid ;' );
				
				$sth->bindParam(':uid', $this->uid, PDO::PARAM_INT);
			}

			$sth->bindParam(':name', $this->name, PDO::PARAM_STR);
			$sth->bindParam(':email', $this->email, PDO::PARAM_STR);
			$sth->bindParam(':password', $this->password, PDO::PARAM_STR);
			$sth->bindParam(':adid', $this->adid, PDO::PARAM_INT);
			$sth->bindParam(':address2', $this->address2, PDO::PARAM_STR);
			$sth->bindParam(':token_facebook', $this->token_facebook, PDO::PARAM_STR);
			$sth->bindParam(':token_twitter', $this->token_twitter, PDO::PARAM_STR);
			$sth->bindParam(':birth', $this->birth, PDO::PARAM_INT);
			
			$ret = $sth->execute();
			
			if( $ret && is_null($this->uid) )
				$this->uid = $dbh->lastInsertId();

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
				
				$user->uid = $arr['uid'];
				$user->name = $arr['name'];
				$user->email = $arr['email'];
				$user->password = $arr['password'];
				$user->adid = $arr['adid'];
				$user->address2 = $arr['door'];
				$user->token_facebook = $arr['token_facebook'];
				$user->token_twitter = $arr['token_twitter'];
				$user->birth = $arr['birth'];
				
				return $user;
			}
			
			return null;
		}

		public static function findByEmail( $email )
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE email = ? LIMIT 1;',
									  array( $email ) );

							
			return static::fillUser( $result );
		}

		public static function findById($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE uid = ? LIMIT 1;',
									  array( $id ) );

							
			return static::fillUser( $result );
		}

		public static function findByCredentials($email, $pass)
		{
			$user = static::findByEmail( $email );

			if( !is_null( $user ) && !is_null( $user->password ) )
			{
				$exp = explode(':', $user->password, 3);
				if( count( $exp ) === 3 &&
						static::saltPass( $pass , $exp[1] ) === $user->password )
					return $user;
			}

			return null;
		}
		
		public function list_promotions_won() {
		
			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
											
			$sth = $dbh->prepare('SELECT pid, name, active, end_date FROM promotion, user WHERE user.uid = promotion.uid');
			
			$sth->bindParam(':uid', $this->uid, PDO::PARAM_INT);
			$sth->bindParam(':pid', $this->pid, PDO::PARAM_INT);
			$sth->bindParam(':name', $this->name, PDO::PARAM_STR);
			$sth->bindParam(':active', $this->active, PDO::PARAM_STR);
			$sth->bindParam(':end_date', $this->end_date, PDO::PARAM_STR);
			
			$ret = $sth->execute();
			
			if( $ret && is_null($this->uid) )
				$this->uid = $dbh->lastInsertId();

			return $ret;
			
		
		}

	}

?>