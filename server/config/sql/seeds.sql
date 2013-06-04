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

INSERT INTO `prizecode` (`pcid`, `emiss_date`, `util_date`, `cur_uid`, `valid_code`, `in_trading`, `transaction`, `upid`) VALUES
(28, 1369987113, 0, 2, '8E+hPhEC3/nBna8hq3jQfYSMMKPbc5RgehR9Bcvlt7FXRL4wCwJdXxO8h5xJFK0te0GkiEKAZ1yi7trMXBnFjA==', 1, 0, 61),
(29, 0, 0, 13, '1231455586', 1, 0, 63),
(30, 1369987782, 0, 2, 'AciSS8XucfliDsk3aq10ZVfiqqDXoU9A1qUiGe/sq7RYxbhHW+28+ecqS/B9l5R89EKPzkzg37o1QJ5Ig0GL7w==', 0, 1, 64),
(31, 1369998886, 0, 2, 'rvZKAEMHpSRrClmI6WBSIrFd1VKpyODnYQEB3KMkqq7d+INNYGjZ3+V2h75qQAzTbFdyWctzOc0yMxLQV72a6w==', 0, 0, 70),
(32, 1370001461, 0, 2, '3NeGCqoLbgKVKJ6iD9DRF+9lVpf3wd3Ihk2DE+0ZQtHslO5wI9V4nqrnPeAEtMn4TcVWq4XmDmkzrpnvnE5jgQ==', 0, 0, 75),
(33, 1370001853, 0, 2, 'cOVXeapIpCGXpeqQcZVynkJVqJJdXpEaV+v06uCbYvVG4LL+9bvWi6ZLAEUVJ20FraFXn9xqtI4ff+5MlhELUQ==', 0, 0, 76),
(34, 1370001943, 0, 2, 'edeOIAaRfxNxnTkv+n12qQ2gQ8s3lCSIhSzZc+gx3Vnr/l0tjtdSLekIUfyWjoq2g/A1g9BuKNHdAUaDKk34Yw==', 0, 0, 77),
(35, 1370002463, 0, 2, '0r0uAHa1QDrWpo3SfK+O4ZUkePbTXilwrZOkn2+z2dKR6I9FMknN7uWrpe2mvoM9oOr83SheEskvjHEzu2hPIg==', 0, 0, 78),
(36, 1370002827, 0, 2, 'JyYUClFCQ+VmI0wBcvhISy4GdEOAhKAYxi+PPxiNBB00nnD6QHref9FC80kV3gKnY5EQ8NbopeIHzqDK4uC7lw==', 0, 0, 80),
(37, 1370002870, 0, 2, 'JSiPcN+ZiTuCH9peOAySgjPoHqXm13d+PB/I2qO5B7PpBfOWdypsi/xIjkgcfgVWDsQPEXZ8WkcfJaMidO8qwQ==', 0, 0, 81),
(38, 1370002886, 0, 2, '3ACRBMGkPO+6PGAHwmMOw6hLyVLPGWoLViEXOJ62fdhRbsfxTkDdigN0J2YRIoryS0JUrmfYxBLj5/i5xipyzQ==', 0, 0, 82),
(39, 1370006821, 0, 13, 'A1biNA5lfP2HfGyDucZHyQwKaAVGBfWCW6m4Cdmenc3J3TbHKWYtN3LgBIXYjoRv+FQNnObvAZb2NgAc3O5kjQ==', 1, 1, 84),
(40, 1370009926, 0, 8, '7eDlJO9b9ih/nBtClbCponULYwPC3Wv4XbdLwEIvXchh+ZF3WIdWk5XBGb2rYrFStSAoZlRRtRyipxl7y9popQ==', 0, 0, 86),
(41, 1370012452, 0, 8, 'J9jDeQurDHySfazpcRQk3omTGxlw1rWayXXlX72mW63u1LqugUc+2IM1SkGrwueYSVEUkTZ80lv6WzvpP9y9Zw==', 1, 1, 87),
(42, 1370012679, 0, 8, '+dvt4WLV8xidw5OsRZ3Ov3uACm2A5FzHyY7JcQUuAxePaBjOi/1APbi+Yd3cbgVGpp+l4YCuT02DfhQ40g8uxA==', 1, 1, 88),
(43, 1370012799, 0, 8, 'o+rtAK6DbXebynOR4KcbZiCqQHqpdfz3hgV2HlxcvqILcAU5VrnoxVUPD8O/vGejFg2zBoiFm5LICVcrIX0wMw==', 0, 0, 89),
(44, 1370013046, 0, 8, 'YMeBYDKkfvVpQ76PwgMJf6sFMgPp1OFM9PfMRrcYCf4zpcvjUYo9BOiIzA2/2oUup/mr9cGij5nGdFHGqhNNQA==', 0, 0, 90),
(45, 1370013164, 0, 8, 'oFg5udgZkQBUGPOoC3cVjcoqHLUGPSxxIhMBSlxmf+Xc9IleVGktyf3gxGPUn9RuaGUQ5+9OBXXjJ5HKCESSaw==', 0, 0, 92),
(46, 1370013674, 0, 8, 'g+6nffbTdJsAADlfo+rl/UexxMSB3FwNdd22PnFBhAVyYHPvi75aIJtzcisyHis+Hg8tjssZwuIFJASnpw/9eA==', 0, 0, 93),
(47, 1370013912, 0, 8, 'MKqGDqYRoS7IzPMPZXxbdoRxlSZDlAf9P1EFthkPgyHbk800kdUmnSX+oMUtO6DMt68rrvM6lOMt9tL9IyJfJg==', 0, 0, 95),
(48, 1370013920, 0, 8, '8l87j5Y310MwF3cvzsmL4tqLNFcdu7tSRylwnw0Jf2K87F66lDS5zbmHQM4SizCtPJqOcGj8VpADGvgf6CYC6A==', 0, 0, 96),
(49, 1370013939, 0, 8, 'Q2PriNaXgAz1trBe8aVKH9xlmBLllAB043e3vKW8k7pP+N3CC81pxbEX8b8MN6OqXtq/37nJk8eUg9vtdOy+fQ==', 0, 0, 97),
(50, 1370013956, 0, 8, '6/dH6duQRMQLrvF0+gQDN3OInGGqHZNjTm6+9VZ9nwF5mvgk8SieLYMI5UisS4nVCfNfa2EsmvRSTg0z0C7xcQ==', 0, 0, 98),
(51, 1370013983, 0, 8, 'Ej5l8L0ijdGZtToYqUNh+ORuUplPdDS1Yk4hYtg96SAYSm144mkhQXDHTSEADdyNcetOwVEu52rLeFtN9Gm9cw==', 0, 0, 99),
(52, 1370013990, 0, 8, 'A/xNuUFBiZW6eu6o17/fCsvPpE5zLckCl6K9Apu/wkmDWtToepVpc0/fVpHV9eul6m+P8V7J//SQouEgaDot0w==', 0, 0, 100),
(53, 1370013999, 0, 8, 'vf7xvJU2yDX5OJ7CYFqRSOJheRnVlT1KYOUspCrZXAHcEnh8Qk7hhNOuLcrCrp7Zu4TZbuwoLNHDgZy67eu+iA==', 0, 0, 101),
(54, 1370015432, 0, 8, '8icjFOSsLPi04w7XUskSRxog0R8bzuanRUpFmS2xNt0j44YjbuRpyB1MMv9X5fyDJurLAoW0OtTeLH7wsmQBzA==', 0, 0, 102),
(55, 1370015567, 0, 8, 'sJJgTBJsEq0w3ocfHoMhXcmABJDFK3czKiIaNnkOvWPPeuwc85bT9+qTieILjDi9t6u8ekPItlYXDs1LYovMJQ==', 0, 0, 103),
(56, 1370015604, 0, 8, 'RXWoUYzOAc7PDhI610gE3RBlGq4KiGUmJusMUFXOBfM7GHp9P7cUwcCDvPga6bSNYCFWO4aidDW4Ukg3dorxFw==', 0, 0, 104),
(57, 1370015616, 0, 8, 'l/VnZwCFkxcZneXsGvVEnP5wJ2JvJygQgj0pg/R2vjDd6/Enng1P2Lpnz+Y4/4bN6I9kH7nWbt/AcHb5nFtoMA==', 0, 0, 105),
(58, 1370015668, 0, 8, 'rcfXtuE7wuZ0sTihl1cWNo+U1FzuVmaFEqb1TqLrzvZneMqnNB7LXWWCJ31d6xSmLUNWyu/pA0CH3B1WjvQkow==', 0, 0, 106),
(59, 1370015674, 0, 8, 'IB4o2m/hZcaQ1/aPCCaA9eunrOfJnIKu7Xcmh1H+m0Q82xlQ7eOqgJjDQupA21oGinZBIzxAHyxNuayp+9bWQA==', 0, 0, 107),
(60, 1370015687, 0, 8, 'r+hmQcHNS1pmStTSMro0TM0aVYh8VhhV86cuypeqjW3s9Jm9T2fDl90EOLtm0NagTfC/S+rP/zUKzaNzJAAxDQ==', 0, 0, 108),
(61, 1370015702, 0, 8, 'Ip7ebfYwpB55weG12m0EostrS7Q1uiBfXjAI96r1UbQeNvVnnYDFbm9k9NITfs5/ZLzA5hhYaiC03Jwabz819Q==', 1, 1, 109),
(62, 1370016037, 0, 2, '2+BTcT2+zajykUenp9UUl1Ue/kQlvSzBJ0+VN+giDGm5N0lYGpC/KX3PLaHvvOzwR/ytW9dI9vXXqwAsUFAvBw==', 0, 0, 110),
(63, 1370019132, 0, 8, 'LcegZIkGM4q5g6ZevWS0HbZH+jGCRpXj6rrc5aLkkMepFoNyZ+XaolXQay4SJ+M6f2ihMZOfdO1z2zCCKrC+TQ==', 1, 1, 115),
(64, 1370019529, 0, 14, '7cr41wDX5pMdYWKBYT3gP8lrZBYM4sFWZnoW3r8j4RYxw6GugKGv4PK4YChfykq+kDr65TEhzOwqXxtcTsziVA==', 0, 0, 118),
(65, 1370028637, 0, 13, 'U5VKGwwzS4Ima5GA2IuZHznxJNxMYeVaVRHHgLtaeDie4IqA4vik7yiGlENPQ0zgTPMn8Bc51Q52PuC07ubqDA==', 1, 1, 120),
(66, 1370028784, 0, 13, 'W4V7elxzB+/StdwrpMp5Sj21dck7UuGSi8GwXx2he+B1H1S43iZR1gn5hU/abBN/H/t94DCNsMwHP0cZEmr/pQ==', 1, 1, 121),
(67, 1370028790, 0, 8, 'tR7ht4cRWBDJTGWC1YO3q2F44nrylDDnoaz9V2jqOv3/XdszBm92G5BLfUXQrpULPILS5H/S944IeuIYUqFPRg==', 1, 1, 123),
(68, 1370028792, 0, 8, 'yWcYWLXE+qlODzjm0d+WJpAKeNN3moIKGIuVLvCzWnARuRPhZ+CBW7Dbg3DEdg+1kFL86uuexbL/s60Lg6pD9A==', 0, 0, 83),
(69, 1370029786, 0, 11, 'jQzZXswMwJi29tv8rypb8w/oKqbQ0PAL1Ue8xQ7U8y7FbuuEfi8QPuOr75+8Nug5M6/s5naw93InO/KILuPSWQ==', 1, 1, 124),
(70, 1370031977, 0, 2, 'o8YYjx1O0uusDReQwrzQ62oC6M0QEtfptCwCDfbkoRT+1g80lJJZeOby/TRsupo1k9w00HeaNwDuTi2361wc5A==', 0, 0, 127),
(71, 1, 0, 2, 'dfwsfsdfsdfsd1sd143fas4fd6sd', 1, 1, 128),
(72, 1370122261, 0, 2, 'nCKAt+ivvYTHZz+gziZOYUJbj1E7EegFLTtzNOrVteN7jdY13HJttPP9vIGhgzKVVbIYZFitPRjWVjJBzGs5iA==', 0, 0, 131);

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

