use mysql;

CREATE USER IF NOT EXISTS 'general_user'@'%' IDENTIFIED WITH mysql_native_password BY 'test1234';
GRANT INSERT, UPDATE, DELETE, SELECT ON *.* TO 'general_user'@'%';

STOP SLAVE;

CHANGE MASTER TO MASTER_HOST='mysql-main',
MASTER_USER='repl_user',
MASTER_PASSWORD='repl_password';

START SLAVE;
