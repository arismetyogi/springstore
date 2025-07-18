FROM openjdk:21-oracle
COPY target/*.jar springstore-app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "springstore-app.jar"]