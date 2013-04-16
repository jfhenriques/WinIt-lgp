<?php
	
	class User extends ActiveRecord {

		private $uid = null;
		private $name;
		private $email;
		private $password;
		private $adid;
		private $door;
		private $token_facebook;
		private $token_twitter;
		

		const TABLE_NAME = 'user' ;


		//public function __construct() {}

		
		/* GET's and SET's*/

		public function getID()
		{
			return $this->uid;
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
			return $this->adid;
		}
		public function setADID($adid)
		{
			$this->adid = $adid;
		}

		
		public function getDoor()
		{
			return $this->door;
		}
		public function setDoor($door)
		{
			$this->door = $door;
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



		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			if( is_null($this->uid) )
				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (name, email, password, adid, door, token_facebook, token_twitter) ' .
										' VALUES(:name, :email, :password, :adid, :door, :token_facebook, :token_twitter)');
			else
			{
				$sth = $dbh->prepare('UPDATE ' . self::TABLE_NAME . ' SET name = :name , email = :email, password = :password,' .
												' adid = :adid, door = :door, token_facebook = :token_facebook,' .
												' token_twitter = :token_twitter WHERE uid = :uid ;' );
				
				$sth->bindParam(':uid', $this->uid, PDO::PARAM_INT);
			}

			$sth->bindParam(':name', $this->name, PDO::PARAM_STR);
			$sth->bindParam(':email', $this->email, PDO::PARAM_STR);
			$sth->bindParam(':password', $this->password, PDO::PARAM_STR);
			$sth->bindParam(':adid', $this->adid, PDO::PARAM_INT);
			$sth->bindParam(':door', $this->door, PDO::PARAM_STR);
			$sth->bindParam(':token_facebook', $this->token_facebook, PDO::PARAM_STR);
			$sth->bindParam(':token_twitter', $this->token_twitter, PDO::PARAM_STR);
			
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
				$user->door = $arr['door'];
				$user->token_facebook = $arr['token_facebook'];
				$user->token_twitter = $arr['token_twitter'];
				
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

	}

?>