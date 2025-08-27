# Etapa de build (JDK 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn -DskipTests clean package

# Etapa de runtime (JRE 21)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render usa PORT (Spring lo leerá si tienes server.port=${PORT:8080})
ENV PORT 8080
EXPOSE 8080

CMD ["sh","-c","java -jar app.jar"]
