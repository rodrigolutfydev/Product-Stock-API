CREATE TABLE categories (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            name VARCHAR(100) NOT NULL,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            product_count INT NOT NULL DEFAULT 0,
                            PRIMARY KEY (id)
);