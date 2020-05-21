FROM maven:3.6.3-ibmjava-8-alpine as auth

WORKDIR /usr/src/auth
COPY . .
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY --from=auth /usr/src/auth/target/auth-service.jar ./

EXPOSE 8084

CMD ["java", "-jar", "auth-service.jar"]
