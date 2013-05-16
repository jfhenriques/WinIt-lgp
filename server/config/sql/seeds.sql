-- phpMyAdmin SQL Dump
-- version 3.5.8.1
-- http://www.phpmyadmin.net
--
-- Máquina: localhost
-- Data de Criação: 14-Maio-2013 às 16:14
-- Versão do servidor: 5.5.31
-- versão do PHP: 5.5.0RC1

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT=0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de Dados: `tlantic`
--

--
-- Extraindo dados da tabela `item`
--

INSERT INTO `item` (`iid`, `name`, `image`) VALUES
(1, 'Ruffles com mayonese', 'mayonese.png'),
(2, 'RedBull', 'tourovermelho.png');

--
-- Extraindo dados da tabela `itempromotion`
--

INSERT INTO `itempromotion` (`iid`, `pid`, `percent`) VALUES
(1, 2, 0),
(2, 2, 0);

--
-- Extraindo dados da tabela `promotion`
--

INSERT INTO `promotion` (`pid`, `active`, `name`, `desc`, `image`, `init_date`, `end_date`, `grand_limit`, `user_limit`, `valid_coord`, `valid_coord_radius`, `transferable`, `win_points`, `func_type`, `rid`, `ptid`) VALUES
(1, 1, 'Promoção do dia', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin pellentesque elementum arcu. Integer vitae eros ante, a pharetra enim. Nam cursus pulvinar magna et scelerisque.', 'promo.jpg', 0, 0, 0, 0, NULL, NULL, 0, 0, 0, 1, 1);

--
-- Extraindo dados da tabela `promotiontype`
--

INSERT INTO `promotiontype` (`ptid`, `name`) VALUES
(1, 'Pague 10 Leve apena 1!');

--
-- Extraindo dados da tabela `qganswer`
--

INSERT INTO `qganswer` (`qid`, `upid`, `answer`) VALUES
(1, 2, '1');

--
-- Extraindo dados da tabela `qgquestion`
--

INSERT INTO `qgquestion` (`qid`, `question`, `question_type`, `answer_choices`, `expected_answer`, `pid`) VALUES
(1, 'Que nome se dá a alguém que nega a existência de Deus?', 2, 'Judeu;Ateu;Cristão;Pagão', '1', 1);

--
-- Extraindo dados da tabela `quizgame`
--

INSERT INTO `quizgame` (`pid`, `name`, `is_quiz`) VALUES
(1, 'Quiz', 1);

--
-- Extraindo dados da tabela `retailer`
--

INSERT INTO `retailer` (`rid`, `name`, `image`, `nif`, `email`, `password`, `adid`, `door`) VALUES
(1, 'Continestes', 'continestes.png', NULL, 'naotem@nao.com', '123456', NULL, NULL);

--
-- Extraindo dados da tabela `session`
--

INSERT INTO `session` (`token`, `uid`, `validity`) VALUES
('2db3aa75a874240edb845639bde3d298ad41b7d3d8a75d95f7c45187abfa83d1', 1, 1367821365),
('4c2830e0262a7fa89f242af8606deea2954ecfe6e41d086fd04024ecd418ebc4', 2, 1367883119),
('9c738b74c2cc057b923347daf0a96b60ba602524d3436ec1129a7bfaffe6f7d9', 2, 1368628101);

--
-- Extraindo dados da tabela `user`
--

INSERT INTO `user` (`uid`, `name`, `email`, `password`, `birth`, `adid`, `door`, `token_facebook`, `token_twitter`, `reset_token`, `reset_token_validity`, `ui_seed`) VALUES
(1, 'João Henriques', 'ei11026', ':666a0c23c33615f9b87a1190558f8e17:443d832c47ab9a780ca01d66d39ee53c39da034550036e2c56bc4d7fc50d684f:', 123, 10000, NULL, NULL, NULL, NULL, 0, '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),
(2, 'Renato', 'r@gmail.com', ':b2aa9898333a0f288fdb29f47a88a484:e19c47dee278593c831f5aae6f487b33337e0ccaa7d24feec740a7f81da4fe0e:', 25, 190699, '4dto', NULL, NULL, NULL, NULL, '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0');

--
-- Extraindo dados da tabela `userpromotion`
--

INSERT INTO `userpromotion` (`upid`, `uid`, `pid`, `init_date`, `end_date`, `state`) VALUES
(2, 1, 1, 1, 1367262244, 1);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;
