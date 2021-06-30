--liquibase formatted sql

--changeset francesco:1
create table player
(
	player_id text primary key,
	name text NOT NULL,
	surname text NOT NULL,
	nickname text NOT NULL,
  	sign_up_date varchar(50)
);
--rollback not required

--changeset francesco:2
create index player_player_id on player(player_id);
--rollback not required