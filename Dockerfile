
FROM openjdk:17-jdk-slim

WORKDIR /app

RUN ls -R
#Copy the JAR file into the container
COPY target/TalentFindr-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

#command to start the application
ENTRYPOINT ["java", "-jar", "app.jar"]