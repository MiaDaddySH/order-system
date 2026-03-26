FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
ARG JAR_FILE=target/user-order-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xms512m","-Xmx1024m","-jar","/app/app.jar"]
