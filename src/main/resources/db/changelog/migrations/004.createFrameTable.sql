--liquibase formatted sql

--changeset francesco:1
create table frame
(
	frame_id text primary key,
	game_id text NOT NULL,
	frame_type text NOT NULL,
	frame_score int NOT NULL,
	frame_index int NOT NULL
);
--rollback not required

--changeset francesco:2
create index frame_frame_id on frame(frame_id);
--rollback not required

--changeset francesco:3
create index frame_game_id on frame(game_id);
--rollback not required

--changeset francesco:4
create index frame_frame_type on frame(frame_type);
--rollback not required

--changeset francesco:5
create index frame_frame_index on frame(frame_index);
--rollback not required