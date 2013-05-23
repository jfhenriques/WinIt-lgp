-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 23, 2013 at 10:47 AM
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

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`iid`, `name`, `image`) VALUES
(1, 'item 1', 'img1.png'),
(2, 'item 2', 'img2.png');

--
-- Dumping data for table `itempromotion`
--

INSERT INTO `itempromotion` (`iid`, `pid`, `percent`) VALUES
(1, 3, 0),
(2, 3, 0);

--
-- Dumping data for table `prizecode`
--

INSERT INTO `prizecode` (`pcid`, `emiss_date`, `util_date`, `cur_uid`, `valid_code`, `in_trading`, `transaction`, `upid`) VALUES
(14, 1368536898, 0, 2, '10OtX2vjKa0YE8EgRtmfft3Aaw9HzkybaozqbK4Bb0igtGlSsGpgFh8h3PiS6nnCzIe9AiX2gT/u8dNKyOvI4Q==', 1, 0, 44),
(15, 0, 0, 1, '1', 1, 0, 45),
(16, 0, 0, 2, '2', 1, 0, 46);

--
-- Dumping data for table `promotion`
--

INSERT INTO `promotion` (`pid`, `active`, `name`, `desc`, `image`, `init_date`, `end_date`, `util_date`, `grand_limit`, `user_limit`, `valid_coord`, `valid_coord_radius`, `transferable`, `win_points`, `func_type`, `rid`, `ptid`) VALUES
(3, 1, 'Batatas fritas', NULL, 'superpromo.png', 0, 0, 0, 0, 0, NULL, NULL, 1, 0, 0, 1, 1),
(8, 1, 'ewrw', '', NULL, 0, 0, 0, 0, 0, NULL, NULL, 1, 0, 1, 1, 1),
(9, 1, 's', NULL, 's', 0, 0, 0, 0, 0, NULL, NULL, 1, 0, 1, 1, 1);

--
-- Dumping data for table `promotiontype`
--

INSERT INTO `promotiontype` (`ptid`, `name`) VALUES
(1, 'Desconto Percentagem');

--
-- Dumping data for table `qganswer`
--

INSERT INTO `qganswer` (`qid`, `upid`, `answer`) VALUES
(1, 44, '1'),
(2, 44, '1  ;    0');

--
-- Dumping data for table `qgquestion`
--

INSERT INTO `qgquestion` (`qid`, `question`, `question_type`, `answer_choices`, `expected_answer`, `pid`) VALUES
(1, 'Pergunta 1', 2, 'a;b;c;d', '1', 3),
(2, 'pergunta 2', 3, 'a;b;c', '0;1', 3);

--
-- Dumping data for table `quizgame`
--

INSERT INTO `quizgame` (`pid`, `name`, `is_quiz`) VALUES
(3, 'Perguntas parvas', 1);

--
-- Dumping data for table `retailer`
--

INSERT INTO `retailer` (`rid`, `name`, `image`, `nif`, `email`, `password`, `adid`, `door`) VALUES
(1, 'Continente', 'continente.png', NULL, 'mail@continente.pt', 'algumapass', NULL, NULL);

--
-- Dumping data for table `session`
--

INSERT INTO `session` (`token`, `uid`, `validity`) VALUES
('1f54f7c87771d963fc15a36c85399d0fc0a83f7c457e424c58d4fc7483a13dc5', 2, 0);

--
-- Dumping data for table `tradingsuggestion`
--

INSERT INTO `tradingsuggestion` (`pcid_orig`, `pcid_dest`, `date`, `transaction`, `state`) VALUES
(16, 15, 1369296852, 0, 0);

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`uid`, `name`, `email`, `password`, `birth`, `adid`, `door`, `token_facebook`, `token_twitter`, `reset_token`, `reset_token_validity`, `ui_seed`) VALUES
(1, 'Joããão Henriques', 'jf@netmadeira.com', ':151577b869f9817fa145d349a72476ba:7555e36ddb61a84006751448232c93baf51b58071bc03cc1653adb366877ad66:', 0, 123, NULL, NULL, NULL, '758e8f965268eb82db15264ae87bb60db5a7c3bc08c9437f01ad1b36e0bcce29f6e04af4d713aa012334c9d3bea8c44f706887b9a968ee89b511146ea7011881', 1367105506, '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),
(2, 'João Henr', 'jf2', ':a97a5cb1405b2c1e1a1d655e6a50c6c7:02aef59391424211b562b65f828329c676825c8b42742742121982eb17abd324:', 1, 20001, NULL, NULL, NULL, NULL, 0, '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),
(11, 'john', 'aasaaaaa', ':a87e4763eeed75b61cfa8f91fd1b8498:63cbb81d027fefaff1fb8b04220ae0e987c2d94880d89f6a7067581df3090718:', 123, 1, NULL, NULL, NULL, NULL, NULL, '��xv[t�O��-�؇��Ib�����h�,[vH\0�');

--
-- Dumping data for table `userpromotion`
--

INSERT INTO `userpromotion` (`upid`, `uid`, `pid`, `init_date`, `end_date`, `state`) VALUES
(44, 2, 3, 1368536803, 1368536898, 1),
(45, 1, 3, 1, 1, 1),
(46, 2, 8, 1, 1, 1);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;
