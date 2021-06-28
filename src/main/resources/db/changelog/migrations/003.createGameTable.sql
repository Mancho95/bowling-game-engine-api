--liquibase formatted sql

--changeset francesco:1
create table game
(
	game_id text primary key,
	player_id text NOT NULL,
	match_id text NOT NULL,
	game_score int NOT NULL
);
--rollback not required

--changeset francesco:2
create index game_game_id on game(game_id);
--rollback not required

--changeset francesco:3
create index game_player_id on game(player_id);
--rollback not required

--changeset francesco:4
create index game_match_id on game(match_id);
--rollback not required