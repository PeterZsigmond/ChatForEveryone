# Welcome to ChatForEveryone!

Hi! I'm Péter Zsigmond, and this is my first **Spring Boot** project. This is a chat web application, with REST web service.

## Requirements
 - Java Development Kit 8
 - Maven
 
## Install
 - Run: `mvn clean install`
 - Create database `chatforeveryone` (or as you named it in the properties file).
 - Configure `application.properties`. See below.
 - Now you only need two files: `application.properties` and `target/chatforeveryone-1.0.jar`. Put them in the same directory. You can delete the other files, if you want.
 - Run: `java -jar chatforeveryone-1.0.jar`
 
### Configuring `application.properties`
 - At first start set the `spring.jpa.hibernate.ddl-auto=` to `create` mode. This will build the database structure. Then stop the program, and rewrite this field to `update`.
 - `server.port=` The port that the server is running on.
 - `spring.datasource.url=` Set this with the following rules: `jdbc:mysql://DB_SERVER_URL:PORT/DB_NAME?characterEncoding=utf8`
 - `spring.datasource.username/password=` MySQL username/password.
 - `email.enable.sending=` Set this to `true`, to send e-mails. (Only use `false` for testing.)
 - `email.server.full.address=` The URL of the server. This will be sent in the email.
 - `spring.mail.host: smtp.gmail.com` The mail server.
 - `spring.mail.port: 25` The mail server's port.
 - `spring.mail.username/password:` The username/password for the email account.

## Thank you for reading.

