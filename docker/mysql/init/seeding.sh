#!/bin/bash

set -e

script_root_dir="init-db"

db_name="demo"

db_sqls=(
  "$db_name/origin.sql"
)

sqlExecute() {
	path=$1
	shift
	sqls=("${@}")

	for file in "${sqls[@]}"
	do
		echo "- import: /$path/$file"
		mysql --default-character-set=utf8 -uroot -p${MYSQL_ROOT_PASSWORD} < "/$path/$file"
	done
}

sqlExecute $script_root_dir "${db_sqls[@]}"
