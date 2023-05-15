FROM debian:latest AS coco-compile

WORKDIR /coco

COPY ../native/*.c /coco/
COPY ../native/*.h /coco/

RUN apt update
RUN apt install -y openjdk-11-jdk gcc build-essential
#TODO amd arm selector uname -m
ENV JAVA_HOME="/usr/lib/jvm/java-11-openjdk-arm64"
ENV JAVA_INCLUDE_DIR="/usr/lib/jvm/java-11-openjdk-arm64/include"

RUN gcc -I $JAVA_HOME/include -I $JAVA_HOME/include/linux -o libCocoJNI.so -fPIC -shared CocoJNI.c -lm

FROM openjdk:11 AS run

WORKDIR /d-optimas/run

COPY --from=coco-compile /coco/*.so /d-optimas/native/

COPY ../config/docker-config.conf ./
COPY ../experiments ./experiments/
COPY target/*-deploy.jar ./d-optimas.jar
COPY ../scripts/run-d-optimas.sh ./
COPY ../config/log4j2.xml ./

RUN apt update
RUN apt install -y liblmdb-dev

ENV CONFIG_FILE="docker-config.conf"
ENV REPEAT=1
ENV COCO_LIB=/d-optimas/native/

ENTRYPOINT ./run-d-optimas.sh ${CONFIG_FILE} ${CASSANDRA_HOST} ${DOPTIMAS_EXP_FILE} ${REPEAT}
