DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (id, date_time, description, calories, food_owner_id)
VALUES (50, '2020-09-28 23:00', 'taco with extra cheese', 1000, 100000),
       (51 ,timestamp without time zone '2020-09-28 23:00' - interval '3 hours', 'burrito with mayo', 1299, 100000),
       (52, timestamp without time zone '2020-09-28 23:00' - interval '6 hours', 'baked beans', 800, 100001),
       (53, timestamp without time zone '2020-09-28 23:00' - interval '2 days', 'salad', 300, 100001),
       (54, '2020-11-11 11:11', 'fried potato', 1800, 100000),
       (55, timestamp without time zone  '2020-11-11 11:11' - interval '10 hours', 'salmon', 390, 100000);
