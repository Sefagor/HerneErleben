# Event Management Backend

## Installation

* Navigate to the directory with your docker-compose.yaml file then build and run the services

```
docker compose up --build

```

### Some infos

* Port Conflict
    * Open docker-compose.yaml.
    * Locate the ports: section.
    * Change only the first number (before the colon **:** ) to another unused port.

### Spring Boot App

* The Spring Boot application runs by default on *http://localhost:8090*
* You can test it with Swagger

### Herne Backend

* The herner Backend runs by default on *http://localhost:8091*
* You can test it with Swagger

### Frontend

* The Frontend runs by default on *http://localhost:3000*

### Database

* The Database base (PostGreSql) run on the port 5433
    * You can use Dbeaver to test the connection and see the contains of the database

### Mosquitto

### SMTP4Dev

* The development email interface is available at: *http://localhost:5000
* When you call the /events/all endpoint (or trigger any email-sending logic), you should see a new email appear here.

### Swagger

* Swagger can be reached at the addresses
* http://localhost:8090/swagger-ui/index.html (Backend)
* http://localhost:8091/swagger-ui/index.html (Backend herne)
