#More Size---
#FROM openjdk:11
#WORKDIR /app
#COPY . .
#RUN ./gradlew clean
#ENTRYPOINT ["./gradlew", "bootRun"]

# less size
FROM amazoncorretto:11-alpine-jdk
ADD build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]