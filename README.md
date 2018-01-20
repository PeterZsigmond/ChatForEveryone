# Welcome to ChatForEveryone!

Hi! I'm Zsigmond Péter, and this is my first **Spring Boot** project. This is a little chat webapplication, with some REST webservice, written in JAVA.


## Install
You will need: 

 - Java 8
 - Maven
 ---
 - Download
 - Run: `mvn clean install`
 - Now you only need two files: `application.properties` and `target/chatforeveryone-0.1.jar`. Put them in the same directory. You can delete the other files, if you want.
 -  Now configure the `application.properties`. See below.
 - Run: `java -jar chatforeveryone-0.1.jar`
### Configure application.properties
 - Before you first start the program, set the `spring.jpa.hibernate.ddl-auto=` to `create` mode. This will build the database structure. Then stop the program, and rewrite this field to `update`.
---
 - `server.port=` The port that the server is running on. 
 ---
 - `spring.datasource.url=` Set this with the following rules: `jdbc:mysql://DB_SERVER_URL:PORT/DB_NAME`
 - `spring.datasource.username/password=` MySQL username/password.
 ---
 - `email.enable.sending=` Set this to `true`, to send e-mails. (Only use `false` for testing.)
 - `email.server.full.address=` The URL of the server. This will be sent in the email.
 - `spring.mail.host: smtp.gmail.com` The mail server.
 - `spring.mail.port: 25` The mail server's port.
 - `spring.mail.username/password:` The username/password for the email account.
---
### TODO:

- Kód rendezés.
- JS kód rendezés.
- CSS rendezés, és formázás.
- **KÉSZ** - Back-end validálás.
- **KÉSZ** - Div class státusz helyére az emailt, jobb oldalt meg a count helyére.
- Rest api javítása: utolsó üzeneteket kérje csak le.
- Email küdés másik szálon fusson.
- **KÉSZ** - Message-nel ne emailt írjon, hanem hogy ki küldte és dátum.
- **KÉSZ** - API-kat a /api/ -ra.
- **KÉSZ** - Hiba oldal.
- Mindennek változónak értelmes név, és programban csak angol.

Later...
- i18n
- Profile pic a usereknek.


### Thank you for reading.

