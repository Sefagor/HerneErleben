# Event Management Backend

## About My Role

I was mainly responsible for the **frontend development** of this project.  
In addition, I actively contributed to the **backend**, especially in implementing data persistence, connecting the REST API endpoints with the database, and working on the `Controller` classes.  
Through this project, I gained strong practical experience with the **Spring Boot** architecture and its integration with RESTful services and PostgreSQL.


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

### Events Management App

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
* The MQTT broker is available at: mqtt://localhost:1883
### SMTP4Dev

* The development email interface is available at: *http://localhost:5000
* When you call the /events/all endpoint (or trigger any email-sending logic), you should see a new email appear here.

### Swagger

* Swagger can be reached at the addresses
* http://localhost:8090/swagger-ui/index.html (Backend)
* http://localhost:8091/swagger-ui/index.html (Backend herne)

---

### ℹ️ Note: Adding Mock Events

You can easily add mock events for testing purposes using **Swagger UI** at  
**http://localhost:8091/swagger-ui/index.html** (Herne Backend).

Use the `POST /events/send` endpoint and provide a JSON payload like this:

```json
{
  "status": "ACTIVE",
  "herneID": "bbacacbbaabaafffa",
  "eventDate": "2025-07-18",
  "eventLocation": {
    "city": "Herne",
    "street": "Hauptstraße",
    "houseNumber": 12,
    "zip": 44623
  },
  "maxParticipant": 100,
  "eventPhoto": "https://example.com/images/event.jpg",
  "eventDescription": "A fun community event with music, food, and games.",
  "eventName": "Event4",
  "startTime": "14:00",
  "endTime": "18:00",
  "categories": [
    { "name": "Fiesta" }
  ]
}