INSERT INTO `qganswer` (`qid`, `upid`, `answer`) VALUES
(12, 61, '5'),
(12, 70, '5'),
(12, 75, '5'),
(12, 77, '5'),
(12, 78, '5'),
(12, 79, '2'),
(12, 80, '5'),
(12, 81, '5'),
(12, 86, '5'),
(12, 89, '5'),
(12, 90, '5'),
(12, 92, '5'),
(12, 93, '5'),
(12, 95, '5'),
(12, 96, '5'),
(12, 97, '5'),
(12, 98, '5'),
(12, 99, '5'),
(12, 100, '5'),
(12, 101, '5'),
(12, 102, '5'),
(12, 104, '5'),
(12, 105, '5'),
(12, 108, '5'),
(12, 109, '5'),
(12, 110, '5'),
(12, 115, '5'),
(12, 118, '5'),
(12, 122, '5'),
(12, 123, '5'),
(12, 127, '5'),
(13, 61, '0'),
(13, 70, '0'),
(13, 75, '0'),
(13, 77, '0'),
(13, 78, '0'),
(13, 79, '0'),
(13, 80, '0'),
(13, 81, '0'),
(13, 86, '0'),
(13, 89, '0'),
(13, 90, '0'),
(13, 92, '0'),
(13, 93, '0'),
(13, 95, '0'),
(13, 96, '0'),
(13, 97, '0'),
(13, 98, '0'),
(13, 99, '0'),
(13, 100, '0'),
(13, 101, '0'),
(13, 102, '0'),
(13, 104, '0'),
(13, 105, '0'),
(13, 108, '0'),
(13, 109, '0'),
(13, 110, '0'),
(13, 115, '0'),
(13, 118, '0'),
(13, 122, '1'),
(13, 123, '0'),
(13, 127, '0'),
(14, 64, '0'),
(14, 76, '0'),
(14, 82, '0'),
(14, 84, '0'),
(14, 87, '0'),
(14, 88, '0'),
(14, 103, '0'),
(14, 106, '0'),
(14, 107, '0'),
(14, 111, '0'),
(14, 112, '2'),
(14, 113, '1'),
(15, 64, '5'),
(15, 76, '5'),
(15, 82, '5'),
(15, 84, '5'),
(15, 87, '5'),
(15, 88, '5'),
(15, 103, '5'),
(15, 106, '5'),
(15, 107, '5'),
(15, 111, '4'),
(15, 112, '1'),
(15, 113, '5'),
(16, 83, '2'),
(17, 83, '1'),
(18, 120, '1'),
(18, 129, '3'),
(18, 130, '3'),
(18, 131, '1'),
(19, 120, '2'),
(19, 129, '3'),
(19, 130, '2'),
(19, 131, '2'),
(30, 121, '0'),
(30, 132, '2'),
(31, 121, '2'),
(31, 132, '3'),
(40, 124, '2'),
(41, 124, '3');

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
-- Extraindo dados da tabela `session`
--

