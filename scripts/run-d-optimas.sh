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

# Dynamic discovery of seed nodes for Docker Swarm
SERVICE_NAME="d-optimas"
# Get IPs of all tasks in the swarm service
TASK_IPS=$(getent hosts tasks.${SERVICE_NAME} | awk '{print $1}')

SEED_NODES=""
COUNTER=0
if [ -n "$TASK_IPS" ]; then
  echo "Found Swarm tasks for ${SERVICE_NAME}:"
  for IP in $TASK_IPS; do
    echo "  - ${IP}"
    SEED_NODES="${SEED_NODES} -Dakka.cluster.seed-nodes.${COUNTER}=akka://d-optimas@${IP}:2551"
    COUNTER=$((COUNTER + 1))
  done
else
  echo "Swarm task discovery returned no IPs. Falling back to DOPTIMAS_HOST if set."
  if [ -n "$DOPTIMAS_HOST" ]; then
     SEED_NODES="-Dakka.cluster.seed-nodes.0=akka://d-optimas@${DOPTIMAS_HOST}:2551"
  fi
fi

JVM_OPTS="-Dconfig.file=${CONFIG_FILE} -Djava.library.path=${COCO_LIB} -Dlog4j.configurationFile=log4j2.xml ${SEED_NODES}"
JVM_OPTS="${JVM_OPTS} --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-exports java.base/jdk.internal.misc=ALL-UNNAMED"

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

  echo "compressing coco data"
  tar -czf coco-data-$i.tar exdata/
  echo "moving coco data to host dir"
  mv coco-data-$i.tar /data
  rm -rf ./exdata
done
