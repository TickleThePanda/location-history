FROM maven:3-eclipse-temurin-21 AS build-image

ARG APP_DIR=/app
WORKDIR ${APP_DIR}

COPY pom.xml ./
COPY apps/cloudrun/pom.xml ./apps/cloudrun/pom.xml
COPY apps/lambda/pom.xml ./apps/lambda/pom.xml
COPY apps/stats/pom.xml ./apps/stats/pom.xml
COPY apps/web/pom.xml ./apps/web/pom.xml
COPY core/loaders/google/pom.xml ./core/loaders/google/pom.xml
COPY core/mapper/pom.xml ./core/mapper/pom.xml
COPY core/runner/pom.xml ./core/runner/pom.xml

RUN mvn -B dependency:go-offline

COPY apps apps
COPY core core

RUN mvn package

FROM eclipse-temurin:21-alpine

ARG APP_DIR=/app
WORKDIR ${APP_DIR}

COPY --from=build-image /app/apps/cloudrun/target/location-history-run.jar ./
COPY config/log4j2.xml ./

RUN which java

ENTRYPOINT [ "java", "-Dlog4j.configurationFile=./log4j2.xml", "-jar", "location-history-run.jar" ]

