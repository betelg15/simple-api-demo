#!/bin/bash

case $1 in
  replica)
    mysql_host="mysql-replica"
    ;;
  *)
    mysql_host="mysql-main"
    ;;
esac

container_id=$(docker-compose ps -q ${mysql_host} | tr -d '\r')
container_id_short=${container_id:0:12}

echo "$mysql_host:$container_id_short"

docker-compose exec ${mysql_host} tail -f /var/lib/mysql/${container_id_short}.log