INSERT INTO `session` (`token`, `uid`, `validity`) VALUES
('10dd89e75e97e1dbc631af141e5aaf6339adbc128c7170bbb8cdfc41d4644157', 4, -1),
('2db3aa75a874240edb845639bde3d298ad41b7d3d8a75d95f7c45187abfa83d1', 1, 1367821365),
('34ba2d90089e47a83723246942475f5e8bcd36dea727ee442536c4f2bb987c45', 12, 1370555006),
('40b9425dea78dbdae5e26a9c5902913f7ce00c5e591496898d6b61fc803016fa', 8, -1),
('4c2830e0262a7fa89f242af8606deea2954ecfe6e41d086fd04024ecd418ebc4', 2, 1367883119),
('7234e9ef4f8e6f1a04a4409485fd3f6e8882163408214b875157ac2bafe694a5', 17, 1370990993),
('78942cbd3e5197b5daa9009b6fb161ec0b34b9d011362bf31313a6a8492044c6', 8, -1),
('8ae573a1756653114da996cb6cb60e7c98320b772b481cd263a31e5d8a2a836e', 8, -1),
('9b94eb7b0dfcd9758f79e61e29f6419e93cca5783bf841b8cd52f1b7d24774b1', 2, 1369916701),
('9c738b74c2cc057b923347daf0a96b60ba602524d3436ec1129a7bfaffe6f7d9', 2, 1368628101),
('af515fdec94f5368be455d8d8341c81419c54814977bedb0a732d8fe5b4f09b0', 10, 1370537832),
('b0166048cf7680173458f026d0781d1b4c347b5e6a3f1644badbc8f4d015d41a', 1, 1369925257),
('bf304c2ced552291955c3114be17f308678b4b7976cf0f99027c342d9cae37d9', 16, 1370987161),
('cf7784ccf29618d34250a766a2047893052186de1f9f13a06a9d638692061fef', 2, 1370523087),
('d6c83b4eea21b103e7a92976ed599f47dd73c0e1ee52a047f3fd4d8c1fdb2310', 12, -1),
('d93dd9fc2eb0f79d98d5273b09eeb7e58b08fd09bde3f5a784eeeebb6ceec7a2', 8, 1370712377),
('dc188543225c759f04417722a1052fcc28975799821e6242c4d93d06a1e7a90b', 11, 1370540157),
('de00fd3b9079b99feabf6fa2e58c98d381207767853158839a66612a0ed4b497', 13, 1370554773),
('e1aa56e896045426a26a56b0427361226105a2fc7bc20e7f1cd54aaec932b462', 14, 1370624196),
('f4f05d8b05f8ca51165fc4cef68727f5f7dd61eed2ce8d9fd524962aab2b61ec', 15, 1370883506);

