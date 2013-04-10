use tlantic
;

set foreign_key_checks=0;


drop table if exists admin
;
drop table if exists badges
;
drop table if exists badgesutilizador
;
drop table if exists buyin
;
drop table if exists codigopremio
;
drop table if exists comprapromocao
;
drop table if exists item
;
drop table if exists itempromocao
;
drop table if exists pergunta
;
drop table if exists pontosxp
;
drop table if exists promocao
;
drop table if exists promocaoutilizador
;
drop table if exists proximityalert
;
drop table if exists quizgame
;
drop table if exists quizgameresposta
;
drop table if exists retalhista
;
drop table if exists sorteio
;
drop table if exists sugestaotrading
;
drop table if exists tags
;
drop table if exists tagspromocao
;
drop table if exists tagsutilizador
;
drop table if exists tipopromocao
;
drop table if exists utilizador
;

create table admin
(
	nome varchar(200) null,
	password varchar(200) null,
	adminid integer not null,
	primary key (adminid),
	unique uq_admin_nome(nome)
) engine=innodb
;


create table badges
(
	nome varchar(200) null,
	imagem varchar(200) null,
	descricao varchar(200) null,
	badgesid integer not null,
	primary key (badgesid)
) engine=innodb
;


create table badgesutilizador
(
	badgesid integer not null,
	utilizadorid integer not null,
	data_aquis integer null,
	primary key (badgesid, utilizadorid),
	key (badgesid),
	key (utilizadorid)
) engine=innodb
;


create table buyin
(
	data int null,
	estado int null,
	buyinid integer not null,
	sorteioid integer null,
	promocaoutilizadorid integer null,
	primary key (buyinid),
	key (promocaoutilizadorid),
	key (sorteioid)
) engine=innodb
;


create table codigopremio
(
	data_emissao int null,
	data_utilizacao int null,
	currently_owned_by int null,
	codigo_validacao int null,
	em_trading int null,
	codigopremioid integer not null,
	promocaoutilizadorid integer null,
	primary key (codigopremioid),
	key (promocaoutilizadorid)
) engine=innodb
;


create table comprapromocao
(
	custo_xp int null,
	comprapromocaoid integer not null,
	primary key (comprapromocaoid),
	key (comprapromocaoid)
) engine=innodb
;


create table item
(
	nome varchar(200) null,
	imagem varchar(200) null,
	itemid integer not null,
	primary key (itemid)
) engine=innodb
;


create table itempromocao
(
	promocaoid integer not null,
	itemid integer not null,
	primary key (promocaoid, itemid),
	key (promocaoid),
	key (itemid)
) engine=innodb
;


create table pergunta
(
	pergunta varchar(200) null,
	tipo_resposta_esperada integer null,
	resposta_esperada varchar(200) null,
	perguntaid integer not null,
	quizgameid integer null,
	primary key (perguntaid),
	key (quizgameid)
) engine=innodb
;


create table pontosxp
(
	data_aquis int null,
	pontos int null,
	pontosxpid integer not null,
	utilizadorid integer null,
	primary key (pontosxpid),
	key (utilizadorid)
) engine=innodb
;


create table promocao
(
	nome varchar(200) null,
	data_inicio int null,
	data_fim int null,
	max_utilizacoes int null,
	coord_validas varchar(200) null,
	coord_raio int null,
	transmissivel int null,
	activa int null,
	pontos_ao_ganhar int null,
	promocaoid integer not null,
	retalhistaid integer null,
	tipopromocaoid integer null,
	primary key (promocaoid),
	key (retalhistaid),
	key (tipopromocaoid)
) engine=innodb
;


create table promocaoutilizador
(
	utilizadorid integer null,
	promocaoid integer null,
	data_inicio integer null,
	data_fim integer null,
	estado smallint null,
	promocaoutilizadorid integer not null,
	primary key (promocaoutilizadorid),
	key (utilizadorid),
	key (promocaoid)
) engine=innodb
;


create table proximityalert
(
	proximityalertid integer not null,
	primary key (proximityalertid),
	key (proximityalertid)
) engine=innodb
;


create table quizgame
(
	titulo varchar(200) null,
	is_quiz int null,
	quizgameid integer not null,
	primary key (quizgameid),
	key (quizgameid)
) engine=innodb
;


create table quizgameresposta
(
	resposta varchar(200) null,
	quizgamerespostaid integer not null,
	perguntaid integer null,
	promocaoutilizadorid integer null,
	quizgameid integer null,
	primary key (quizgamerespostaid),
	key (promocaoutilizadorid),
	key (perguntaid),
	key (quizgameid)
) engine=innodb
;


