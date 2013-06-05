-- phpMyAdmin SQL Dump
-- version 3.5.8.1
-- http://www.phpmyadmin.net
--
-- Máquina: localhost
-- Data de Criação: 04-Jun-2013 às 23:55
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
(1, 'Newbie', '', 'Welcome to PromGame. Congrats on your first login! There''s a big world out there, so keep playing. There are badges to be had! 10 pts'),
(2, 'Promer', 'badges-09.jpg', 'You’ve won your first promotion! You’re a Promer. Now you can try trade it with your frinds, for a new promotion that you like better. 25 pts'),
(3, 'Trader', 'badges-03.jpg', 'You’ve done your first tradding! You’re a Trader. Keep playing and trading the promotions that you don’t like with others. 30 pts'),
(4, 'Compie', 'badges-06.jpg', 'You’ve played your first competitive game! Now you are a Compie, make sure you keep play against your friends to become a Top Compie. 50 pts'),
(5, 'Coopie', NULL, 'You’ve played your first cooperative game! Now you are a Coopie, make sure you keep helping your friends to become a Top Coopie. 50 pts'),
(6, 'Top Promer', 'badges-01.jpg', 'You’ve won 25 promotions! Keep playing to win more. There’s a new badge if you win 50 promotions. 100 pts'),
(7, 'Super Promer', 'badges-02.jpg', 'You’ve won 50 promotions! Congrats, you’re the best, keep winning and having fun! 250 pts'),
(8, 'Top Trader', 'badges-04.jpg', 'You’ve done 25 tradings! Keep trading to win more badges. There’s a new badge if you make 50 tradings. 150 pts'),
(9, 'Super Trader', 'badges-05.jpg', 'You’ve done 50 tradings! Congrats, you’re the best trader. Keep trading and having fun with your new promotions. 300 pts'),
(10, 'Top Compie', 'badges-07.jpg', 'You’ve played 25 competitive games! Keep competing to win more badges, there’s a new one if you beat 50 friends. 250 pts'),
(11, 'Super Compie', 'badges-08.jpg', 'You’ve played 50 competitive games! Congrats, you really are a competitive person, keep competing and beating your friends! 500 pts'),
(12, 'Top Coopie', NULL, 'You’ve played 25 cooperative games! Keep cooperating to win more badges, there’s a new one if you cooperate with 50 friends.\r\n250 pts'),
(13, 'Super Coopie', NULL, 'You’ve played 50 cooperative games! Congrats, you really are a cooperative person, keep cooperating and helping your friends! 500 pts');

--
-- Extraindo dados da tabela `item`
--

INSERT INTO `item` (`iid`, `name`, `image`) VALUES
(1, 'Ruffles com mayonese', 'mayonese.png'),
(2, 'RedBull', 'tourovermelho.png');

--
-- Extraindo dados da tabela `prizecode`
--


--
-- Extraindo dados da tabela `promotion`
--