--
-- Extraindo dados da tabela `tradingsuggestion`
--

INSERT INTO `tradingsuggestion` (`pcid_orig`, `pcid_dest`, `transaction`, `date`, `end_date`, `state`) VALUES
(30, 29, 0, 1369995157, 1370378269, 3),
(39, 28, 0, 1370028437, 0, 0),
(40, 66, 1, 1370224047, 0, 0),
(40, 71, 1, 1370221486, 0, 0),
(42, 28, 0, 1370013785, 0, 0),
(42, 29, 0, 1370029037, 0, 0),
(43, 29, 0, 1370221682, 0, 0),
(43, 66, 1, 1370224050, 0, 0),
(43, 71, 1, 1370223503, 0, 0),
(44, 66, 1, 1370224052, 0, 0),
(44, 71, 1, 1370223790, 0, 0),
(45, 66, 1, 1370224054, 0, 0),
(46, 29, 0, 1370019177, 0, 0),
(54, 71, 1, 1370221707, 0, 0),
(55, 28, 0, 1370029084, 0, 0),
(55, 29, 0, 1370221687, 0, 0),
(55, 71, 1, 1370221705, 0, 0),
(57, 71, 1, 1370224073, 0, 0),
(58, 28, 0, 1370029064, 0, 0),
(58, 71, 1, 1370224071, 0, 0),
(59, 28, 0, 1370029080, 0, 0),
(59, 71, 1, 1370221498, 0, 0),
(60, 29, 0, 1370221689, 0, 0),
(60, 71, 1, 1370113064, 1370113100, 1),
(62, 39, 1, 1370382452, 1370385480, 3),
(65, 28, 0, 1370028849, 0, 0),
(65, 41, 1, 1370383138, 0, 0),
(68, 71, 0, 1370112617, 1370112809, 2);

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

