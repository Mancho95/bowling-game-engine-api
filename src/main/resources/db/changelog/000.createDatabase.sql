-- Execute with postgresql super user
create role bowling_game_engine_owner LOGIN ENCRYPTED PASSWORD 'd25d8cc0be33e4a517550364b6c86de672171235';
create role bowling_game_engine_writer LOGIN ENCRYPTED PASSWORD '4a5c318876b860a07c8381ada470fa08e7bcfcd7';

GRANT bowling_game_engine_owner TO postgres;

CREATE DATABASE bowling_game_engine
    WITH OWNER = bowling_game_engine_owner
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;