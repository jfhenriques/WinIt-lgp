<?php
	
	class User
		extends ActiveRecord
		implements SavableActiveRecord {

		// private $uid = null;
		// private $name;
		// private $email;
		// private $password;
		// private $adid;
		// private $birth = 0;
		// private $address2;
		// private $token_facebook;
		// private $token_twitter;
		

		const TABLE_NAME = 'user' ;


		//public function __construct() {}

		
		/* GET's and SET's*/

		public function getUID()
		{
			return (int)$this->getData('uid');
		}
		

		public function getName()
		{
			return $this->getData('name');
		}		
		public function setName($name)
		{
			$this->data['name'] = $name;
		}
		

		public function getEmail()
		{
			return $this->getData('email');
		}
		public function setEmail($email)
		{
			$this->data['email'] = $email;
		}
		

		public function getPassword()
		{
			return $this->getData('password');
		}
		public function setPassword($password)
		{
			$this->data['password'] = $password;
		}
		

		public function getADID()
		{
			return (int)$this->getData('adid');
		}
		public function setADID($adid)
		{
			$this->data['adid'] = $adid;
		}

		
		public function getAddress2()
		{
			return $this->getData('address2');
		}
		public function setAddress2($address2)
		{
			$this->data['address2'] = $address2;
		}

		
		public function getTokenFacebook()
		{
			return $this->getData('token_facebook');
		}
		public function setTokenFacebook($token_facebook)
		{
			$this->data['token_facebook'] = $token_facebook;
		}
		

		public function getTokenTwitter()
		{
			return $this->getData('token_twitter');
		}
		public function setTokenTwitter($token_twitter)
		{
			$this->data['token_twitter'] = $token_twitter;
		}

		public function getBirth()
		{
			return (int)$this->getData('birth');
		}
		public function setBirth($birth)
		{
			$this->data['birth'] = $birth;
		}

		public function getResetToken()
		{
			return $this->getData('reset_token');
		}
		public function setResetToken($token)
		{
			$this->data['reset_token'] = $token;
		}

		public function getResetTokenValidity()
		{
			return (int)$this->getData('reset_token_validity');
		}
		public function setResetTokenValidity($validity)
		{
			$this->data['reset_token_validity'] = $validity;
		}

		public function getSeed()
		{
			return $this->getData('ui_seed');
		}
		public function setSeed($seed)
		{
			$this->data['ui_seed'] = $seed;
		}





		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			$uid = $this->getUID();
			$isInsert = ( $uid <= 0 ) ;

			if( $isInsert )
			{
				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (name, email, password, adid, door, token_facebook, token_twitter, birth, ui_seed) ' .
										' VALUES(:name, :email, :password, :adid, :address2, :token_facebook, :token_twitter, :birth, :seed)');
				
				$seed = $this->getSeed();
				$sth->bindParam(':seed', $seed, PDO::PARAM_LOB );
			}
			else
			{
				$sth = $dbh->prepare('UPDATE ' . self::TABLE_NAME . ' SET name = :name , email = :email, password = :password,' .
												' adid = :adid, door = :address2, birth = :birth, token_facebook = :token_facebook,' .
												' token_twitter = :token_twitter , reset_token = :res_token , reset_token_validity = :res_token_val WHERE uid = :uid ;' );
				
				$res_token = $this->getResetToken();
				$res_token_val = $this->getResetTokenValidity();

				$sth->bindParam(':uid', $uid, PDO::PARAM_INT);
				$sth->bindParam(':res_token', $res_token, PDO::PARAM_STR);
				$sth->bindParam(':res_token_val', $res_token_val, PDO::PARAM_INT);
			}

			$name = $this->getName();
			$email = $this->getEmail();
			$password = $this->getPassword();
			$adid = $this->getADID();
			$address2 = $this->getAddress2();
			$token_facebook = $this->getTokenFacebook();
			$token_twitter = $this->getTokenTwitter();
			$birth = $this->getBirth();

			$sth->bindParam(':name', $name, PDO::PARAM_STR);
			$sth->bindParam(':email', $email, PDO::PARAM_STR);
			$sth->bindParam(':password', $password, PDO::PARAM_STR);
			$sth->bindParam(':adid', $adid, PDO::PARAM_INT);
			$sth->bindParam(':address2', $address2, PDO::PARAM_STR);
			$sth->bindParam(':token_facebook', $token_facebook, PDO::PARAM_STR);
			$sth->bindParam(':token_twitter', $token_twitter, PDO::PARAM_STR);
			$sth->bindParam(':birth', $birth, PDO::PARAM_INT);
			
			$ret = $sth->execute();
			
			if( $ret && $isInsert )
				$this->data['uid'] = $dbh->lastInsertId();

			return $ret;
		}





		public static function saltPass( $pass, $salt = null )
		{
			$salt = is_null($salt) ? md5(uniqid(mt_rand(), true)) : $salt ;
			return ":${salt}:" . hash( 'sha256', $salt.$pass ) . ':';
		}

		public static function findByResetToken( $token )
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE reset_token = ? LIMIT 1;',
									  array( $token ) );

							
			return static::fillModel( $result, new User() );
		}

		public static function findByEmail( $email )
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE email = ? LIMIT 1;',
									  array( $email ) );

							
			return static::fillModel( $result, new User() );
		}

		public static function findByUID($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE uid = ? LIMIT 1;',
									  array( $id ) );

			return static::fillModel( $result, new User() );
		}

		public static function compareWithHashedPass($newPass, $oldHashedPass)
		{
			if( is_null( $newPass ) || is_null( $oldHashedPass ) )
				return false;

			$exp = explode(':', $oldHashedPass, 3);

			return ( count( $exp ) === 3 &&
						static::saltPass( $newPass , $exp[1] ) === $oldHashedPass ) ;
		}

		public static function findByCredentials($email, $pass)
		{
			$user = static::findByEmail( $email );

			if( !is_null( $user ) && self::compareWithHashedPass( $pass, $user->getPassword() ) !== false )
				return $user;

			return null;
		}
		
		public function list_promotions_won() {
		
/*			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			$userID = $this->getUID();
		
			$sth = $dbh->prepare('SELECT promotion.pid, promotion.name, promotion.active, promotion.end_date '.
									'FROM promotion, user, userpromotion '.
									'WHERE user.uid = userpromotion.uid '.
									'AND promotion.pid = userpromotion.pid '.
									'AND user.uid = ? ;' );
			
			$ret = $sth->execute(array());
			
			$result = $sth->fetchAll();
			
			// var_dump($result);

			return $result;*/
			
		
		}
		
		
		public function list_badges_won() {
			
			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			$userID = $this->getUID();
			
			$sth = $dbh->prepare('SELECT b.bid AS bid, b.name AS bname, b.image AS bimage, b.description AS description, ub.aquis_date AS bdata'.
										'FROM badges AS b '.
										'INNER JOIN userbadges AS ub ON (ub.bid = b.bid) '.
										'INNER JOIN user AS u ON (u.uid = ub.uid) '.
										'WHERE u.uid = '.$userID.' ;');
			
			$ret = $sth->execute(array());
			
			$result = $sth->fetchAll();
			
			// var_dump($result);

			return $result;			
		}

	}

?>