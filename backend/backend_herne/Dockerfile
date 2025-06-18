FROM maven:3.9-amazoncorretto-21 AS builder

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.7

WORKDIR /app

COPY --from=builder /app/target/backend_herne-0.0.1-SNAPSHOT.jar ./app.jar

CMD ["java", "-jar", "app.jar"]