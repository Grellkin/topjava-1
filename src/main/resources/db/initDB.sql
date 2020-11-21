DROP TABLE IF EXISTS user_roles;
drop table if exists meals;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
  id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name             VARCHAR                 NOT NULL,
  email            VARCHAR                 NOT NULL,
  password         VARCHAR                 NOT NULL,
  registered       TIMESTAMP DEFAULT now() NOT NULL,
  enabled          BOOL DEFAULT TRUE       NOT NULL,
  calories_per_day INTEGER DEFAULT 2000    NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR,
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE meals
(
          id integer primary key default nextval('global_seq'),
          date_time timestamp without time zone NOT NULL,
          description varchar NOT NULL,
          calories integer NOT NULL,
          food_owner_id integer not null,

          constraint meals_food_owner_id_user_id_fk foreign key (food_owner_id) references users (id) match simple
                on update no action on delete cascade
 );
CREATE UNIQUE INDEX meals_date_time_owner_unique_idx ON meals ( food_owner_id, date_time);