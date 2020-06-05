FROM node as micro-front
WORKDIR /app/micro-front
COPY frontend-microservices .
RUN npm ci
RUN npm run-script build

FROM maven:3.6.3-ibmjava-8-alpine as zuul

WORKDIR /app/zuul
COPY zuul .
RUN mkdir -p src/main/resources/static
COPY --from=micro-front /app/micro-front/build src/main/resources/static
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY --from=zuul /app/zuul/target/zuul.jar ./

EXPOSE 8082

CMD ["java", "-jar", "zuul.jar"]

