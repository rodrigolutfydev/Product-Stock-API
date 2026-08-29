CREATE TABLE products (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(50) NOT NULL,
                          price DECIMAL(10,2) NOT NULL,
                          stock INT NOT NULL DEFAULT 0,
                          active BOOLEAN NOT NULL DEFAULT TRUE,
                          category_id BIGINT,
                          PRIMARY KEY (id),
                          CONSTRAINT fk_product_category
                              FOREIGN KEY (category_id) REFERENCES categories(id)
);