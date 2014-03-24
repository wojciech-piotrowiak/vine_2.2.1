# Tasks schema
 
# --- !Ups

CREATE SEQUENCE vine_comment_id_seq;
CREATE TABLE vinecomment (
    id integer NOT NULL,
    gid integer NOT NULL,
    content text,
    vine integer NOT NULL,
    creator integer NOT NULL,
    created timestamp  NOT NULL,
    restricted boolean,
    visible boolean,
	PRIMARY KEY (id),
	foreign key (vine) references vine (id) ON DELETE CASCADE,
	foreign key (creator) references client (id) ON DELETE CASCADE
);
 
# --- !Downs
 
DROP TABLE vine_comment;
DROP SEQUENCE vine_comment_id_seq;