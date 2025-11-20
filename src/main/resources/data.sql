DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  role VARCHAR(250) NOT NULL
);

INSERT INTO employees (first_name, last_name, email, password, role) VALUES
  ('Laurent', 'GINA', 'laurentgina@mail.com', 'laurent', 'EMPLOYEE'),
  ('Sophie', 'FONCEK', 'sophiefoncek@mail.com', 'sophie', 'EMPLOYEE'),
  ('Bill','Gates','bill@gates.com','$2a$10$sZBJuZNSxo62yFw3wEeL7.vj4aV1C.bbTkfHENj3fNTAZGKEFHei2','MANAGER'),
  ('Agathe', 'FEELING', 'agathefeeling@mail.com', 'agathe', 'EMPLOYEE');