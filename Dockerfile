FROM gradle:8.10.2-jdk21 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./

RUN gradle dependencies --no-daemon

COPY . /app

RUN gradle clean build --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/sj-review-service.jar

EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "sj-review-service.jar"]