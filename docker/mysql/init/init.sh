#!/bin/bash

set -e

db_sqls=(
    "init.sql"
    )

sqlExecute() {
	path=$1
	shift
	sqls=("${@}")

	for file in "${sqls[@]}"
	do
		echo "- import: /$path/$file"
		sed "s/MASTER_HOST='[^']*'/MASTER_HOST='$MASTER_HOST'/" /$path/$file \
		  | mysql --default-character-set=utf8 -uroot -p${MYSQL_ROOT_PASSWORD}
	done
}

sqlExecute "init-mysql" "${db_sqls[@]}"
