-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 23, 2013 at 10:50 AM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT=0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `tlantic`
--

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `proc_avail_prom`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `proc_avail_prom`( uid_in INT )
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

  
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `email` varchar(300) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`aid`),
  UNIQUE KEY `uq_admin_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `badges`
--

DROP TABLE IF EXISTS `badges`;
CREATE TABLE IF NOT EXISTS `badges` (
  `bid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `image` varchar(300) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
CREATE TABLE IF NOT EXISTS `item` (
  `iid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `image` varchar(300) NOT NULL,
  PRIMARY KEY (`iid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `itempromotion`
--

DROP TABLE IF EXISTS `itempromotion`;
CREATE TABLE IF NOT EXISTS `itempromotion` (
  `iid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `percent` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`iid`,`pid`),
  KEY `i_itempromotion_iid` (`iid`),
  KEY `i_itempromotion_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `prizecode`
--

DROP TABLE IF EXISTS `prizecode`;
CREATE TABLE IF NOT EXISTS `prizecode` (
  `pcid` int(11) NOT NULL AUTO_INCREMENT,
  `emiss_date` int(11) NOT NULL,
  `util_date` int(11) NOT NULL DEFAULT '0',
  `cur_uid` int(11) NOT NULL,
  `valid_code` varchar(100) NOT NULL,
  `in_trading` smallint(1) NOT NULL DEFAULT '0',
  `transaction` int(11) NOT NULL DEFAULT '0',
  `upid` int(11) NOT NULL,
  PRIMARY KEY (`pcid`),
  UNIQUE KEY `valid_code` (`valid_code`),
  UNIQUE KEY `i_prizecode_upid` (`upid`),
  KEY `i_prizecode_cur_uid` (`cur_uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
CREATE TABLE IF NOT EXISTS `promotion` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `active` smallint(1) NOT NULL DEFAULT '1',
  `name` varchar(200) NOT NULL,
  `desc` text,
  `image` varchar(200) DEFAULT NULL,
  `init_date` int(11) NOT NULL DEFAULT '0',
  `end_date` int(11) NOT NULL DEFAULT '0',
  `util_date` int(11) NOT NULL DEFAULT '0',
  `grand_limit` int(11) NOT NULL DEFAULT '0',
  `user_limit` int(11) NOT NULL DEFAULT '1',
  `valid_coord` varchar(200) DEFAULT NULL,
  `valid_coord_radius` int(11) DEFAULT NULL,
  `transferable` smallint(1) NOT NULL DEFAULT '0',
  `win_points` int(11) NOT NULL DEFAULT '0',
  `func_type` smallint(2) NOT NULL,
  `rid` int(11) NOT NULL,
  `ptid` int(11) NOT NULL,
  PRIMARY KEY (`pid`),
  KEY `i_promotion_rid` (`rid`),
  KEY `i_promotion_ptid` (`ptid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `promotionbuy`
--

DROP TABLE IF EXISTS `promotionbuy`;
CREATE TABLE IF NOT EXISTS `promotionbuy` (
  `pid` int(11) NOT NULL,
  `xp_cost` int(11) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `promotiontags`
--

DROP TABLE IF EXISTS `promotiontags`;
CREATE TABLE IF NOT EXISTS `promotiontags` (
  `pid` int(11) NOT NULL,
  `tid` int(11) NOT NULL,
  PRIMARY KEY (`pid`,`tid`),
  KEY `i_promotiontags_pid` (`pid`),
  KEY `i_promotiontags_tid` (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `promotiontype`
--

DROP TABLE IF EXISTS `promotiontype`;
CREATE TABLE IF NOT EXISTS `promotiontype` (
  `ptid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`ptid`),
  UNIQUE KEY `uq_promotiontype_name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `proximityalert`
--

DROP TABLE IF EXISTS `proximityalert`;
CREATE TABLE IF NOT EXISTS `proximityalert` (
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `qganswer`
--

DROP TABLE IF EXISTS `qganswer`;
CREATE TABLE IF NOT EXISTS `qganswer` (
  `qid` int(11) NOT NULL,
  `upid` int(11) NOT NULL,
  `answer` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`qid`,`upid`),
  KEY `i_qganswer_qid` (`qid`),
  KEY `i_qganswer_upid` (`upid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `qgquestion`
--

DROP TABLE IF EXISTS `qgquestion`;
CREATE TABLE IF NOT EXISTS `qgquestion` (
  `qid` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(200) NOT NULL,
  `question_type` smallint(1) NOT NULL,
  `answer_choices` varchar(200) DEFAULT NULL,
  `expected_answer` varchar(200) DEFAULT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`qid`),
  KEY `i_qgquestion_pid` (`pid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `quizgame`
--

DROP TABLE IF EXISTS `quizgame`;
CREATE TABLE IF NOT EXISTS `quizgame` (
  `pid` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `is_quiz` smallint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `raffle`
--

DROP TABLE IF EXISTS `raffle`;
CREATE TABLE IF NOT EXISTS `raffle` (
  `pid` int(11) NOT NULL,
  `xp_cost` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `rafflebuyin`
--

DROP TABLE IF EXISTS `rafflebuyin`;
CREATE TABLE IF NOT EXISTS `rafflebuyin` (
  `pid` int(11) NOT NULL,
  `upid` int(11) NOT NULL,
  `state` tinyint(1) NOT NULL DEFAULT '0',
  `date` int(11) NOT NULL,
  PRIMARY KEY (`pid`,`upid`),
  KEY `i_sortbuyin_pid` (`pid`),
  KEY `i_sortbuyin_upid` (`upid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `retailer`
--

DROP TABLE IF EXISTS `retailer`;
CREATE TABLE IF NOT EXISTS `retailer` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `image` varchar(200) DEFAULT NULL,
  `nif` int(11) DEFAULT NULL,
  `email` varchar(300) NOT NULL,
  `password` varchar(100) NOT NULL,
  `adid` int(11) DEFAULT NULL,
  `door` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `i_retailer_adid` (`adid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `session`
--

DROP TABLE IF EXISTS `session`;
CREATE TABLE IF NOT EXISTS `session` (
  `token` varchar(100) NOT NULL,
  `uid` int(11) NOT NULL,
  `validity` int(11) NOT NULL,
  PRIMARY KEY (`token`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
CREATE TABLE IF NOT EXISTS `tag` (
  `tid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`tid`),
  UNIQUE KEY `uq_tag_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tradingsuggestion`
--

DROP TABLE IF EXISTS `tradingsuggestion`;
CREATE TABLE IF NOT EXISTS `tradingsuggestion` (
  `pcid_orig` int(11) NOT NULL,
  `pcid_dest` int(11) NOT NULL,
  `date` int(11) NOT NULL,
  `transaction` int(11) NOT NULL,
  `state` smallint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pcid_orig`,`pcid_dest`,`transaction`),
  KEY `i_tradingsuggestion_pcid_orig` (`pcid_orig`),
  KEY `i_tradingsuggestion_pcid_dest` (`pcid_dest`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `email` varchar(300) NOT NULL,
  `password` varchar(100) NOT NULL,
  `birth` int(11) DEFAULT NULL,
  `adid` int(11) DEFAULT NULL,
  `door` varchar(200) DEFAULT NULL,
  `token_facebook` varchar(200) DEFAULT NULL,
  `token_twitter` varchar(200) DEFAULT NULL,
  `reset_token` varchar(150) DEFAULT NULL,
  `reset_token_validity` int(11) DEFAULT NULL,
  `ui_seed` binary(32) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uq_user_email` (`email`),
  UNIQUE KEY `reset_token` (`reset_token`),
  KEY `i_user_adid` (`adid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `userbadges`
--

DROP TABLE IF EXISTS `userbadges`;
CREATE TABLE IF NOT EXISTS `userbadges` (
  `uid` int(11) NOT NULL,
  `bid` int(11) NOT NULL,
  `aquis_date` int(11) NOT NULL,
  PRIMARY KEY (`uid`,`bid`),
  KEY `i_userbadges_uid` (`uid`),
  KEY `i_userbadges_bid` (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `userpromotion`
--

DROP TABLE IF EXISTS `userpromotion`;
CREATE TABLE IF NOT EXISTS `userpromotion` (
  `upid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `init_date` int(11) DEFAULT NULL,
  `end_date` int(11) DEFAULT NULL,
  `state` smallint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`upid`),
  KEY `i_userpromotion_uid` (`uid`),
  KEY `i_userpromotion_pid` (`pid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Triggers `userpromotion`
--
DROP TRIGGER IF EXISTS `tg_userpromotion_limit`;
DELIMITER //
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
  
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `usertags`
--

DROP TABLE IF EXISTS `usertags`;
CREATE TABLE IF NOT EXISTS `usertags` (
  `uid` int(11) NOT NULL,
  `tid` int(11) NOT NULL,
  PRIMARY KEY (`uid`,`tid`),
  KEY `i_usertags_uid` (`uid`),
  KEY `i_usertags_tid` (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `xppoints`
--

DROP TABLE IF EXISTS `xppoints`;
CREATE TABLE IF NOT EXISTS `xppoints` (
  `uid` int(11) NOT NULL,
  `aquis_date` int(11) NOT NULL,
  `xp_points` int(11) NOT NULL DEFAULT '0',
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`,`aquis_date`),
  KEY `i_xppoints_uid` (`uid`),
  KEY `i_xppoints_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `itempromotion`
--
ALTER TABLE `itempromotion`
  ADD CONSTRAINT `itempromotion_ibfk_1` FOREIGN KEY (`iid`) REFERENCES `item` (`iid`),
  ADD CONSTRAINT `itempromotion_ibfk_2` FOREIGN KEY (`pid`) REFERENCES `promotion` (`pid`);

--
-- Constraints for table `prizecode`
--
ALTER TABLE `prizecode`
  ADD CONSTRAINT `prizecode_ibfk_1` FOREIGN KEY (`cur_uid`) REFERENCES `user` (`uid`),
  ADD CONSTRAINT `prizecode_ibfk_2` FOREIGN KEY (`upid`) REFERENCES `userpromotion` (`upid`);

--
-- Constraints for table `promotion`
--
ALTER TABLE `promotion`
  ADD CONSTRAINT `promotion_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `retailer` (`rid`),
  ADD CONSTRAINT `promotion_ibfk_2` FOREIGN KEY (`ptid`) REFERENCES `promotiontype` (`ptid`);

--
-- Constraints for table `promotiontags`
--
ALTER TABLE `promotiontags`
  ADD CONSTRAINT `promotiontags_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `promotion` (`pid`),
  ADD CONSTRAINT `promotiontags_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `tag` (`tid`);

--
-- Constraints for table `proximityalert`
--
ALTER TABLE `proximityalert`
  ADD CONSTRAINT `proximityalert_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `promotion` (`pid`);

--
-- Constraints for table `qganswer`
--
ALTER TABLE `qganswer`
  ADD CONSTRAINT `qganswer_ibfk_1` FOREIGN KEY (`qid`) REFERENCES `qgquestion` (`qid`),
  ADD CONSTRAINT `qganswer_ibfk_2` FOREIGN KEY (`upid`) REFERENCES `userpromotion` (`upid`);

--
-- Constraints for table `qgquestion`
--
ALTER TABLE `qgquestion`
  ADD CONSTRAINT `qgquestion_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `quizgame` (`pid`);

--
-- Constraints for table `quizgame`
--
ALTER TABLE `quizgame`
  ADD CONSTRAINT `quizgame_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `promotion` (`pid`);

--
-- Constraints for table `raffle`
--
ALTER TABLE `raffle`
  ADD CONSTRAINT `raffle_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `promotion` (`pid`);

--
-- Constraints for table `rafflebuyin`
--
ALTER TABLE `rafflebuyin`
  ADD CONSTRAINT `rafflebuyin_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `raffle` (`pid`),
  ADD CONSTRAINT `rafflebuyin_ibfk_2` FOREIGN KEY (`upid`) REFERENCES `userpromotion` (`upid`);

--
-- Constraints for table `retailer`
--
ALTER TABLE `retailer`
  ADD CONSTRAINT `retailer_ibfk_1` FOREIGN KEY (`adid`) REFERENCES `address` (`adid`);

--
-- Constraints for table `session`
--
ALTER TABLE `session`
  ADD CONSTRAINT `session_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);

--
-- Constraints for table `tradingsuggestion`
--
ALTER TABLE `tradingsuggestion`
  ADD CONSTRAINT `tradingsuggestion_ibfk_1` FOREIGN KEY (`pcid_orig`) REFERENCES `prizecode` (`pcid`),
  ADD CONSTRAINT `tradingsuggestion_ibfk_2` FOREIGN KEY (`pcid_dest`) REFERENCES `prizecode` (`pcid`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`adid`) REFERENCES `address` (`adid`);

--
-- Constraints for table `userbadges`
--
ALTER TABLE `userbadges`
  ADD CONSTRAINT `userbadges_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`),
  ADD CONSTRAINT `userbadges_ibfk_2` FOREIGN KEY (`bid`) REFERENCES `badges` (`bid`);

--
-- Constraints for table `userpromotion`
--
ALTER TABLE `userpromotion`
  ADD CONSTRAINT `userpromotion_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`),
  ADD CONSTRAINT `userpromotion_ibfk_2` FOREIGN KEY (`pid`) REFERENCES `promotion` (`pid`);

--
-- Constraints for table `usertags`
--
ALTER TABLE `usertags`
  ADD CONSTRAINT `usertags_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`),
  ADD CONSTRAINT `usertags_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `tag` (`tid`);

--
-- Constraints for table `xppoints`
--
ALTER TABLE `xppoints`
  ADD CONSTRAINT `xppoints_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`),
  ADD CONSTRAINT `xppoints_ibfk_2` FOREIGN KEY (`pid`) REFERENCES `promotion` (`pid`);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
