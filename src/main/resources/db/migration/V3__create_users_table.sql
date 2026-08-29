CREATE TABLE users (
                       id BIGINT not null AUTO_INCREMENT,
                       login VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id)
);