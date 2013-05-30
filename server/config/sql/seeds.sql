-- phpMyAdmin SQL Dump
-- version 3.5.8.1
-- http://www.phpmyadmin.net
--
-- Máquina: localhost
-- Data de Criação: 30-Maio-2013 às 00:59
-- Versão do servidor: 5.5.31
-- versão do PHP: 5.5.0RC2

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT=0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de Dados: `tlantic`
--

--
-- Extraindo dados da tabela `badges`
--

INSERT INTO `badges` (`bid`, `name`, `image`, `description`) VALUES
(1, 'Newbie', 'Banana-Splits-Pin-Badge.jpg', 'Welcome to PromGame. Congrats on your first login! There''s a big world out there, so keep playing. There are badges to be had! 10 pts'),
(2, 'Promer', 'Banana-Splits-Pin-Badge.jpg', 'You’ve won your first promotion! You’re a Promer. Now you can try trade it with your frinds, for a new promotion that you like better. 25 pts'),
(3, 'Trader', 'OurHero.png', 'You’ve done your first tradding! You’re a Trader. Keep playing and trading the promotions that you don’t like with others. 30 pts'),
(4, 'Compie', 'Badge-of-Awesome.png', 'You’ve played your first competitive game! Now you are a Compie, make sure you keep play against your friends to become a Top Compie. 50 pts'),
(5, 'Coopie', NULL, 'You’ve played your first cooperative game! Now you are a Coopie, make sure you keep helping your friends to become a Top Coopie. 50 pts'),
(6, 'Top Promer', NULL, 'You’ve won 25 promotions! Keep playing to win more. There’s a new badge if you win 50 promotions. 100 pts'),
(7, 'Super Promer', NULL, 'You’ve won 50 promotions! Congrats, you’re the best, keep winning and having fun! 250 pts'),
(8, 'Top Trader', NULL, 'You’ve done 25 tradings! Keep trading to win more badges. There’s a new badge if you make 50 tradings. 150 pts'),
(9, 'Super Trader', NULL, 'You’ve done 50 tradings! Congrats, you’re the best trader. Keep trading and having fun with your new promotions. 300 pts'),
(10, 'Top Compie', NULL, 'You’ve played 25 competitive games! Keep competing to win more badges, there’s a new one if you beat 50 friends. 250 pts'),
(11, 'Super Compie', NULL, 'You’ve played 50 competitive games! Congrats, you really are a competitive person, keep competing and beating your friends! 500 pts'),
(12, 'Top Compie', NULL, 'You’ve played 25 cooperative games! Keep cooperating to win more badges, there’s a new one if you cooperate with 50 friends.\r\n250 pts'),
(13, 'Super Coopie', NULL, 'You’ve played 50 cooperative games! Congrats, you really are a cooperative person, keep cooperating and helping your friends! 500 pts');

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
-- Extraindo dados da tabela `prizecode`
--

INSERT INTO `prizecode` (`pcid`, `emiss_date`, `util_date`, `cur_uid`, `valid_code`, `in_trading`, `transaction`, `upid`) VALUES
(1, 0, 0, 2, '123', 1, 1, 2),
(2, 0, 1634044210, 2, '12', 0, 0, 3),
(4, 0, 0, 2, '12345678', 0, 0, 4),
(7, 1, 0, 1, '152464655', 1, 0, 3),
(8, 3456666, 0, 2, 'hytejjutyuetyu', 0, 0, 8),
(9, 1369320865, 0, 2, 'lSp+Y6l87Q3f/cEPtF4CDWImhhYvW9Jy2/dpHnu78jGFV3UFzySEpJbuHOmOpvrOvkNUanrYMHrxClOvtl075w==', 0, 0, 10),
(10, 1369320886, 0, 2, 'hrUk2TGq4HzlOZrLUvnWNB1avLjnAGSvJD7h28AK26yvOxYgC8EKWM8pNlphyI7JNS/WA5ZY3l4Fr1bPeW/qaA==', 0, 0, 11),
(11, 1369321750, 0, 2, 'Ho00w4dlB2nENMotwIJQO5rhIQ30ylpxWjdeNNSw9FpdFZrvGgdl7LiEvuhhM9AiqOLGjF2B/T29C99feWvk9Q==', 0, 0, 16),
(12, 1369321887, 0, 2, 'J2/1koerTZNU0n3BxXJ/SnkjQRXyuBZsrAXzgPE+ZJQuipJoXS6MyU72C1oX75QNIKXAQEvK8UNbHA5I2kObZw==', 0, 0, 18),
(13, 1369608573, 0, 2, 'DSLzg6HlLbJou8JazDuvmayPDQpL7y5phQOvvg4gd4gDk9yYqiFMlBQny6keUD0aTbzwvnGMDs5RQXAcRCHkCg==', 0, 0, 29),
(14, 1321562, 0, 2, 'mhfdsa', 0, 0, 4);

