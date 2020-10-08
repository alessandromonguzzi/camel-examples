# Use camel-sql to connect to MySQL database using transactions

## Setup MySQL

```
sudo podman run --name mysql -e MYSQL_ROOT_PASSWORD=secretpassword -e MYSQL_USER=devuser -e MYSQL_PASSWORD=devdb -e MYSQL_DATABASE=devdb -p 3306:3306 -d mysql:5.6

sudo podman exec -it mysql bash

#now inside the mysql docker, grant privileges to the users

mysql --password devdb
#enter root password when prompted

use devdb;

#create your database schema here
create table myTable(name varchar(255));

#grant permissions to remote users to connect to the database
create user 'devuser'@'localhost' identified by 'devpassword';
grant all on devdb.* to 'devuser'@'localhost' identified by 'devpassword';
grant all on devdb.* to 'devuser'@'%' identified by 'devpassword';
flush privileges;
```

Note the double grant command that are needed to allow remote communication to the database. 
Instead of `grant all` specify the correct permissions for specific users.

## Run Test application

1. Compile

```
mvn clean install
```

2. Install war file on JBoss EAP

3. Test the REST services:

```
#insert row:
http://localhost:8080/camel-sql-mysql/camel/restsvc/insert

#test rollback
http://localhost:8080/camel-sql-mysql/camel/restsvc/rollbackExample

#remove all rows from table
http://localhost:8080/camel-sql-mysql/camel/restsvc/deleteAllTable

```

## NOTES
1. Verify that the MySQL Engine supports transactions. See [Solution](https://access.redhat.com/solutions/5471771) for details.
2. Update the MySQL connector dependency in pom.xml to be aligned with the used MySQL database version.
3. [Alternative MySQL 5.1 docker image](https://hub.docker.com/r/vsamov/mysql-5.1.73) - by default MyISAM Engine is used and transactions do not work!