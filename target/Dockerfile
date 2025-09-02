# Simple Dockerfile (JDK 17)
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
ARG JAR_FILE=target/patika-air-quality-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]