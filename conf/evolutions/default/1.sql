# Tasks schema
 
# --- !Ups

CREATE SEQUENCE client_id_seq;
CREATE SEQUENCE gid_seq;
CREATE TABLE client (
    id integer NOT NULL,
    gid integer NOT NULL,
	login varchar(255) UNIQUE,
    firstName varchar(255),
	lastName varchar(255),
	registered timestamp NOT NULL,
	active boolean,
	password varchar(255),
	PRIMARY KEY (id)
);
# --- !Downs
 
DROP TABLE client;
DROP SEQUENCE client_id_seq;
DROP SEQUENCE gid_seq;