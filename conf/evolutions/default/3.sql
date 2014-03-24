# Tasks schema
 
# --- !Ups

CREATE SEQUENCE vine_id_seq;
CREATE TABLE vine (
    id integer NOT NULL,
    gid integer NOT NULL,
    label varchar(255),
    description text,
    client integer NOT NULL,
    recipe integer,
    created timestamp NOT NULL,
    visible boolean,
	PRIMARY KEY (id),
	foreign key (client) references client (id) ON DELETE CASCADE,
	foreign key (recipe) references recipe (id) ON DELETE CASCADE
);
 
# --- !Downs
 
DROP TABLE vine;
DROP SEQUENCE vine_id_seq;