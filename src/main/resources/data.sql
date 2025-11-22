DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  role VARCHAR(250) NOT NULL,
  otp_code VARCHAR(250),
  otp_expiration_date TIMESTAMP
);

INSERT INTO employees (first_name, last_name, email, password, role, otp_code, otp_expiration_date) VALUES
  ('Laurent', 'GINA', 'laurentgina@mail.com', 'laurent', 'EMPLOYEE', null, null),
  ('Sophie', 'FONCEK', 'sophiefoncek@mail.com', 'sophie', 'EMPLOYEE', null, null),
  ('Bill','Gates','bill@gates.com','$2a$10$sZBJuZNSxo62yFw3wEeL7.vj4aV1C.bbTkfHENj3fNTAZGKEFHei2','MANAGER', null, null),
  ('Agathe', 'FEELING', 'agathefeeling@mail.com', 'agathe', 'EMPLOYEE', null, null);