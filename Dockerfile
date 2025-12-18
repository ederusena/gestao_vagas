FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y maven openjdk-17-jdk
COPY . .
RUN mvn clean install

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /target/gestao_vagas-0.0.1-SNAPSHOT.jar /app/gestao_vagas.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "gestao_vagas.jar"]