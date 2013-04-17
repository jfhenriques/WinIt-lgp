-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 17, 2013 at 01:38 AM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT=0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `tlantic`
--

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
CREATE TABLE IF NOT EXISTS `address` (
  `adid` int(11) NOT NULL AUTO_INCREMENT,
  `cp4` int(4) NOT NULL,
  `cp3` int(3) NOT NULL,
  `stid` int(11) NOT NULL,
  `llll` int(6) NOT NULL,
  PRIMARY KEY (`adid`),
  UNIQUE KEY `uq_moradas_cp4_cp3_llll_stid` (`cp4`,`cp3`,`stid`,`llll`),
  KEY `i_address_cp4` (`cp4`),
  KEY `i_address_cp3` (`cp3`),
  KEY `i_address_llll` (`llll`),
  KEY `i_address_stid` (`stid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=311519 ;

--
-- RELATIONS FOR TABLE `address`:
--   `stid`
--       `addrstreet` -> `stid`
--   `llll`
--       `cplocality` -> `llll`
--

-- --------------------------------------------------------

--
-- Table structure for table `addrstreet`
--

DROP TABLE IF EXISTS `addrstreet`;
CREATE TABLE IF NOT EXISTS `addrstreet` (
  `stid` int(11) NOT NULL AUTO_INCREMENT,
  `street` varchar(300) NOT NULL,
  PRIMARY KEY (`stid`),
  UNIQUE KEY `I_addrstreet_street` (`street`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=155221 ;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `cpdistrict`
--

DROP TABLE IF EXISTS `cpdistrict`;
CREATE TABLE IF NOT EXISTS `cpdistrict` (
  `dd` int(3) NOT NULL,
  `district` varchar(100) NOT NULL,
  PRIMARY KEY (`dd`),
  UNIQUE KEY `uq_cpdistricts_distrito` (`district`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `cplocality`
--

DROP TABLE IF EXISTS `cplocality`;
CREATE TABLE IF NOT EXISTS `cplocality` (
  `llll` int(6) NOT NULL,
  `dd` int(3) NOT NULL,
  `locality` varchar(100) NOT NULL,
  PRIMARY KEY (`llll`),
  KEY `i_address_dd` (`dd`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `cplocality`:
--   `dd`
--       `cpdistrict` -> `dd`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `itempromotion`
--

DROP TABLE IF EXISTS `itempromotion`;
CREATE TABLE IF NOT EXISTS `itempromotion` (
  `iid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`iid`,`pid`),
  KEY `i_itempromotion_iid` (`iid`),
  KEY `i_itempromotion_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `itempromotion`:
--   `iid`
--       `item` -> `iid`
--   `pid`
--       `promotion` -> `pid`
--

-- --------------------------------------------------------

--
-- Table structure for table `prizecode`
--

DROP TABLE IF EXISTS `prizecode`;
CREATE TABLE IF NOT EXISTS `prizecode` (
  `pcid` int(11) NOT NULL AUTO_INCREMENT,
  `emiss_date` int(11) NOT NULL,
  `util_date` int(11) DEFAULT NULL,
  `cur_uid` int(11) NOT NULL,
  `valid_code` varchar(100) NOT NULL,
  `in_trading` smallint(1) NOT NULL DEFAULT '0',
  `upid` int(11) NOT NULL,
  PRIMARY KEY (`pcid`),
  KEY `i_prizecode_cur_uid` (`cur_uid`),
  KEY `i_prizecode_upid` (`upid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- RELATIONS FOR TABLE `prizecode`:
--   `cur_uid`
--       `user` -> `uid`
--   `upid`
--       `userpromotion` -> `upid`
--

-- --------------------------------------------------------

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
CREATE TABLE IF NOT EXISTS `promotion` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `active` smallint(11) NOT NULL DEFAULT '1',
  `name` varchar(200) NOT NULL,
  `init_date` int(11) NOT NULL,
  `end_date` int(11) NOT NULL,
  `grand_limit` int(11) NOT NULL DEFAULT '0',
  `user_imit` int(11) NOT NULL DEFAULT '1',
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- RELATIONS FOR TABLE `promotion`:
--   `rid`
--       `retailer` -> `rid`
--   `ptid`
--       `promotiontype` -> `ptid`
--

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

--
-- RELATIONS FOR TABLE `promotiontags`:
--   `pid`
--       `promotion` -> `pid`
--   `tid`
--       `tag` -> `tid`
--

-- --------------------------------------------------------

--
-- Table structure for table `promotiontype`
--

DROP TABLE IF EXISTS `promotiontype`;
CREATE TABLE IF NOT EXISTS `promotiontype` (
  `ptid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `percent` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`ptid`),
  UNIQUE KEY `uq_promotiontype_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `proximityalert`
--

DROP TABLE IF EXISTS `proximityalert`;
CREATE TABLE IF NOT EXISTS `proximityalert` (
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `proximityalert`:
--   `pid`
--       `promotion` -> `pid`
--

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

--
-- RELATIONS FOR TABLE `qganswer`:
--   `qid`
--       `qgquestion` -> `qid`
--   `upid`
--       `userpromotion` -> `upid`
--

-- --------------------------------------------------------

--
-- Table structure for table `qgquestion`
--

DROP TABLE IF EXISTS `qgquestion`;
CREATE TABLE IF NOT EXISTS `qgquestion` (
  `qid` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(200) NOT NULL,
  `answer_type` smallint(1) NOT NULL,
  `answer_choices` varchar(200) DEFAULT NULL,
  `expected_answer` varchar(200) DEFAULT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`qid`),
  KEY `i_qgquestion_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- RELATIONS FOR TABLE `qgquestion`:
--   `pid`
--       `quizgame` -> `pid`
--

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

--
-- RELATIONS FOR TABLE `quizgame`:
--   `pid`
--       `promotion` -> `pid`
--

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

--
-- RELATIONS FOR TABLE `raffle`:
--   `pid`
--       `promotion` -> `pid`
--

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

--
-- RELATIONS FOR TABLE `rafflebuyin`:
--   `pid`
--       `raffle` -> `pid`
--   `upid`
--       `userpromotion` -> `upid`
--

-- --------------------------------------------------------

--
-- Table structure for table `retailer`
--

DROP TABLE IF EXISTS `retailer`;
CREATE TABLE IF NOT EXISTS `retailer` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `nif` int(11) DEFAULT NULL,
  `email` varchar(300) NOT NULL,
  `password` varchar(100) NOT NULL,
  `adid` int(11) DEFAULT NULL,
  `door` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `i_retailer_adid` (`adid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- RELATIONS FOR TABLE `retailer`:
--   `adid`
--       `address` -> `adid`
--

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

--
-- RELATIONS FOR TABLE `session`:
--   `uid`
--       `user` -> `uid`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `tradingsuggestion`
--

DROP TABLE IF EXISTS `tradingsuggestion`;
CREATE TABLE IF NOT EXISTS `tradingsuggestion` (
  `pcid_orig` int(11) NOT NULL,
  `pcid_dest` int(11) NOT NULL,
  `date` int(11) NOT NULL,
  `state` smallint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pcid_orig`,`pcid_dest`),
  KEY `i_tradingsuggestion_pcid_orig` (`pcid_orig`),
  KEY `i_tradingsuggestion_pcid_dest` (`pcid_dest`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `tradingsuggestion`:
--   `pcid_orig`
--       `prizecode` -> `pcid`
--   `pcid_dest`
--       `prizecode` -> `pcid`
--

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
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uq_user_email` (`email`),
  KEY `i_user_adid` (`adid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- RELATIONS FOR TABLE `user`:
--   `adid`
--       `address` -> `adid`
--

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

--
-- RELATIONS FOR TABLE `userbadges`:
--   `uid`
--       `user` -> `uid`
--   `bid`
--       `badges` -> `bid`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- RELATIONS FOR TABLE `userpromotion`:
--   `uid`
--       `user` -> `uid`
--   `pid`
--       `promotion` -> `pid`
--

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

--
-- RELATIONS FOR TABLE `usertags`:
--   `uid`
--       `user` -> `uid`
--   `tid`
--       `tag` -> `tid`
--

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
-- RELATIONS FOR TABLE `xppoints`:
--   `uid`
--       `user` -> `uid`
--   `pid`
--       `promotion` -> `pid`
--

--
-- Constraints for dumped tables
--

--
-- Constraints for table `address`
--
ALTER TABLE `address`
  ADD CONSTRAINT `address_ibfk_2` FOREIGN KEY (`stid`) REFERENCES `addrstreet` (`stid`),
  ADD CONSTRAINT `address_ibfk_3` FOREIGN KEY (`llll`) REFERENCES `cplocality` (`llll`);

--
-- Constraints for table `cplocality`
--
ALTER TABLE `cplocality`
  ADD CONSTRAINT `cplocality_ibfk_1` FOREIGN KEY (`dd`) REFERENCES `cpdistrict` (`dd`);

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
