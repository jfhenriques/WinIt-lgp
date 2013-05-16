DROP PROCEDURE IF EXISTS proc_avail_prom //
CREATE PROCEDURE proc_avail_prom( uid_in INT )
BEGIN

	DECLARE l_last_row_fetched INT DEFAULT 0;
	DECLARE pp,uu,gg,gt INT;
	#DECLARE msg VARCHAR(200) DEFAULT "";
	
	DECLARE cur1 CURSOR FOR SELECT
					p.pid AS pid,
					p.user_limit AS user_limit,
					p.grand_limit AS grand_limit,
					IFNULL(gt.g_tot, 0) AS g_tot
				FROM promotion AS p
				LEFT JOIN (
					SELECT
						pid,
						count(*) AS g_tot
					FROM userpromotion
					GROUP BY pid
				) AS gt ON (gt.pid = p.pid)
				WHERE active = 1 AND
				( p.init_date = 0 OR p.init_date < UNIX_TIMESTAMP(NOW()) ) AND
				( p.end_date = 0 OR p.end_date >= UNIX_TIMESTAMP(NOW()) )
				GROUP BY p.pid
				HAVING grand_limit = 0 OR g_tot < grand_limit;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET l_last_row_fetched=1;
	
	OPEN cur1;
		 
		WHILE l_last_row_fetched = 0 DO  
			FETCH cur1 INTO pp,uu,gg,gt;  
			IF l_last_row_fetched = 0 THEN

			
			END IF;    
		END WHILE;  
	
	CLOSE cur1;
	
END//

			
			
DROP PROCEDURE IF EXISTS proc_avail_prom //
CREATE PROCEDURE proc_avail_prom( uid_in INT )
BEGIN

	SET @now = UNIX_TIMESTAMP(NOW());
	
	SELECT
		p.pid AS pid,
		p.name AS name,
		p.image AS image,
		p.active AS active,
		p.user_limit AS user_limit,
		p.grand_limit AS grand_limit,
		IFNULL(gt.g_tot, 0) AS g_tot,
		IFNULL(up.times, 0) AS u_tot
	FROM promotion AS p
	LEFT JOIN (
		SELECT
			p.pid AS pid,
			up.uid AS uid,
			count(*) AS times
		FROM promotion AS p
		INNER JOIN userpromotion AS up
		ON (up.pid = p.pid)
		WHERE up.uid = uid_in AND
			p.active = 1 AND
			( p.init_date = 0 OR p.init_date < @now ) AND
			( p.end_date = 0 OR p.end_date >= @now )
		GROUP BY p.pid
	) AS up ON (up.pid = p.pid)
	LEFT JOIN (
		SELECT
			pid,
			count(*) AS g_tot
		FROM userpromotion
		GROUP BY pid
	) AS gt ON (gt.pid = p.pid)
	WHERE p.active = 1 AND
		( p.init_date = 0 OR p.init_date < @now ) AND
		( p.end_date = 0 OR p.end_date >= @now )
	HAVING ( grand_limit = 0 OR g_tot < grand_limit ) AND
		   ( user_limit = 0 OR u_tot < user_limit );

	
END//












DROP TRIGGER IF EXISTS `tg_userpromotion_limit`//
CREATE TRIGGER `tg_userpromotion_limit` BEFORE INSERT ON `userpromotion`
FOR EACH ROW BEGIN

	DECLARE u_lim INT DEFAULT 0;
	DECLARE g_lim INT DEFAULT 0;
	DECLARE tmp INT DEFAULT 0;
	DECLARE tmp2 INT DEFAULT NULL;
	DECLARE act SMALLINT(1) DEFAULT 0;
	DECLARE t_end INT DEFAULT 0;
	DECLARE t_init INT DEFAULT 0;

	SELECT user_limit,grand_limit,active,init_date,end_date
	INTO u_lim,g_lim,act,t_init,t_end
	FROM promotion WHERE pid = NEW.pid LIMIT 1;
	
	SET @now = unix_timestamp(now());

	IF ( act <> 1 OR t_init > @now OR ( t_end > 0 AND t_end < @now ) ) THEN
		signal sqlstate '45000' set message_text = "Promotion inactive, or out of valid timeframe";
	END IF;

	
	IF g_lim > 0 THEN

		SELECT count(*) INTO tmp
		FROM promotion AS p
		INNER JOIN userpromotion AS up
		ON (up.pid = p.pid)
		WHERE p.pid = NEW.pid
		GROUP BY p.pid;
		
		IF tmp >= g_lim THEN
			signal sqlstate '45000' set message_text = "Grand limit reached";
		END IF;
		
	END IF;


	SELECT count(*) INTO tmp2
	FROM userpromotion
	WHERE pid = NEW.pid AND uid = NEW.uid AND (end_date = NULL OR end_date = 0)
	GROUP BY pid;

	IF tmp2 IS NOT NULL AND tmp2 > 0 THEN
		signal sqlstate '45000' set message_text = "You can only have one active participation at one time";
	END IF;


	IF u_lim > 0 THEN

		SELECT count(*) INTO tmp
		FROM userpromotion
		WHERE pid = NEW.pid AND uid = NEW.uid
		GROUP BY pid;

		IF tmp >= u_lim THEN
			signal sqlstate '45000' set message_text = "User limit reached";
		END IF;
		
	END IF;
	
END//







#"Promotion has expired or is inactive";
#DECLARE msg VARCHAR(200) DEFAULT "";
#SET msg = concat("tot: ", cast(tmp AS char));


SELECT
	*
FROM promotions AS p
LEFT JOIN (
	SELECT
		pid,
		count(*) AS g_tot
	FROM userpromotion
	GROUP BY pid
) AS gt ON (gt.pid = p.pid)
LEFT JOIN (
	SELECT
		p.pid AS pid,
		count(*) AS u_tot
	FROM promotion AS p
	INNER JOIN userpromotion AS up
	ON (up.pid = p.pid)
	WHERE
		up.uid = 1
		AND
		p.active = 1 AND ( p.end_time = 0 OR p.end_time < 123 )
	GROUP by p.pid
) AS ut ON (gt.pid = p.pid)
WHERE
	p.active = 1 AND ( p.end_time = 0 OR p.end_time < 123 )
	
	
	
	
SELECT
	p.pid AS pid,
	IFNULL(gt.g_tot, 0) AS g_tot
FROM promotion AS p
LEFT JOIN (
	SELECT
		pid,
		count(*) AS g_tot
	FROM userpromotion
	GROUP BY pid
) AS gt ON (gt.pid = p.pid)
LEFT JOIN userpromotion AS up ON (up.pid = p.pid)
GROUP BY p.pid
