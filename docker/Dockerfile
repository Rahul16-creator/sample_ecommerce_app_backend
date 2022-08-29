FROM openjdk:11
WORKDIR /app
COPY . .
RUN ./gradlew clean
ENTRYPOINT ["./gradlew", "bootRun"]