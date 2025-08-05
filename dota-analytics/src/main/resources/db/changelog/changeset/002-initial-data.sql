--liquibase formatted sql

--changeset dota-dev:2


INSERT INTO roles (id, name)
VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name)
VALUES (2, 'ROLE_ADMIN');


INSERT INTO heroes (name, primary_attribute)
VALUES ('Anti-Mage', 'AGILITY');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Axe', 'STRENGTH');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Bane', 'UNIVERSAL');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Crystal Maiden', 'INTELLIGENCE');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Drow Ranger', 'AGILITY');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Earthshaker', 'STRENGTH');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Juggernaut', 'AGILITY');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Pudge', 'STRENGTH');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Shadow Fiend', 'AGILITY');
INSERT INTO heroes (name, primary_attribute)
VALUES ('Lina', 'INTELLIGENCE');


SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));
SELECT setval('heroes_id_seq', (SELECT MAX(id) FROM heroes));
