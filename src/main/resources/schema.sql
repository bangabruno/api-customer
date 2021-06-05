DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS customers;

CREATE TABLE users (
  username varchar(50) NOT NULL PRIMARY KEY,
  password varchar(100) NOT NULL,
  enabled boolean not null DEFAULT true
);
  
CREATE TABLE authorities (
  username varchar(50) NOT NULL,
  authority varchar(50) NOT NULL,
  CONSTRAINT foreign_authorities_users_1 foreign key(username) references users(username)
);

CREATE UNIQUE INDEX ix_auth_username on authorities (username,authority);

CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    active BOOLEAN DEFAULT true NOT NULL,
    favorite_products TEXT[]
);