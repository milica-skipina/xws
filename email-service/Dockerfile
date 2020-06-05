FROM maven:3.6.3-ibmjava-8-alpine AS email-service
WORKDIR /usr/src/email-service
COPY  . .
RUN ["mvn", "package", "-DskipTests"]


FROM openjdk:8-jdk-alpine
WORKDIR /app
EXPOSE 8086
COPY --from=email-service /usr/src/email-service/target/email-service-0.0.1-SNAPSHOT.jar  ./

CMD ["java", "-jar", "email-service-0.0.1-SNAPSHOT.jar"]