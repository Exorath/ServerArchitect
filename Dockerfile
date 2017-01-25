FROM maven:3.3-jdk-8
MAINTAINER toonsevrin@gmail.com

COPY . /usr/src/server/
COPY runarchitect /usr/bin/
ENV serverdir /usr/src/server/
WORKDIR /usr/src/server/
RUN mvn package
