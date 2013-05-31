<?php
	
	class User
		extends ActiveRecord
		implements SavableActiveRecord {
		

		const TABLE_NAME = 'user' ;


		const KEY_USER_POINTS = "sql.userwithpoints";


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
			$this->data['adid'] = (int)$adid;
		}

		
		public function getAddress2()
		{
			return $this->getData('address2');
		}
		public function setAddress2($address2)
		{
			$this->data['address2'] = $address2;
		}
		

		public function getFacebookUID()
		{
			return $this->getData('facebook_uid');
		}
		public function setFacebookUID($facebook_uid)
		{
			$this->data['facebook_uid'] = $facebook_uid;
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
			$this->data['birth'] = (int)$birth;
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

		public function isActive()
		{
			return (bool)$this->getData('active');
		}
		public function setActive($state)
		{
			$this->data['active'] = $state;
		}



		/* --- ONLY AVAILABLE WITH User::findByUIDWithPoints( UID ) --- */

			public function getTotalPoints()
			{
				return (int)$this->getData('points');
			}

		/* --- */





		public function save()
		{

			$dbh = DbConn::getInstance()->getDB();
			$sth = null;
			
			$uid = $this->getUID();
			$isInsert = ( $uid <= 0 ) ;

			if( $isInsert )
			{
				$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (name, email, password, adid, address2, facebook_uid, birth, ui_seed) ' .
										' VALUES(:name, :email, :password, :adid, :address2, :facebook_uid, :birth, :seed)');
				
				$seed = $this->getSeed();
				$facebook_uid = $this->getFacebookUID();

				$sth->bindParam(':seed', $seed, PDO::PARAM_LOB );
				$sth->bindParam(':facebook_uid', $facebook_uid, is_null( $facebook_uid ) ?  PDO::PARAM_NULL : PDO::PARAM_INT );
			}
			else
			{
				$sth = $dbh->prepare('UPDATE ' . self::TABLE_NAME . ' SET name = :name , email = :email, password = :password,  adid = :adid, address2 = :address2, ' .
												' birth = :birth, active = :active, reset_token = :res_token , reset_token_validity = :res_token_val WHERE uid = :uid ;' );
				
				$res_token = $this->getResetToken();
				$res_token_val = $this->getResetTokenValidity();
				$active = $this->isActive() ? 1 : 0 ;

				$sth->bindParam(':uid', $uid, PDO::PARAM_INT);
				$sth->bindParam(':active', $active , PDO::PARAM_INT);
				$sth->bindParam(':res_token', $res_token, PDO::PARAM_STR);
				$sth->bindParam(':res_token_val', $res_token_val, PDO::PARAM_INT);
			}

			$name = $this->getName();
			$email = $this->getEmail();
			$password = $this->getPassword();
			$address2 = $this->getAddress2();
			$birth = $this->getBirth();

			$adid = $this->getADID();
			if( $adid <= 0 )
				$adid = NULL;

			$sth->bindParam(':name', $name, PDO::PARAM_STR);
			$sth->bindParam(':email', $email, is_null( $email ) ? PDO::PARAM_NULL : PDO::PARAM_STR );
			$sth->bindParam(':password', $password, is_null( $password ) ? PDO::PARAM_NULL : PDO::PARAM_STR );
			$sth->bindParam(':adid', $adid, ( $adid > 0 ) ? PDO::PARAM_INT : PDO::PARAM_NULL );
			$sth->bindParam(':address2', $address2, PDO::PARAM_STR);
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
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE active = 1 AND facebook_uid = NULL AND reset_token = ? LIMIT 1;',
									  array( $token ) );

							
			return static::fillModel( $result, new User() );
		}

		public static function findByEmail( $email )
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE active = 1 AND email = ? LIMIT 1;',
									  array( $email ) );

							
			return static::fillModel( $result, new User() );
		}

		public static function findByUID($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE active = 1 AND uid = ? LIMIT 1;',
									  array( $id ) );

			return static::fillModel( $result, new User() );
		}

		public static function findByUIDWithPoints($id)
		{
			$cc = CommonCache::getInstance();
			$sql = $cc->get( self::KEY_USER_POINTS );

			if( $sql === false )
			{
				$sql =  'SELECT u.uid AS uid, u.active AS active, u.name AS name, u.email AS email, ' .
						' u.password AS password, u.birth AS birth, u.adid AS adid, u.address2 AS address2, ' .
						' u.facebook_uid AS facebook_uid, u.reset_token AS reset_token, u.ui_seed AS ui_seed, ' .
						' u.reset_token_validity AS reset_token_validity, IFNULL(SUM(xp_points), 0) AS points FROM '. self::TABLE_NAME .
						' AS u LEFT JOIN xppoints AS xp ON (xp.uid = u.uid) WHERE u.active = 1 AND u.uid = ? LIMIT 1; ' ;

				$cc->set( self::KEY_USER_POINTS, $sql );
			}

			$result = static::query( $sql, array( $id ) );

			return static::fillModel( $result, new User() );
		}

		public static function findByFacebookUID($facebook_uid)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME . ' WHERE facebook_uid = ? LIMIT 1;',
									  array( $facebook_uid ) );

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

			if(   !is_null( $user ) && !is_null( $user->getPassword() )
				&& self::compareWithHashedPass( $pass, $user->getPassword() ) !== false )
				return $user;

			return null;
		}

	}

?>