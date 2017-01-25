FROM maven:3.3-jdk-8
MAINTAINER toonsevrin@gmail.com

COPY . /usr/src/server/
COPY serverarchitect /usr/bin/
ENV SERVERDIR /usr/src/server/
WORKDIR /usr/src/server/
RUN mvn package
