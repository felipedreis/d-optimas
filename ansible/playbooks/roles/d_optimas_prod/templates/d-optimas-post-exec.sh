#!/bin/bash

cd {{ work_dir }}

doptimas_running=`docker wait d-optimas-1`

if [[ "$doptimas_running" == "0"  ]]; then
  echo "d-optimas finished successfully"
  docker-compose down
  rm -rf results/d-optimas-finished
  tar -czf {{ experiment_instance  }}.tar.gz results/*
  gsutil cp {{ experiment_instance }}.tar.gz gs://{{ experiment_name }}
  sudo poweroff
fi

