
DROP TABLE persons;
DROP TABLE movies;
DROP TABLE crew;
DROP TABLE principals;
DROP TABLE ratings;


CREATE TABLE persons
(
    id SERIAL PRIMARY KEY,
    name character varying(250),
    uid character varying(250)
);

CREATE TABLE movies
(
    id SERIAL PRIMARY KEY,
    movie_id character varying(250),
    title character varying(250),
    is_adult boolean,
    release_year bigint,
    duration bigint,
    genres character varying(500)
);


CREATE TABLE crew
(
    id SERIAL PRIMARY KEY,
    movie_id character varying(250),
    directors character varying(8000),
    writers character varying(8000)
);

CREATE TABLE principals
(
    id SERIAL PRIMARY KEY,
    movie_id character varying(100),
    actors character varying(10000)
);

CREATE TABLE ratings
(
    id SERIAL PRIMARY KEY,
    movie_id character varying(100),
    avg_rating double precision,
    votes bigint
);