--
-- Extraindo dados da tabela `promotion`
--

INSERT INTO `promotion` (`pid`, `active`, `name`, `desc`, `image`, `init_date`, `end_date`, `util_date`, `grand_limit`, `user_limit`, `valid_coord`, `valid_coord_radius`, `transferable`, `win_points`, `func_type`, `rid`, `ptid`) VALUES
(1, 1, 'Promoção do dia', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin pellentesque elementum arcu. Integer vitae eros ante, a pharetra enim. Nam cursus pulvinar magna et scelerisque.', 'promo.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 0, 0, 1, 1),
(2, 1, 'Promoção da semana', 'O Lorem Ipsum é um texto modelo da indústria tipográfica e de impressão. O Lorem Ipsum tem vindo a ser o texto padrão usado por estas indústrias desde o ano de 1500, quando uma misturou os caracteres de um texto para criar um espécime de livro. Este texto não só sobreviveu 5 séculos, mas também o salto para a tipografia electrónica, mantendo-se essencialmente inalterada. Foi popularizada nos anos 60 com a disponibilização das folhas de Letraset, que continham passagens com Lorem Ipsum, e mais recentemente com os programas de publicação como o Aldus PageMaker que incluem versões do Lorem Ipsum.', 'home.jpg', 0, 1412935810, 0, 0, 0, NULL, NULL, 1, 10, 1, 1, 1),
(3, 1, 'Promoção do Mês', 'É um facto estabelecido de que um leitor é distraído pelo conteúdo legível de uma página quando analisa a sua mancha gráfica. Logo, o uso de Lorem Ipsum leva a uma distribuição mais ou menos normal de letras, ao contrário do uso de "Conteúdo aqui, conteúdo aqui", tornando-o texto legível. Muitas ferramentas de publicação electrónica e editores de páginas web usam actualmente o Lorem Ipsum como o modelo de texto usado por omissão, e uma pesquisa por "lorem ipsum" irá encontrar muitos websites ainda na sua infância. Várias versões têm evoluído ao longo dos anos, por vezes por acidente, por vezes propositadamente (como no caso do humor).', 'poupe-iva-lidl-300x276.png', 0, 1412939410, 0, 0, 0, NULL, NULL, 1, 100, 1, 1, 1),
(4, 1, 'Feliz dia da Mãe', 'Existem muitas variações das passagens do Lorem Ipsum disponíveis, mas a maior parte sofreu alterações de alguma forma, pela injecção de humor, ou de palavras aleatórias que nem sequer parecem suficientemente credíveis. Se vai usar uma passagem do Lorem Ipsum, deve ter a certeza que não contém nada de embaraçoso escondido no meio do texto. Todos os geradores de Lorem Ipsum na Internet acabam por repetir porções de texto pré-definido, como necessário, fazendo com que este seja o primeiro verdadeiro gerador na Internet. Usa um dicionário de 200 palavras em Latim, combinado com uma dúzia de modelos de frases, para gerar Lorem Ipsum que pareçam razoáveis. Desta forma, o Lorem Ipsum gerado é sempre livre de repetição, ou de injecção humorística, etc.', 'hiper-desconto-continente-300x236.png', 0, 1412943010, 0, 0, 0, NULL, NULL, 0, 0, 1, 1, 1),
(5, 1, 'Ovos da Páscoa', 'Ao contrário da crença popular, o Lorem Ipsum não é simplesmente texto aleatório. Tem raízes numa peça de literatura clássica em Latim, de 45 AC, tornando-o com mais de 2000 anos. Richard McClintock, um professor de Latim no Colégio Hampden-Sydney, na Virgínia, procurou uma das palavras em Latim mais obscuras (consectetur) numa passagem Lorem Ipsum, e atravessando as cidades do mundo na literatura clássica, descobriu a sua origem. Lorem Ipsum vem das secções 1.10.32 e 1.10.33 do "de Finibus Bonorum et Malorum" (Os Extremos do Bem e do Mal), por Cícero, escrito a 45AC. Este livro é um tratado na teoria da ética, muito popular durante a Renascença. A primeira linha de Lorem Ipsum, "Lorem ipsum dolor sit amet..." aparece de uma linha na secção 1.10.32.', 'continente-promoÃ§Ã£o-resmas-de-papel.jpg', 0, 1412943010, 0, 0, 0, NULL, NULL, 0, 1000, 1, 1, 1),
(6, 1, 'HappyHour', 'O pedaço mais habitual do Lorem Ipsum usado desde os anos 1500 é reproduzido abaixo para os interessados. As secções 1.10.32 e 1.10.33 do "de Finibus Bonorum et Malorum" do Cícero também estão reproduzidos na sua forma original, acompanhados pela sua tradução em Inglês, versões da tradução de 1914 por H. Rackham.', 'zebra_180_1222446a.jpg', 0, 1412943010, 0, 0, 0, NULL, NULL, 0, 40, 1, 1, 1),
(7, 1, 'Super Promoção', 'Estamos com saudade de trocar ideias com todos vocês... por isso, a partir de hoje temos uma novidade imperdível para os amantes de uma boa leitura: está no ar a promoção exclusiva do Fluindo o Olhar!', 'Promocao', 0, 1412935810, 0, 0, 1, NULL, NULL, 1, 200, 0, 1, 1),
(8, 1, 'Promoção 50%', 'Lorem Ipsum é um texto modelo da indústria tipográfica e de impressão. O Lorem Ipsum tem vindo a ser o texto padrão usado por estas indústrias desde o ano de 1500, quando uma misturou os caracteres de um texto para criar um espécime de livro. Este texto não só sobreviveu 5 séculos, mas também o salto para a tipografia electrónica, mantendo-se essencialmente inalterada. Foi popularizada nos anos 60 com a disponibilização das folhas de Letraset, que continham passagens com', 'Promo50.png', 0, 0, 0, 0, 1, NULL, NULL, 0, 20, 0, 1, 1);

