#!/bin/bash

CONFIG_FILE=$1
CASSANDRA_HOST=$2
DOPTIMAS_EXP_FILE=$3
REPEAT=$4

sleep 300

echo "welcome to the docker run of d-optimas, this experiment ${DOPTIMAS_EXP_FILE} will run ${REPEAT} times in a clean environment"
for i in `seq 1 ${REPEAT}`; do

  echo "cleaning up previous akka cluster data"
  rm -rf ddata-d-optimas-replicator-*

  echo "starting simulation number ${i}"
  java -Dconfig.file=$CONFIG_FILE  -jar ./d-optimas.jar -host ${CASSANDRA_HOST} -config ${DOPTIMAS_EXP_FILE}
  echo "starting data extraction"
  java -Dconfig.file=$CONFIG_FILE -jar ./d-optimas.jar -host ${CASSANDRA_HOST} -config ${DOPTIMAS_EXP_FILE} -extract
  exp_result_folder=${DOPTIMAS_EXP_FILE}-$i
  mkdir -p $exp_result_folder
  echo "copying data to host dir ${exp_result_folder}"
  mv ./data/* $exp_result_folder
  echo "D-Optimas finished successfully"
  mv $exp_result_folder /data
  rm -rf ./data
done
