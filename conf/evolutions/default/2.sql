# Tasks schema
 
# --- !Ups

CREATE SEQUENCE recipe_id_seq;
CREATE TABLE recipe (
    id integer NOT NULL,
    gid integer NOT NULL,
    label varchar(255),
    description text,
    creator integer NOT NULL,
    created timestamp NOT NULL,
    visible boolean,
	PRIMARY KEY (id),
	foreign key (creator) references client (id) ON DELETE CASCADE
);
 
# --- !Downs
 
DROP TABLE recipe;
DROP SEQUENCE recipe_id_seq;