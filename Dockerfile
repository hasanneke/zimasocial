FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/zimasocial-0.0.1-SNAPSHOT.jar zimasocial.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "zimasocial.jar", "--spring.profiles.active=dev"]