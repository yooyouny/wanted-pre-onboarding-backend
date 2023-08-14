FROM openjdk:17-ea-11-jdk-slim
ARG JAR_FILE=/build/libs/app.jar
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asiz/Seoul
ENTRYPOINT ["java", "-jar", "./app.jar"]
EXPOSE 8080