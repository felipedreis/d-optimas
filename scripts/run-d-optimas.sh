#!/bin/bash

CONFIG_FILE=$1
CASSANDRA_HOST=$2
DOPTIMAS_EXP_FILE=$3
REPEAT=$4


while ! bash -c "</dev/tcp/${CASSANDRA_HOST}/9042 &>/dev/null"; do
  echo "waiting for cassandra";
  sleep 5
done

sleep 30

ls /d-optimas/native

JVM_OPTS="-Dconfig.file=${CONFIG_FILE} -Djava.library.path=${COCO_LIB} -Dlog4j.configurationFile=log4j2.xml"

echo "welcome to the docker run of d-optimas, this experiment ${DOPTIMAS_EXP_FILE} will run ${REPEAT} times in a clean environment"
for i in `seq 1 ${REPEAT}`; do

  echo "cleaning up previous akka cluster data"
  rm -rf ddata-d-optimas-replicator-*

  echo "starting simulation number ${i}"
  java $JVM_OPTS -jar ./d-optimas.jar -host ${CASSANDRA_HOST} -config ${DOPTIMAS_EXP_FILE}
  echo "starting data extraction"
  java $JVM_OPTS -jar ./d-optimas.jar -host ${CASSANDRA_HOST} -config ${DOPTIMAS_EXP_FILE} -extract
  exp_result_folder=${DOPTIMAS_EXP_FILE}-$i
  mkdir -p $exp_result_folder
  echo "copying data to host dir ${exp_result_folder}"
  mv ./data/* $exp_result_folder
  echo "D-Optimas finished successfully"
  mv $exp_result_folder /data
  rm -rf ./data
done
