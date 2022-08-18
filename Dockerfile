FROM eclipse-temurin:11-jdk-alpine

WORKDIR /app/build
COPY . .
RUN ./gradlew --no-daemon bootWar

FROM eclipse-temurin:11-jre-alpine

WORKDIR /app/prod

COPY --from=0 /app/build/build/libs/*.war app.war

EXPOSE 8080

CMD ["java", "-jar", "app.war"]