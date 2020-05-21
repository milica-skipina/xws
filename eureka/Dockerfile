FROM maven:3.6.3-ibmjava-8-alpine as eureka

WORKDIR /usr/src/eureka
COPY . .
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY --from=eureka /usr/src/eureka/target/eureka-1.0.0.jar ./

EXPOSE 8761

CMD ["java", "-jar", "eureka-1.0.0.jar"]