create table retalhista
(
	nome varchar(200) null,
	password varchar(200) null,
	retalhistaid integer not null,
	primary key (retalhistaid)
) engine=innodb
;


create table sorteio
(
	custo_xp int null,
	sorteioid integer not null,
	primary key (sorteioid),
	key (sorteioid)
) engine=innodb
;


create table sugestaotrading
(
	codigopremioorigid integer not null,
	codigopremiodestid integer not null,
	data integer null,
	estado smallint null,
	primary key (codigopremioorigid, codigopremiodestid),
	key (codigopremiodestid),
	key (codigopremioorigid)
) engine=innodb
;


create table tags
(
	nome varchar(200) null,
	tagsid integer not null,
	primary key (tagsid)
) engine=innodb
;


create table tagspromocao
(
	tagsid integer not null,
	promocaoid integer not null,
	primary key (tagsid, promocaoid),
	key (tagsid),
	key (promocaoid)
) engine=innodb
;


create table tagsutilizador
(
	utilizadorid integer not null,
	tagsid integer not null,
	primary key (utilizadorid, tagsid),
	key (utilizadorid),
	key (tagsid)
) engine=innodb
;


create table tipopromocao
(
	nome varchar(200) null,
	percentagem double null,
	tipopromocaoid integer not null,
	primary key (tipopromocaoid)
) engine=innodb
;


create table utilizador
(
	nome varchar(200) null,
	email int null,
	password varchar(200) null,
	cp4 int null,
	cp3 int null,
	porta_andar int null,
	token_facebook varchar(200) null,
	token_twitter varchar(200) null,
	utilizadorid integer not null,
	primary key (utilizadorid)
) engine=innodb
;



set foreign_key_checks=1;


alter table badgesutilizador add constraint fk_badges 
	foreign key (badgesid) references badges (badgesid)
;

alter table badgesutilizador add constraint fk_badges_utilizador 
	foreign key (utilizadorid) references utilizador (utilizadorid)
;

alter table buyin add constraint fk_buyin_sorteio 
	foreign key (sorteioid) references sorteio (sorteioid)
;

alter table comprapromocao add constraint fk_comprapromocao_promocao 
	foreign key (comprapromocaoid) references promocao (promocaoid)
;

alter table itempromocao add constraint fk_itempromo_promocao 
	foreign key (promocaoid) references promocao (promocaoid)
;

alter table itempromocao add constraint fk_item 
	foreign key (itemid) references item (itemid)
;

alter table pergunta add constraint fk_pergunta_quizgame 
	foreign key (quizgameid) references quizgame (quizgameid)
;

alter table pontosxp add constraint fk_pontosxp_utilizador 
	foreign key (utilizadorid) references utilizador (utilizadorid)
;

alter table promocao add constraint fk_promocao_retalhista 
	foreign key (retalhistaid) references retalhista (retalhistaid)
;

alter table promocao add constraint fk_promocao_tipopromocao 
	foreign key (tipopromocaoid) references tipopromocao (tipopromocaoid)
;

alter table promocaoutilizador add constraint fk_promouser_utilizador 
	foreign key (utilizadorid) references utilizador (utilizadorid)
;

alter table promocaoutilizador add constraint fk_promouser_promocao 
	foreign key (promocaoid) references promocao (promocaoid)
;

alter table proximityalert add constraint fk_proximityalert_promocao 
	foreign key (proximityalertid) references promocao (promocaoid)
;

alter table quizgame add constraint fk_quizgame_promocao 
	foreign key (quizgameid) references promocao (promocaoid)
;

alter table quizgameresposta add constraint fk_quizgameresposta_pergunta 
	foreign key (perguntaid) references pergunta (perguntaid)
;

alter table quizgameresposta add constraint fk_quizgameresposta_quizgame 
	foreign key (quizgameid) references quizgame (quizgameid)
;

alter table sorteio add constraint fk_sorteio_promocao 
	foreign key (sorteioid) references promocao (promocaoid)
;

alter table sugestaotrading add constraint fk_codigopremioorigid 
	foreign key (codigopremioorigid) references codigopremio (codigopremioid)
;

alter table tagspromocao add constraint fk_tagspromo_tags 
	foreign key (tagsid) references tags (tagsid)
;

alter table tagspromocao add constraint fk_tagspromo_promocao 
	foreign key (promocaoid) references promocao (promocaoid)
;

alter table tagsutilizador add constraint fk_tagsuser_utilizador 
	foreign key (utilizadorid) references utilizador (utilizadorid)
;

alter table tagsutilizador add constraint fk_tagsuser_tags 
	foreign key (tagsid) references tags (tagsid)
;
