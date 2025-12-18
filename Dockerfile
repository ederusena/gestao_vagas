FROM eclipse-temurin:17-jdk-alpine AS build

RUN apk add --no-cache maven
COPY . .
RUN mvn clean install

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY --from=build /target/gestao_vagas-0.0.1-SNAPSHOT.jar /app/gestao_vagas.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "gestao_vagas.jar"]