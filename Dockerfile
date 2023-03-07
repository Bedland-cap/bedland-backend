FROM openjdk:17
LABEL maintainer="pszafran"
WORKDIR /app
COPY /target/bedland-0.0.1-SNAPSHOT.jar /app/bedland.jar
ENTRYPOINT ["java","-jar","bedland.jar"]
EXPOSE 8081
