FROM maven:3.6.3-ibmjava-8-alpine as zuul

WORKDIR /usr/src/zuul
COPY . .
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY --from=zuul /usr/src/zuul/target/zuul.jar ./

EXPOSE 8082

CMD ["java", "-jar", "zuul.jar"]
