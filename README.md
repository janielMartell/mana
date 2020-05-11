# Mana

The MANA Video Store's new Multi-User GUI based application.

## Dependencies
The project has the following dependencies:
- **Maven**: This is basically a package manager by the Apache Foundation that is used to manage this project's dependencies.
- **JavaFx**: OpenJFX is an open source, next generation client application platform for desktop, mobile and embedded systems built on Java.
- **JMetro**: JMetro is  a theme inspired by Fluent Design and adapted to fit the JavaFX SDK. (A Windows look & feel)
- **HikariCP**: A Fast, simple, reliable. HikariCP is a "zero-overhead" production ready JDBC connection pool. 

## Run the application
First of all, make sure you have `java` (JDK 11), `maven` (3.6), and `mysql` installed, run:

```shell script
mysql --version
# mysql  Ver 14.14 Distrib 5.7.30, for Linux (x86_64) using  EditLine wrapper

java -version
# openjdk version "11.0.7" 2020-04-14

mvn -version
# Apache Maven 3.6.0
```

Now run the `mana.sql` to create the database, tables, stored procedures and user for the application. 


Now that you made sure you have `java`, `maven` installed, and that the `mysql` database is up, run the following command to run the application:

```shell script
mvn clean javafx:run
```
