FROM openjdk:11-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]./mvnw pacote && java -jar target/gs-spring-boot-docker-0.1.0.jar