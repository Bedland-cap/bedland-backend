DELETE FROM announcement;
DELETE FROM member;
DELETE FROM flat;
DELETE FROM building;
DELETE FROM manager;

INSERT INTO manager (id, version, create_date, update_date, login, password, name, last_name, email, phone_number)
VALUES (1, 0, '2023-02-14', '2023-02-14', 'jkowalski', 'pomarańdż', 'Janusz', 'Kowalski', 'janush.kowal@email.com', '555666777'),
       (2, 0, '2023-02-14', '2023-02-14', 'jwick', 'notthedogplease', 'John', 'Wick', 'john.wick@email.com', '321654987'),
       (3, 0, '2023-02-14', '2023-02-14', 'anowak', '1234', 'Anna', 'Nowak', 'anowak@email.com', '427657548'),
       (4, 0, '2023-02-14', '2023-02-14', 'sstefan', 'password', 'Stefan', 'Stefan', 'stefanx2@email.com', '213721372');

INSERT INTO building(id, version, create_date, update_date, address, building_name, floors, manager_entity_id)
VALUES (1, 0, '2023-02-14', '2023-02-14', 'ul. Legnicka 48H, 54-202 Wrocław', 'Business Garden Wrocław', 6, 1),
       (2, 0, '2023-02-14', '2023-02-14', 'ul. Kolorowa 6, 60-198 Poznań', 'Business Garden Poznań', 16, 2),
       (3, 0, '2023-02-14', '2023-02-14', 'ul. Lublańska 38, 31-476 Kraków', 'Business Park', 4, 3),
       (4, 0, '2023-02-14', '2023-02-14', 'ul. Wrocławska 152A, 45-835 Opole', 'Business Garden', 15, 4),
       (5, 0, '2023-02-14', '2023-02-14', 'ul. Żwirki i Wigury 16a, 02-092 Warszawa', 'Business Garden Warszawa', 2, 1);

INSERT INTO flat(id, version, create_date, update_date, building_entity_id, number, floor, shape_path)
VALUES (1, 0, '2023-02-14', '2023-02-14', 1, 1, 1, "X1-Y17-Z8273"),
       (2, 0, '2023-02-14', '2023-02-14', 1, 2, 1, "X45-Y1457-Z63"),
       (3, 0, '2023-02-14', '2023-02-14', 1, 3, 2, "X351-Y1657-Z84563"),
       (4, 0, '2023-02-14', '2023-02-14', 1, 4, 2, "X342-Y1537-Z83573"),
       (5, 0, '2023-02-14', '2023-02-14', 2, 1, 2, "X1-Y17-Z8273"),
       (6, 0, '2023-02-14', '2023-02-14', 2, 2, 1, "X3451-Y1457-Z8273"),
       (7, 0, '2023-02-14', '2023-02-14', 3, 1, 2, "X4351-Y34517-Z8273"),
       (8, 0, '2023-02-14', '2023-02-14', 4, 1, 1, "X3451-Y13457-Z8273"),
       (9, 0, '2023-02-14', '2023-02-14', 5, 1, 2, "X4531-Y13457-Z8273");

INSERT INTO member(id, version, create_date, update_date, login, password, name, last_name, email, phone_number, is_owner, flat_entity_id)
VALUES (1, 0, '2023-02-14', '2023-02-14', 'iSutherland','IsABEl','Isabel', 'Sutherland', 'isabel.sutherland@email.com', '123456789', 1, 1),
       (2, 0, '2023-02-14', '2023-02-14', 'clWeiss','ClWeiss','Claude', 'Weiss', 'Claude.Weiss@email.com', '123456789', 0, 2),
       (3, 0, '2023-02-14', '2023-02-14', 'aBray','AlBray','Alyssia', 'Bray', 'Alyssia.Bray@email.com', '123456789', 0, 3),
       (4, 0, '2023-02-14', '2023-02-14', 'aPerry','IsABEl','Ashton', 'Perry', 'Ashton.Perry@email.com', '123456789', 1, 4),
       (5, 0, '2023-02-14', '2023-02-14', 'iPerry','IsABEl','Isabel', 'Perry', 'isabel.perry@email.com', '123456789', 0, 4);

INSERT INTO announcement(id, version, create_date, update_date, flat_entity_id, building_entity_id, title, description)
VALUES (1, 0, '2023-02-14', '2023-02-14', 1, 1, 'Uwaga mieszkańcy!','Mamy inwazję szczurów! Gryzą, napadają w ciemnych zaułkach i kradną portfele!');