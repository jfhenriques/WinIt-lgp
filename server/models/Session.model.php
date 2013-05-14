<?php
	
	
	
	class Session
		extends ActiveRecord
		implements SavableActiveRecord {
		
		const TABLE_NAME = 'session' ;
		
		

		public function getToken()
		{
			return $this->getData('token');
		}
		public function setToken($token)
		{
			$this->data['token'] = $token;
		}
		
		
		public function getUID()
		{
			return (int)$this->getData('uid');
		}
		public function setUID($userId)
		{
			$this->data['uid'] = (int)$userId;
		}
		

		public function getValidity()
		{
			return (int)$this->getData('validity');
		}
		public function setValidity($validity)
		{
			$this->data['validity'] = (int)$validity;
		}
		
		
		public function deleteCache()
		{
			CommonCache::getInstance()->delete( CommonCache::buildVarName( self::TABLE_NAME, $this->getToken() ) );
		}
		
		public function save()
		{
			$dbh = DbConn::getInstance()->getDB();
			
			$sth = $dbh->prepare('INSERT INTO ' . self::TABLE_NAME . ' (token, uid, validity) VALUES(:tok, :uid, :val) ON DUPLICATE KEY UPDATE validity = :val ;');	
			
			$tok = $this->getToken() ;
			$uid = $this->getUID() ;
			$val = $this->getValidity() ;

			$sth->bindParam(':tok', $tok, PDO::PARAM_STR);
			$sth->bindParam(':uid', $uid, PDO::PARAM_INT);
			$sth->bindParam(':val', $val, PDO::PARAM_INT);

			// If this instance was cached, force its deletetion, so the next cache miss forces it to reload
			$this->deleteCache();

			return $sth->execute();
		}


		public static function resetUserTokens($uid)
		{
			$return = static::executeQuery( 'SELECT * FROM '. self::TABLE_NAME .
											' WHERE uid = ? AND validity >= 0;',
									  			array( $uid ), $stmt );

			if( $stmt !== null && $return !== false )
			{
				while( $row = $stmt->fetch() )
				{
					if( !is_null( $sess = static::fillModel( $row, new Session() ) ) )
					{
						$sess->setValidity(-1);
						$sess->save();
					}
				}

			}
		}

		public static function findByToken($token)
		{
			$result = static::cachedQuery( $token,
											self::TABLE_NAME,
											'SELECT * FROM '. self::TABLE_NAME . ' WHERE token = ? LIMIT 1;',
											array( $token ),
											function( $arr ) {
												return $arr['validity'] >= 0 &&
															( TOKEN_VALIDITY == 0 || $arr['validity'] >= time() ) ;
											});
				
			return static::fillModel( $result, new Session() );
		}
		

		public static function findByUID($id)
		{
			$result = static::query( 'SELECT * FROM '. self::TABLE_NAME .
											' WHERE uid = ? AND validity >= 0 AND ( ? = \'0\' OR validity >= ? ) ORDER BY validity DESC LIMIT 1;',
									  array( $id, TOKEN_VALIDITY, time() + 60 ) );

							
			return static::fillModel( $result, new Session() );
		}
	}
?>