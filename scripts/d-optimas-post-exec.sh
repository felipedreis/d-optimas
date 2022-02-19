#!/bin/bash

doptimas_running=`docker container ls | grep d-optimas`

if [[ "$doptimas_running" == ""  ]]; then
  echo "d-optimas stopped. zipping files"
  docker-compose down
  tar -czf {{ experiment_instance  }}.tar.gz results/*
  gsutil cp {{ experiment_instance }}.tar.gz gs://{{ experiment_name }}
else
  echo "d-optimas is still running"
fi

