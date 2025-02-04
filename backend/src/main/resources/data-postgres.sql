INSERT INTO PROJET (ID, NOM, DEBUT, FIN)
VALUES (default, 'Projet 1 (en cours)', '2025-01-01', NULL),        -- code 1
       (default, 'Projet 2 (terminé)', '2024-12-01', '2024-12-31'), -- code 2
       (default, 'Projet 3 (en cours)', '2024-11-01', NULL); -- code 3

INSERT INTO PERSONNE(MATRICULE, NOM, PRENOM, POSTE, SUPERIEUR_MATRICULE)
VALUES (default, 'Doe', 'John', 'CEO', NULL),              -- matricule 1
       (default, 'Reagan', 'Jane', 'Manager', 1),          -- matricule 2
       (default, 'Carter', 'Alice', 'System Engineer', 1), -- matricule 3
       (default, 'Truman', 'Bob', 'Developper', 2),        -- matricule 4
       (default, 'Roosevelt', 'Eve', 'Analyst', 2); -- matricule 5

INSERT INTO PARTICIPATION(ROLE, POURCENTAGE, PROJET_ID, PERSONNE_MATRICULE)
VALUES ('Chef de projet', 0.2, 1, 2),       -- participation 1
       ('Front-End Developper', 0.3, 1, 3), -- participation 2
       ('Back-End Developper', 0.2, 1, 4),  -- participation 3
       ('Tester', 0.4, 1, 5),               -- participation 4
       ('Chef de projet', 0.4, 2, 2),       -- participation 5
       ('Front-End Developper', 0.3, 2, 3), -- participation 6
       ('Back-End Developper', 0.2, 2, 4),  -- participation 7
       ('Analyst', 0.4, 2, 5),              -- participation 8
       ('Chef de projet', 0.1, 3, 2),       -- participation 9
       ('Front-End developper', 0.3, 3, 3); -- participation 10