--
-- Extraindo dados da tabela `usergcm`
--

INSERT INTO `usergcm` (`uid`, `gcm`, `date`) VALUES
(8, 'APA91bEn-_t7KamKlOS0aduYuK83OAV2y47OHKAhYFKO63hFBQY8C5K6mjBL0-5h-7etomu8nvY5ByHoCvnxpVo049OValIpucypdhf91e8GMRnlyqCoQU1JqNjuAlUlqI_TQffGVMo7HgCjtyEKCvs6_cI7EDaxXw', 1370225003),
(8, 'APA91bFi9aNQBFRn3rd6ne8bk-8CzWrDCBHhYolipIRbNiaDFJv3ydTBnvijSJzWc7BjZSI7DgXxs75cBr-FvQEoGf4g08DPE3Zgc4omPAi-CTZB-mtZkKn8txY4OvYqjCEosla91krpNRzagsDz2HGkMAhQ46JozQ', 1370224037),
(8, 'APA91bFLSATvAsfUbA1_6BludXyyw9j8tQ49qQ44IXcMoGYSx3pGc92BCDYdcq96QUa34KgnlQyB0zJIQWVuE6u5lU5ckHwMzwCmyXJvoKRa6De6Qea7Pall78V8nPxbeR-ZjSidLGf57inNGHeD3Ah0vFhNZSyA3A', 1370293753),
(8, 'APA91bGX2ZARL1IKsZ8d0G04l2TyQNEqDg8U9hqZiV3seHrnIUcpVmYxOoyzTH7aT1YmOFtTInLhhvMFLo1GvcEJ157zHp5MIRiBYUDs3byIGhL_YQAPNX8AdyxKi6aTcI-UwfH3CuKVSwJa_d_Ibx9KNoQa9SQQ1g', 1370285867),
(8, 'APA91bHe9nXKRE3VEgjhb6ygiG_CvI4DS4Lk7Tie_kCmuliaeX9_Hwbr9mCfP2ndLJFqC2XgTqMjuhCiMDUmi6kNE3_NCg_apVfYf4U2GF-K4LTGPKCRnophNdL_oT6h83MhOHoJnRMkYONJJpbTUIiQsYNcXM6ibw', 1370293722),
(11, 'APA91bFxH2ZVvSEHIdCdnT972y0E601qW3StPTfRJZ76hAG41cQJmO1LIb1yJz6uTXIKkRIRpULtx8EFfaSxGU2VZqT6GzEaSd5gi24-unPSQwPbwcsD_eFzmjIKKo8iH1fy6wRP13VTOVabLVA-dRBLl8CbJ-YFCg', 1370256764),
(11, 'APA91bGOvViHgExAvYUU7scnmdxOXNmaP0COOTJW7k1ZMdkSC1wnlHUUdKa9p0AjLklJCkv-N0BpISLnTxIa9nBoI_x7lnT6FSvWWBgmxg5J_TxArLictJPtwRzSFT2PNagccriV9DcUE_J4Qap5xXiNT9k5ZzkSOw', 1370265010);

