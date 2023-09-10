DROP TABLE IF EXISTS USERS;

CREATE TABLE IF NOT EXISTS USERS (
  USER_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
  EMAIL varchar(255) NOT NULL,
  LOGIN varchar(255) NOT NULL,
  USER_NAME varchar(255) NOT NULL,
  BIRTHDAY DATETIME NOT NULL
);

DROP TABLE IF EXISTS FILMS;

CREATE TABLE IF NOT EXISTS FILMS (
  FILM_ID INT PRIMARY KEY AUTO_INCREMENT,
  FILM_NAME varchar(255) NOT NULL,
  FILM_DESCRIPTION text NOT NULL,
  MPA INT NOT NULL,
  DURATION INT NOT NULL,
  RELEASE_DATE DATETIME NOT NULL
);

DROP TABLE IF EXISTS FRIEND_USER;

CREATE TABLE IF NOT EXISTS FRIEND_USER (
  USER_ID INT NOT NULL,
  FRIEND_ID INT NOT NULL,
  COMMON_FRIEND boolean NOT NULL
);

DROP TABLE IF EXISTS GENRES;

CREATE TABLE IF NOT EXISTS GENRES (
  GENRE_ID INT PRIMARY KEY ,
  GENRE_NAME varchar(255) NOT NULL
);


DROP TABLE IF EXISTS GENRES_FILMS;

CREATE TABLE IF NOT EXISTS GENRES_FILMS (
  GENRE_ID INT NOT NULL,
  FILM_ID INT NOT NULL,
  PRIMARY KEY (GENRE_ID, FILM_ID)
);


DROP TABLE IF EXISTS MPA;

CREATE TABLE IF NOT EXISTS MPA (
  RATING_ID INT PRIMARY KEY,
  RATING_NAME varchar(255) NOT NULL
);
DROP TABLE IF EXISTS LIKES_FILMS;

create table if not exists LIKES_FILMS (
  FILM_ID BIGINT NOT NULL,
  USER_ID BIGINT NOT NULL
    );