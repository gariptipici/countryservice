FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR usr/src/app
COPY . ./
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
ARG JAR_NAME="countryservice-0.0.1-SNAPSHOT"
WORKDIR /usr/src/app
RUN ls
EXPOSE 8080
COPY --from=build /usr/src/app/target/${JAR_NAME}.jar ./countryservice.jar
ENTRYPOINT ["java","-jar","countryservice.jar"]