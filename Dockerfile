FROM maven:3.3-jdk-8
MAINTAINER toonsevrin@gmail.com


ARG AWS_ACCESS_KEY_ID
ENV AWS_ACCESS_KEY_ID ${AWS_ACCESS_KEY_ID}
ARG AWS_SECRET_KEY
ENV AWS_SECRET_KEY ${AWS_SECRET_KEY}
ARG AWS_REGION
ENV AWS_REGION ${AWS_REGION}

COPY . /usr/src/server/
COPY serverarchitect /usr/bin/
COPY startserver /usr/bin/
ENV SERVERDIR /usr/src/server/
WORKDIR /usr/src/server/
RUN chmod +x /usr/bin/serverarchitect; chmod +x /usr/bin/startserver; 
RUN mvn package
