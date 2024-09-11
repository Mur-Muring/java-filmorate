DROP TABLE IF EXISTS user, rating, film, genre, film_genres, film_users, friends;

CREATE TABLE IF NOT EXISTS user
(
    id       integer generated by default as identity not null primary key,
    email    varchar(300) NOT NULL,
    login    varchar(300) NOT NULL,
    name     varchar(300),
    birthday date
);

CREATE TABLE IF NOT EXISTS rating
(
    id          integer generated by default as identity not null primary key,
    name        varchar(300) NOT NULL UNIQUE
    description varchar(300) NOT NULL
);

CREATE TABLE IF NOT EXISTS film
(
    id           integer generated by default as identity not null primary key,
    name         varchar(300) NOT NULL,
    description  varchar(300),
    releaseDate  date         NOT NULL,
    duration     integer      NOT NULL,
    rating_mpa   integer      NOT NULL REFERENCES rating(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS genre
(
    id   integer generated by default as identity not null primary key,
    name varchar(300) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  integer REFERENCES film(id) ON DELETE CASCADE,
    genre_id integer REFERENCES genre(id) ON DELETE RESTRICT,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_users
(
    film_id  integer REFERENCES film(id) ON DELETE CASCADE,
    user_id integer REFERENCES user(id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);


CREATE TABLE IF NOT EXISTS friends
(
    user_id integer REFERENCES user(id) ON DELETE CASCADE,
    friend_id integer REFERENCES user(id) ON DELETE CASCADE,
    status varchar(300)
);