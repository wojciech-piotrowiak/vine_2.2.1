# Tasks schema
 
# --- !Ups

CREATE SEQUENCE vine_history_id_seq;
CREATE TABLE vinehistory (
    id integer NOT NULL,
    gid integer NOT NULL,
    label varchar(255),
    description text,
    vine integer NOT NULL,
    created timestamp  NOT NULL,
    visible boolean,
	PRIMARY KEY (id),
	foreign key (vine) references vine (id) ON DELETE CASCADE
);
 
# --- !Downs
 
DROP TABLE vine_history;
DROP SEQUENCE vine_history_id_seq;