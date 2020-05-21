FROM maven:3.6.3-ibmjava-8-alpine as advertisement

WORKDIR /usr/src/advertisement
COPY . .
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY --from=advertisement /usr/src/advertisement/target/advertisements.jar ./

EXPOSE 8083

CMD ["java", "-jar", "advertisements.jar"]
