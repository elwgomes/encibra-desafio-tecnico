FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean test

RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/desafio-0.0.1-SNAPSHOT.jar /app/desafio-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "desafio-0.0.1-SNAPSHOT.jar"]
