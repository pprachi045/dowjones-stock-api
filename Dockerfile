FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
COPY target/dowjones-stock-api-0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java", "-jar", "/application.jar"]