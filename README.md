# Auctioner - online bidding platform - API
__Author:__ Jan Hrbotický (xhrboti1@mendelu.cz)

## Basic information
* This repository holds code for Auctioner API, the online bidding platform. 
* This project is part of diploma thesis diploma thesis at [Mendel University](https://mendelu.cz/en/).
* __Used technologies:__
  * Maven
  * Java 11
  * Spring Boot
  * PostgreSQL
  * Flyway
  * JUnit 5
  * Mapstruct
  * Testcontainers
  * Lombok
  * Docker
  * and others
  
## Installing the app

* __Prerequisities:__
  * Installed [Docker](https://www.docker.com/)
  
* __Instalation:__
  1. Checkout this repository
  1. Start up docker
  1. Create ```.env``` file.
      * It is recommended to use ```.env.example``` as an example (```cp .env.example .env```). In case you do not want to use this file, you have to provide following variables
        * _POSTGRES_JDCBC_ - JDBC connection to database (in example, by deafult, the connection points to dockerized PostgreSQL instance)
        * _POSTGRES_USER_ - User for database login
        * _POSTGRES_PASSWORD_ - Password for given user
        * _TZ_ - App time zone (in example, by default, Europe/Prague)
        * _SERVER_PORT_ - Port where server will accept connections
        * _JWT_SECRET_ - Secret key for JWT encoding and decoding. __Important note:__ the key has to be at lest 128 chars long
        * _JWT_EXPIRATION_ - Expiration of JWT token in miliseconds (In example, by defualt, 60 days)
      * __Important note:__ if you willing to change some default properties in environment file, check later one the front-end part that may need the same changes
  1. Run ```docker-compose up``` command
  1. __Thats it!__ Application is running on ```localhost:8090``` (In case you have not changed the port of an app)
      * To prove application is up and running, you can send REST request ```GET http://localhost:8090/hello/unauthorized```
      * Swagger OpenAPI v3 is reachable on address ```http://localhost:8090/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/```   
  1. To stop application, simply run ```docker-compose down```
   
  
