--liquibase formatted sql

--changeset francesco:1
create table match
(
	match_id text primary key,	
  	match_winner_id text NOT NULL,
  	match_winner_nickname text NOT NULL,
  	match_date varchar(50)
);
--rollback not required

--changeset francesco:2
create index match_match_id on match(match_id);
--rollback not required

--changeset francesco:3
create index match_match_winner_id on match(match_winner_id);
--rollback not required