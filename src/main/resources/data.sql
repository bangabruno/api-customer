CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT true
);

INSERT INTO users (id, username, password, enabled)
  VALUES (1, 'user', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', true);

CREATE TABLE authorities (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(100) NOT NULL,
  FOREIGN KEY (username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_customers ON authorities (username, authority);

INSERT INTO authorities (id, username, authority)
  VALUES (DEFAULT, 'user', 'ROLE_CUSTOMER');

CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    active BOOLEAN DEFAULT true NOT NULL,
    favorites_products JSONB
);

CREATE INDEX idx_favorites_products_id ON customers ((favorites_products->>'id'));