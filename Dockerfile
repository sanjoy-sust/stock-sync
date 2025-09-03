FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/stock-sync-0.0.1-SNAPSHOT.jar app.jar
COPY tmp/vendor-b/stock.csv tmp/vendor-b/stock.csv
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]