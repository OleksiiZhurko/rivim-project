FROM gradle:7.5.1-jdk11-alpine as build
COPY --chown=gradle:gradle . /usr/local/app/java_backend
WORKDIR /usr/local/app/java_backend
RUN gradle build

FROM openjdk:11-jdk-oracle
COPY --from=build /usr/local/app/java_backend/build/libs/webclient.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/webclient.jar"]