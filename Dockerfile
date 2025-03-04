FROM openjdk:17-jdk-alpine
MAINTAINER capo.com
COPY target/autenticacion-0.0.1-SNAPSHOT.jar asignacion_autenticacion.jar
ENTRYPOINT ["java","-jar","/asignacion_autenticacion.jar"]
