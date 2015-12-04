
DROP DATABASE sql395256;
CREATE DATABASE sql395256;

USE sql395256;

CREATE TABLE User(
	userID INT(11) primary key key auto_increment not null,
    username VARCHAR(50) not null,
    pass VARCHAR(50) not null
);

INSERT INTO User (username,pass) VALUES ('Sheldon','password1');
INSERT INTO User (username,pass) VALUES ('Leonard','bannana');
INSERT INTO User (username,pass) VALUES ('Penny','peanutbutter');
INSERT INTO User (username,pass) VALUES ('George','theJungle');
INSERT INTO User (username,pass) VALUES ('Snoop','Lion');
INSERT INTO User (username,pass) VALUES ('Kanye','West');
INSERT INTO User (username,pass) VALUES ('Donald','Trump');
INSERT INTO User (username,pass) VALUES ('Kendrick','Lamar');
INSERT INTO User (username,pass) VALUES ('Kid','Cudi');
INSERT INTO User (username,pass) VALUES ('The','realest');
INSERT INTO User (username,pass) VALUES ('point','guard');
INSERT INTO User (username,pass) VALUES ('Barack','Obama');
INSERT INTO User (username,pass) VALUES ('Stevie','Wonder');
INSERT INTO User (username,pass) VALUES ('jk','penny');
INSERT INTO User (username,pass) VALUES ('Aaron','Cote');
INSERT INTO User (username,pass) VALUES ('Jeffrey','Miller');
INSERT INTO User (username,pass) VALUES ('faraz','Miller');
INSERT INTO User (username,pass) VALUES ('derek','Miller');
INSERT INTO User (username,pass) VALUES ('james','Miller');
INSERT INTO User (username,pass) VALUES ('noob','Miller');
INSERT INTO User (username,pass) VALUES ('sda','Miller');
INSERT INTO User (username,pass) VALUES ('number','Miller');
INSERT INTO User (username,pass) VALUES ('someone','Miller');
INSERT INTO User (username,pass) VALUES ('manfred','Miller');
INSERT INTO User (username,pass) VALUES ('jason','Miller');
INSERT INTO User (username,pass) VALUES ('williams','Miller');
INSERT INTO User (username,pass) VALUES ('forgetit','Miller');
INSERT INTO User (username,pass) VALUES ('cheese','Miller');
INSERT INTO User (username,pass) VALUES ('cake','Miller');
INSERT INTO User (username,pass) VALUES ('hungry','Miller');
INSERT INTO User (username,pass) VALUES ('food','Miller');



CREATE TABLE SecretInfo(
	userID INT(11) primary key not null,
    secretQuestion VARCHAR(100) not null,
    secretAnswer VARCHAR(100) not null
);

INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(1, 'What school do you go to?', 'USC');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(2, 'What city are you from?', 'LCA');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(3, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(4, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(5, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(6, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(7, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(8, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(9, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(10, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(11, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(12, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(13, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(14, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(15, 'YES or NO?', 'NO');

INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(16, 'What school do you go to?', 'USC');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(17, 'What city are you from?', 'LCA');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(18, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(19, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(20, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(21, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(22, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(23, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(24, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(25, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(26, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(27, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(28, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(29, 'YES or NO?', 'NO');
INSERT INTO SecretInfo(userID,secretQuestion,secretAnswer) VALUES(30, 'YES or NO?', 'NO');


CREATE TABLE BasicStats(
	userID INT(11) primary key not null,
	rating INT(11),
    wins INT(11),
    losses INT(11),
    gamesPlayed INT (11)
);

INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(1, 1210, 1,0,1);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(2, 1190, 0, 1 ,1);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(3, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(4, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(5, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(6, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(7, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(8, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(9, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(10, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(11, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(12, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(13, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(14, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(15, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(16, 1210, 1,0,1);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(17, 1190, 0, 1 ,1);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(18, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(19, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(20, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(21, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(22, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(23, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(24, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(25, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(26, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(27, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(28, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(29, 1200, 0, 0, 0);
INSERT INTO BasicStats(userID,rating,wins,losses,gamesPlayed) VALUES(30, 1200, 0, 0, 0);



CREATE TABLE GameStats(
	userID INT(11) not null,
    opponentName VARCHAR(50),
    iWon BOOLEAN,
    chessNotation VARCHAR(10000)
);

INSERT INTO GameStats(userId,opponentName,iWon,chessNotation) VALUES(1,'noob',true,'chessnotation');
INSERT INTO GameStats(userId,opponentName,iWon,chessNotation) VALUES(1,'other',true,'chessnotation2');






