FROM openjdk:11 AS run

WORKDIR /d-optimas/run

COPY ../config/docker-config.conf ./
COPY ../experiments ./experiments/
COPY target/*-deploy.jar ./d-optimas.jar
COPY ../scripts/run-d-optimas.sh ./

RUN apt update
RUN apt install -y liblmdb-dev

ENV CONFIG_FILE="docker-config.conf"
ENV REPEAT=1
ENTRYPOINT ./run-d-optimas.sh ${CONFIG_FILE} ${CASSANDRA_HOST} ${DOPTIMAS_EXP_FILE} ${REPEAT}
