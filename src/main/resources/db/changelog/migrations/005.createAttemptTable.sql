--liquibase formatted sql

--changeset francesco:1
create table attempt
(
	attempt_id text primary key,
	frame_id text NOT NULL,
	knocked_pins int NOT NULL,
	attempt_index int NOT NULL
);
--rollback not required

--changeset francesco:2
create index attempt_attempt_id on attempt(attempt_id);
--rollback not required

--changeset francesco:3
create index attempt_frame_id on attempt(frame_id);
--rollback not required

--changeset francesco:4
create index attempt_attempt_index on attempt(attempt_index);
--rollback not required