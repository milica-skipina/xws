FROM maven:3.6.3-ibmjava-8-alpine as tracking

WORKDIR /usr/src/tracking
COPY . .
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY --from=tracking /usr/src/tracking/target/tracking-0.0.1-SNAPSHOT.jar ./

EXPOSE 8088

CMD ["java", "-jar", "tracking-0.0.1-SNAPSHOT.jar"]
