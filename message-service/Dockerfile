FROM maven:3.6.3-ibmjava-8-alpine as messages

WORKDIR /usr/src/messages
COPY . .
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY --from=messages /usr/src/messages/target/messages.jar ./

EXPOSE 8085

CMD ["java", "-jar", "messages.jar"]
