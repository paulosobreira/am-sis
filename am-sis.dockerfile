FROM eclipse-temurin:21-jre
MAINTAINER Paulo Sobreira
WORKDIR /app
COPY target/am-sis.jar /app/am-sis.jar
EXPOSE 8080
CMD ["java", "-jar", "am-sis.jar"]
