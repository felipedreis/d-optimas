#!/bin/bash
USER_NAME='cassandra'
PASSWORD='cassandra'

while ! bash -c "</dev/tcp/cassandra/9042 &>/dev/null"; do
  echo "waiting for cassandra";
  sleep 5
done

for cql_file in ./tmp/cql/*.cql;
do
  cqlsh cassandra -u "${USER_NAME}" -p "${PASSWORD}" -f "${cql_file}" ;
  echo "########################Script ""${cql_file}"" executed!!!########################"
done
echo "########################Execution of SH script is finished!########################"
echo "########################Stopping temporary instance!########################"