--
-- Extraindo dados da tabela `promotiontype`
--

INSERT INTO `promotiontype` (`ptid`, `name`) VALUES
(1, 'Pague 10 Leve apena 1!');

--
-- Extraindo dados da tabela `qganswer`
--

INSERT INTO `qganswer` (`qid`, `upid`, `answer`) VALUES
(1, 28, '2'),
(1, 29, '1'),
(1, 30, '1'),
(2, 28, '1'),
(2, 29, '0'),
(2, 30, '2'),
(3, 28, '1'),
(3, 29, '1'),
(3, 30, '0'),
(4, 28, '1'),
(4, 29, '4'),
(4, 30, '2'),
(5, 30, '1'),
(6, 30, '1'),
(7, 30, '1'),
(8, 30, '1'),
(9, 30, '2'),
(10, 30, '2'),
(11, 30, '1');

--
-- Extraindo dados da tabela `qgquestion`
--

INSERT INTO `qgquestion` (`qid`, `question`, `question_type`, `answer_choices`, `expected_answer`, `pid`) VALUES
(1, 'Que nome se dá a alguém que nega a existência de Deus?', 2, 'Judeu;Ateu;Cristão;Pagão', '1', 1),
(2, 'Qual é a cor da laranja?', 2, 'Cor de laranja;Amarelo;Azul;Verde', '0', 1),
(3, 'Qual dos seguintes países não está localizado no mesmo continente dos demais?', 2, 'Portugal;Brasil;França;Holanda ', '1', 1),
(4, 'Como se chama o Papa?', 2, 'Francisco; Bento; Manuel; Pedro; João; Marco', '4', 1),
(5, 'Por quantos ossos é constituído o esqueleto humano?', 2, '200;199;201;206', '3', 1),
(6, 'Como se chama a camada da pele que está à superfície?', 2, 'Derme;Epiderme;Nervo;Músculo', '1', 1),
(7, 'Qual o mamífero mais rápido da Terra?', 2, 'Lebre;Leão;Cavalo;Leopardo;Zebra;Gazela', '3', 1),
(8, 'Em que ano chegou Vasco da Gama à Índia?  ', 2, '1502;1498;1499;1500;2010;2009', '1', 1),
(9, 'Qual dos nomes não corresponde a um apóstolo de Cristo?', 2, 'Tiago;Bartolomeu;António;André', '2', 1),
(10, 'Quantos litros de sangue contém o corpo humano de um adulto?', 2, '2;5;6;10', '1', 1),
(11, 'Quem foi o vencedor do campeonato português de futebol 12/13?', 2, 'Futebol Clube do Porto;Sport Lisboa e Benfica;Sporting Clube de Portugal;Futebol Clube de Paços de Ferreira', '0', 1);

