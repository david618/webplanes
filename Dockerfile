# TAG=v2.0
# docker buildx build --platform=linux/amd64 -t david62243/webplanes:${TAG} .   # Use this line to complie on ARM based Mac
# docker build -t david62243/webplanes:${TAG} .
# docker push david62243/webplanes:${TAG}
#
# Build stage
#

FROM maven:3.8.5-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -DskipTests=true -f /home/app/pom.xml install

#
# Package stage
#
FROM openjdk:22-ea-17-slim
RUN useradd -u 1000 -ms /bin/bash ubuntu

COPY --from=build /home/app/target/webplanes-0.0.1-SNAPSHOT.jar /home/ubuntu/webplanes.jar
USER ubuntu
WORKDIR /home/ubuntu
EXPOSE 8080
ENTRYPOINT ["java","-jar","webplanes.jar"]