INSERT INTO `promotion` (`pid`, `active`, `name`, `desc`, `image`, `init_date`, `end_date`, `util_date`, `grand_limit`, `user_limit`, `valid_coord`, `valid_coord_radius`, `transferable`, `win_points`, `func_type`, `rid`, `ptid`) VALUES
(9, 1, 'Promoção Escolha seu Destino', 'Pacotes de Viagem no valor unitário de R 8. 000, 00 , sendo que os ganhadores deverão escolher o destino de sua viagem, o período de duração, escolha dos hotéis, acomodações e reservas para acompanhante, bem como demais itens que compõe sua viagem, sempre respeitando o valor do pacote de viagem sorteado.', 'promo.jpg', 1286705411, 0, 0, 0, 0, NULL, NULL, 1, 10, 1, 1, 1),
(10, 1, 'Super preço', 'Os deuses devem estar roucos.', 'promo4.png', 1318241411, 0, 0, 0, 0, NULL, NULL, 1, 20, 1, 1, 1),
(13, 1, 'Promoção Vai dar volta à cabeça', '200. 000 bolas de futebol exclusivas, divididas em 04 cores. Para participar da promoção Clear, o interessado deverá efetuar a compra, de qualquer produto da linha Clear no valor mínimo de R 20, 00 , em um dos estabelecimentos participantes da promoção', 'promo9.png', 1352628611, 0, 0, 0, 0, NULL, NULL, 1, 30, 1, 1, 1),
(14, 1, 'Promoções férias de Verão', 'Descubra as viagens ao melhor preço! Conheça ofertas de Férias Sol e Praia que temos para si! Férias com descontos até 100% na 2ª pessoa nas Caraíbas (México, República Dominicana, Jamaica) e nas Ilhas Espanholas (Baleares e Canárias).', 'promo25.jpg', 1420279811, 0, 0, 0, 0, NULL, NULL, 1, 40, 0, 1, 1),
(16, 1, 'Especial Club Santana', 'Estada no hotel no regime seleccionado. Dias inteiramente livres para descansar ou para conhecer os recantos desta ilha. Encontrará praias quase desertas, florestas tropicias, gente acolhedora.', 'promo.jpg', 1389521411, 0, 0, 0, 0, NULL, NULL, 1, 100, 1, 1, 1),
(17, 1, 'FÉRIAS Zanzibar', 'País da África Oriental, ladeado pelo Uganda, Quénia, Moçambique, Malawi, Zâmbia e Congo com uma fronteira fluvial, a Tanzânia apresenta uma grande disparidade geográfica com grandes planícies, montanhas e vulcões, verdadeiros santuários para uma imensidão de espécies animais selvagens.', 'promo5.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 2000, 1, 1, 1),
(18, 1, 'Portugal Ao Vivo', 'Vinte anos depois, Portugal volta a fazer ouvir a sua voz. Ou as suas vozes. Duas gerações de músicos marcarão presença em dois dias de celebração da música portuguesa através de alguns dos seus mais destacados expoentes. Tudo acontecerá no estádio do Restelo a 21 e 22 de junho.', 'promo13.jpg', 1, 0, 0, 0, 0, NULL, NULL, 1, 15, 1, 1, 1),
(19, 1, 'Sorriso Maroto', 'O quinteto surgiu em 1997, como uma brincadeira de amigos, no Grajaú, bairro da zona norte carioca. Desde então, Bruno Cardoso (vocais), Cris Oliveira (percussão e vocal), Sérgio Jr (violão e vocal), Vinícius Augusto (teclado e vocal) e Fred (percussão) não pararam de fazer sucessos em todo o Brasil – e agora também na Europa.', 'promo15.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 0, 1, 1, 1),
(20, 1, 'Sammy 2', 'Para seres um dos sortudos a ganhar um DVD, só tens de nos dizer, de forma criativa, qual o animal marinho de que mais gostas.', 'promo17.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 0, 1, 1, 1),
(21, 1, 'DVD à pala', 'Para seres um dos sortudos a ganhar este espetacular DVD, só tens de nos dizer, de forma criativa, quem é a tua personagem preferida do mundo dos jogos e porquê.', 'promo16.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 0, 1, 1, 1),
(22, 1, 'Duarte e Marta 4', 'Esta é a nova aventura de Duarte e Marta que acaba de chegar às livrarias. Mas tu podes ganhar um dos 10 livros que temos para oferecer! Se gostavas de ser um dos sortudos a ganhar este livro, só tens de fazer uma frase que inclua as palavras: SAPO MARTA DUARTE AVENTURA.', 'promo18.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 0, 1, 1, 1),
(23, 1, 'Astérix e Obélix', 'Para seres um dos sortudos a receber em casa este DVD, só tens de nos responder criativamente à seguinte pergunta: Se bebesses uma poção mágica, que poderes gostarias de ter?', 'promo19.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 10, 1, 1, 1),
(24, 1, 'A Onda da Rita', 'Cria um vídeo no MEO Kanal e mostra-nos o espírito tua “onda”. Envia-nos o nº do teu canal.', 'promo20.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 3000, 1, 1, 1),
(25, 1, 'TV5Monde - Festival de Cannes', 'Habilite-se a ganhar uma televisão portátil ou um relógio TV5Monde!', 'promo21.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 20, 1, 1, 1),
(26, 1, 'Consola Nintendo 3DS', 'Veja maravilhosos mundos em 3D ganharem vida na palma da sua mão com a consola Nintendo 3DS Vermelho Metálico e surpreenda-se todos os dias com novos conteúdos descarregados para a consola através de uma ligação Wi-Fi. O ecrã superior da Nintendo 3DS faz uso da mais recente tecnologia para reproduzir videojogos, fotografias ou até vídeos em 3D, sem a necessidade de óculos especiais. E para que a experiência seja o mais confortável possível, a Nintendo 3DS tem um botão que lhe permite ajustar o nível de intensidade do 3D de forma a encontrar um ponto de conforto para a sua visão.', 'promo22.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 200, 1, 1, 1),
(27, 1, 'Kits Minnie Mouse', 'Entre no mundo Minnie Mouse com um aroma delicioso de frutas vermelhas e almíscar doce. Este fantástico kit, composto por 1 perfume Minnie e um Spray desembaraçante com purpurinas, é a prenda ideal para a “princesa lá de casa”! ', 'promo23.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 200, 1, 1, 1),
(28, 1, 'Kit Carros', '“A prenda ideal para todos os fãs de palmo e meio do Faísca. Acelera já no universo louco das corridas com Corine de Farme.” O kit é composto por: um perfume com notas cítricas, um gel de duche 2 em 1 para cabelos e corpo Pêra e um champô suave Pêssego alperce.', 'promo24.jpg', 0, 0, 0, 0, 0, NULL, NULL, 1, 100, 1, 1, 1);

--
-- Extraindo dados da tabela `promotiontype`
--

INSERT INTO `promotiontype` (`ptid`, `name`) VALUES
(1, 'Pague 10 Leve apena 1!');

--
-- Extraindo dados da tabela `qganswer`
--



--
-- Extraindo dados da tabela `qgquestion`
--

INSERT INTO `qgquestion` (`qid`, `question`, `question_type`, `answer_choices`, `expected_answer`, `pid`) VALUES
(12, 'Quantos lados tem um cubo?', 2, '1;2;3;4;5;6', '5', 10),
(13, 'Qual clube ganhou o campeonato nacional de futebol na época 2012/13?', 2, 'FCP;SLB;SCP', '0', 10),
(14, 'Quem é o selecionador nacional de voleibol?', 2, 'Alberto;Manuel;Antunes', '0', 18),
(15, 'Quantas bananas comi ontem?', 2, '1;2;3;4;5;6', '5', 18),
(16, 'Qual a fórmula da àgua?', 2, '2HO;HO2;H2O;OH2', '2', 9),
(17, 'Qual é a montanha mais alta da terra?', 2, 'Monte Kilimanjaro;Monte Evereste;Alpes Suíços;Pirinéus', '1', 9),
(18, 'Quando inspiramos usamos oxigénio e quando expiramos produzimos...', 2, 'Oxigénio;Dióxido de Carbono;Hélio;Monóxido de Carbono', '1', 13),
(19, 'Os raios UV são...', 2, 'Ultra Violentos;UniVersais;Ultra Violeta;Universalmente Violetas', '2', 13),
(20, 'Que animal é o pato feio?', 2, 'Ganso;Pato;Galinha;Gaivota', '0', 14),
(21, 'Qual destas listas contém só continentes?', 2, 'America,Rússia,Australia;Asia,Europa,Africa;Asia,Australia,Brasil;Asia,Africa,Africa do Sul', '1', 14),
(22, 'Qual a sequência de ?', 2, '1,2,3,4,...;1,2,4,8,...;6,5,4,3,2,....;1,1,2,3,5,8,...', '3', 16),
(23, 'Qual destas nações não fez parte da União Soviética?', 2, 'Russia;Narnia;Ucrânia;Bielorrússia', '1', 16),
(24, 'O número 3 é...', 2, 'Par;Ímpar;Neutro;Menor que 2', '1', 17),
(25, 'Tiger Woods ficou famoso por que desporto?', 2, 'Golf;Natação;Futebol;Andebol', '0', 17),
(26, 'Que tipo de Animal é o Garfield?', 2, 'Cão;Gato;Cavalo;porco', '1', 19),
(27, 'Qual destes menus é fast food?', 2, 'Salmão;Carne estufada;Bacalhau à Brás;Pizza', '3', 19),
(28, 'Quais são os eixos do espaço?', 2, 'A,B,C;X,Y,Z;1,2,3;L,W,H', '1', 20),
(29, 'Qual o número aproximado de pi?', 2, '3.28;2.14;3.14;4.56', '2', 20),
(30, 'Quem é o actual presidente da república?', 2, 'Aníbel Cavaco Silva;Jorge Sampaio;Pedro Passos Coelho;Rui Rio', '0', 21),
(31, 'Quem é o PAPA?', 2, 'João Paulo II;Bento XIV;Francisco I;José Pelicarpo', '2', 21),
(32, 'Quantos litros tem o corpo humano?', 2, '5.2;6;7;2', '0', 22),
(33, 'Em que ano se deu a independência do Brasil?', 2, '1990;1822;2010;500', '1', 22),
(34, 'Quando é o dia da liberdade?', 2, '24 de Abril;10 de Outubro;1 de Janeiro;25 de Abril', '3', 23),
(35, 'Janeiro, Fevereiro, Março, Abril, Maio...', 2, 'Junho;Julho;Outubro;Janeiro', '0', 23),
(36, 'Dois ao quadrado é igual a...', 2, '4;5;6;8', '0', 24),
(37, 'Quem venceu o óscar de melhor filme em 2010?', 2, 'Black Swan;The Godfather;Perfume;The King Speech', '3', 24),
(38, 'Raiz ao quadrado de dezasseis é...', 2, '2;4;8;6', '1', 25),
(39, 'Quem protagonizou Wolverine?', 2, 'Colin Firth;Leonardo DiCaprio;Steve Carell;Hugh Jackman', '3', 25),
(40, 'Qual destes não é número primo?', 2, '2;3;4;5', '2', 26),
(41, 'O céu é..', 2, 'Verde;Vermelho;Rosa;Azul', '3', 26),
(42, 'Qual destas cidades é do Norte?', 2, 'Lisboa;Albufeira;Santarém;Viana', '3', 27),
(43, 'Quais são os inimigos dos Angry Birds?', 2, 'Vacas;Ovelhas;Porcos;Cobras', '2', 27),
(44, 'Quais são os inimigos dos Angry Birds?', 2, 'Vacas;Ovelhas;Porcos;Cobras', '2', 28),
(45, 'Dois mais dois são...', 2, '20;10;5;4', '3', 28);

--
-- Extraindo dados da tabela `quizgame`
--

INSERT INTO `quizgame` (`pid`, `name`, `is_quiz`) VALUES
(9, 'Quiz 2', 1),
(10, 'Quiz 1', 1),
(13, 'Quiz 4', 1),
(14, 'Quiz 5', 1),
(16, 'Quiz 6', 1),
(17, 'Quiz 7', 1),
(18, 'Quiz 3', 1),
(19, 'Quiz 8', 1),
(20, 'Quiz 9', 1),
(21, 'Quiz 10', 1),
(22, 'Quiz 11', 1),
(23, 'Quiz 12', 1),
(24, 'Quiz 13', 1),
(25, 'Quiz 14', 1),
(26, 'Quiz 15', 1),
(27, 'Quiz 16', 1),
(28, 'Quiz 17', 1);

--
-- Extraindo dados da tabela `retailer`
--

INSERT INTO `retailer` (`rid`, `name`, `image`, `nif`, `email`, `password`, `adid`, `door`) VALUES
(1, 'Tlantic', 'tlantic.png', 220039990, 'tlantic@tlantic.com', '123456', 127202, '21');

--
-- Extraindo dados da tabela `user`
--

INSERT INTO `user` (`uid`, `active`, `name`, `email`, `password`, `birth`, `adid`, `address2`, `facebook_uid`, `token_twitter`, `reset_token`, `reset_token_validity`, `ui_seed`) VALUES
(1, 1, 'João Henriques', 'ei11026', ':666a0c23c33615f9b87a1190558f8e17:443d832c47ab9a780ca01d66d39ee53c39da034550036e2c56bc4d7fc50d684f:', 123, 10000, '1', NULL, NULL, NULL, 0, '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),
(2, 1, 'Renato Rodrigues', 'r@gmail.com', ':b2aa9898333a0f288fdb29f47a88a484:e19c47dee278593c831f5aae6f487b33337e0ccaa7d24feec740a7f81da4fe0e:', 683143829, 1000, NULL, NULL, NULL, NULL, 0, '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0');

--
-- Extraindo dados da tabela `userbadges`
--

INSERT INTO `userbadges` (`uid`, `bid`, `aquis_date`) VALUES
(1, 6, 1365768730),
(2, 2, 1355314330),
(2, 3, 1357992730),
(4, 4, 1363090330),
(8, 7, 1368360730),
(8, 8, 1371039130),
(10, 9, 1373631130),
(11, 10, 1376309530),
(12, 11, 1378987930);



SET FOREIGN_KEY_CHECKS=1;
COMMIT;