--
-- Extraindo dados da tabela `quizgame`
--

INSERT INTO `quizgame` (`pid`, `name`, `is_quiz`) VALUES
(1, 'Quiz', 1),
(2, 'Quiz Amanhã', 1);

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
('9b94eb7b0dfcd9758f79e61e29f6419e93cca5783bf841b8cd52f1b7d24774b1', 2, 1369916701),
('9c738b74c2cc057b923347daf0a96b60ba602524d3436ec1129a7bfaffe6f7d9', 2, 1368628101),
('b0166048cf7680173458f026d0781d1b4c347b5e6a3f1644badbc8f4d015d41a', 1, 1369925257);

--
-- Extraindo dados da tabela `user`
--

INSERT INTO `user` (`uid`, `name`, `email`, `password`, `birth`, `adid`, `door`, `facebook_uid`, `token_twitter`, `reset_token`, `reset_token_validity`, `ui_seed`) VALUES
(1, 'João Henriques', 'ei11026', ':666a0c23c33615f9b87a1190558f8e17:443d832c47ab9a780ca01d66d39ee53c39da034550036e2c56bc4d7fc50d684f:', 123, 10000, '1', NULL, NULL, NULL, 0, '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),
(2, 'Rui Manuel', 'r@gmail.com', ':b2aa9898333a0f288fdb29f47a88a484:e19c47dee278593c831f5aae6f487b33337e0ccaa7d24feec740a7f81da4fe0e:', 25, 190699, NULL, NULL, NULL, NULL, 0, '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0');

--
-- Extraindo dados da tabela `userbadges`
--

INSERT INTO `userbadges` (`uid`, `bid`, `aquis_date`) VALUES
(2, 1, 687089410);

--
-- Extraindo dados da tabela `userpromotion`
--

INSERT INTO `userpromotion` (`upid`, `uid`, `pid`, `init_date`, `end_date`, `state`) VALUES
(2, 1, 1, 1, 1367262244, 1),
(3, 2, 2, 1, 1412946610, 1),
(4, 2, 3, 1, 1418303410, 1),
(5, 2, 1, 1, 1607692210, 1),
(6, 2, 6, 1, 1634044210, 1),
(7, 1, 3, 123, 0, 1),
(8, 1, 6, 111, 1, 1),
(10, 1, 1, 1369320855, 1369320865, 1),
(11, 1, 1, 1369320855, 1, 1),
(12, 1, 2, 1369321606, 0, 0),
(13, 1, 3, 1369321612, 0, 0),
(14, 2, 5, 1369321649, 0, 0),
(16, 1, 1, 1, 1, 1),
(17, 1, 1, 1, 1, 0),
(18, 1, 1, 1, 1, 1),
(20, 2, 4, 1369403961, 1, 0),
(21, 1, 6, 1369403995, 1, 1),
(28, 1, 1, 1369608526, 1369608532, 0),
(29, 1, 1, 1369608553, 1369608573, 1),
(30, 1, 1, 1369654334, 1369654357, 0),
(31, 2, 4, 1369840174, 0, 0),
(32, 2, 1, 1369860220, 0, 0);

--
-- Extraindo dados da tabela `xppoints`
--

INSERT INTO `xppoints` (`uid`, `aquis_date`, `xp_points`, `pid`) VALUES
(1, 1349874602, 80, 2),
(1, 1349961002, 80, 1),
(1, 1349961062, 80, 4),
(2, 1349863802, 10, 1),
(2, 1349867402, 20, 2),
(2, 1349871002, 30, 3),
(2, 1349874602, 40, 6);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;