--
-- Extraindo dados da tabela `userpromotion`
--

INSERT INTO `userpromotion` (`upid`, `uid`, `pid`, `init_date`, `end_date`, `state`) VALUES
(61, 2, 10, 1369987094, 1, 1),
(62, 2, 9, 1369987125, 1, 1),
(63, 13, 22, 1, 1, 1),
(64, 2, 18, 1369987773, 1, 1),
(65, 2, 10, 1369991722, 1, 0),
(66, 2, 18, 1369991828, 1, 0),
(67, 2, 13, 1369992198, 1, 0),
(68, 8, 9, 1369998528, 1, 0),
(69, 8, 10, 1369998586, 1, 0),
(70, 2, 10, 1369998877, 1, 1),
(71, 2, 10, 1369998904, 1, 0),
(72, 8, 13, 1369998989, 1, 0),
(73, 2, 9, 1369999242, 1, 0),
(74, 2, 13, 1369999269, 1, 0),
(75, 2, 10, 1370000052, 1370001461, 1),
(76, 2, 18, 1370001442, 1370001853, 1),
(77, 2, 10, 1370001922, 1370001943, 1),
(78, 2, 10, 1370001996, 1370002463, 1),
(79, 2, 10, 1370002468, 1370002529, 0),
(80, 2, 10, 1370002536, 1370002827, 1),
(81, 2, 10, 1370002840, 1370002870, 1),
(82, 2, 18, 1370002880, 1370002886, 1),
(83, 2, 9, 1370003515, 1370028792, 1),
(84, 13, 18, 1370006817, 1370006821, 1),
(86, 8, 10, 1370009920, 1370009926, 1),
(87, 8, 18, 1370012446, 1370012452, 1),
(88, 8, 18, 1370012674, 1370012679, 1),
(89, 8, 10, 1370012793, 1370012799, 1),
(90, 8, 10, 1370013041, 1370013046, 1),
(92, 8, 10, 1370013159, 1370013164, 1),
(93, 8, 10, 1370013670, 1370013674, 1),
(95, 8, 10, 1370013906, 1370013912, 1),
(96, 8, 10, 1370013917, 1370013920, 1),
(97, 8, 10, 1370013935, 1370013939, 1),
(98, 8, 10, 1370013952, 1370013956, 1),
(99, 8, 10, 1370013979, 1370013983, 1),
(100, 8, 10, 1370013987, 1370013990, 1),
(101, 8, 10, 1370013995, 1370013999, 1),
(102, 8, 10, 1370015428, 1370015432, 1),
(103, 8, 18, 1370015558, 1370015567, 1),
(104, 8, 10, 1370015600, 1370015604, 1),
(105, 8, 10, 1370015612, 1370015616, 1),
(106, 8, 18, 1370015663, 1370015668, 1),
(107, 8, 18, 1370015671, 1370015674, 1),
(108, 8, 10, 1370015681, 1370015687, 1),
(109, 8, 10, 1370015698, 1370015702, 1),
(110, 2, 10, 1370016034, 1370016037, 1),
(111, 2, 18, 1370016068, 1370016074, 0),
(112, 2, 18, 1370016135, 1370016139, 0),
(113, 2, 18, 1370016154, 1370016159, 0),
(115, 8, 10, 1370019107, 1370019132, 1),
(118, 14, 10, 1370019524, 1370019529, 1),
(120, 13, 13, 1370028616, 1370028637, 1),
(121, 13, 21, 1370028756, 1370028784, 1),
(122, 8, 10, 1370028773, 1370028779, 0),
(123, 8, 10, 1370028786, 1370028790, 1),
(124, 11, 26, 1370029776, 1370029786, 1),
(127, 2, 10, 1370031941, 1370031977, 1),
(128, 8, 26, 1, 1, 0),
(129, 2, 13, 1370122209, 1370122235, 0),
(130, 2, 13, 1370122242, 1370122246, 0),
(131, 2, 13, 1370122250, 1370122261, 1),
(132, 2, 21, 1370122287, 1370122290, 0),
(139, 2, 9, 1370385523, 0, 0);

