FROM maven:3.6.3-ibmjava-8-alpine AS appOrders
WORKDIR /usr/src/orders-microservice
COPY  . .
RUN ["mvn", "package", "-DskipTests"]


FROM openjdk:8-jdk-alpine
WORKDIR /app
EXPOSE 8086
COPY --from=appOrders /usr/src/orders-microservice/target/orders-microservice-0.0.1-SNAPSHOT.jar ./

CMD ["java", "-jar", "orders-microservice-0.0.1-SNAPSHOT.jar"]
