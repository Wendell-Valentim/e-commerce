FROM maven:3.9.14-eclipse-temurin-25 as build
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM amazoncorretto:25.0.2

WORKDIR /app

COPY --from=build ./build/target/*.jar ./app.jar

ENTRYPOINT java -jar app.jar