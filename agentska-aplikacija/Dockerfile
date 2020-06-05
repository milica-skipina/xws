FROM node as frontend
WORKDIR /app/frontend
COPY frontend .
RUN npm ci
RUN npm run-script build

FROM maven:3.6.3-ibmjava-8-alpine as backend
WORKDIR /app/backend
COPY backend .
RUN mkdir -p src/main/resources/static
COPY --from=frontend /app/frontend/build src/main/resources/static
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=backend /app/backend/target/agent.jar ./
EXPOSE 8099
CMD ["java", "-jar", "agent.jar"]
