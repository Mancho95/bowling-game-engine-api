# bowling-game-engine-api
Bowling engine backend for cgm assessment.

How to install:

- import the project as a "Maven Project" in any Java IDE
- import java 11 jdk
- Update Maven Dependencies
- Run "src/main/resources/db/changelog/000.createDatabase.sql" with a postgres user, in any dbms supporting PostgreSQL databases. You have to be logged as postgres user to run CREATE queries.
- build the application, goal: mvn clean install
- Run application.


Postman Collection to test exposed endpoints:
https://www.getpostman.com/collections/3f7fd9658d36b43e24f5

# POST Player Signup
  Endpoint: /player/signup <br/>
  Body: raw json <br/>
  Creates a new player. The body object needs to have a "player" object, which needs to have "name", "surname" and "nickname" attributes to be validated. 
# GET Player Change Nickname
  Endpoint: /player/change/{playerId}/{newNickname} <br/>
  Changes player nickname with {newNickname}, for given {playerId}
# POST Start Match
  Endpoint: /match/start <br/>
  Body: raw json <br/>
  Creates a new match for given array of players. The body object needs to have a "players" array, with strings containing "playerId"s of playing players.
# GET Best Record
  Endpoint: /record/bestRecord <br/>
  Gets the best record game, returning infos on game played and player.
# GET Best Record For Player
  Endpoint: /record/bestRecord/{playerId} <br/>
  Gets the best record game for player with {playerId}, returning infos on game played and player. 
# GET Leaderboard
  Endpoint: /record/leaderboard <br/>
  Gets the 5 best record game, returning infos on games played and players.