--
-- Extraindo dados da tabela `xppoints`
--

INSERT INTO `xppoints` (`uid`, `aquis_date`, `xp_points`, `pid`) VALUES
(2, 1369980964, 10, 10),
(2, 1369980986, 10, 10),
(2, 1369981022, 10, 10),
(2, 1369981060, 20, 10),
(2, 1369984372, 20, 10),
(2, 1369987113, 20, 10),
(2, 1369987782, 15, 18),
(2, 1369998886, 20, 10),
(2, 1370001461, 20, 10),
(2, 1370001853, 15, 18),
(2, 1370001943, 20, 10),
(2, 1370002463, 20, 10),
(2, 1370002529, 10, 10),
(2, 1370002827, 20, 10),
(2, 1370002870, 20, 10),
(2, 1370002886, 15, 18),
(2, 1370016037, 20, 10),
(2, 1370016074, 7, 18),
(2, 1370016159, 7, 18),
(2, 1370028792, 10, 9),
(2, 1370031977, 20, 10),
(2, 1370122246, 15, 13),
(2, 1370122261, 30, 13),
(8, 1370009926, 20, 10),
(8, 1370012452, 15, 18),
(8, 1370012679, 15, 18),
(8, 1370012799, 20, 10),
(8, 1370013046, 20, 10),
(8, 1370013164, 20, 10),
(8, 1370013674, 20, 10),
(8, 1370013912, 20, 10),
(8, 1370013920, 20, 10),
(8, 1370013939, 20, 10),
(8, 1370013956, 20, 10),
(8, 1370013983, 20, 10),
(8, 1370013990, 20, 10),
(8, 1370013999, 20, 10),
(8, 1370015432, 20, 10),
(8, 1370015567, 15, 18),
(8, 1370015604, 20, 10),
(8, 1370015616, 20, 10),
(8, 1370015668, 15, 18),
(8, 1370015674, 15, 18),
(8, 1370015687, 20, 10),
(8, 1370015702, 20, 10),
(8, 1370019132, 20, 10),
(8, 1370028779, 10, 10),
(8, 1370028790, 20, 10),
(11, 1370029786, 200, 26),
(13, 1370006821, 15, 18),
(13, 1370028637, 30, 13),
(14, 1370019529, 20, 10);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;
