FROM gradle:8.10-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM eclipse-temurin:17-jre
EXPOSE 1337
RUN mkdir /app
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/fcgi-server.jar
ENTRYPOINT ["java", "-DFCGI_PORT=1337", "-jar", "fcgi-server.jar"]