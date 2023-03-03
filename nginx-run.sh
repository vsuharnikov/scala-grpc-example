#!/usr/bin/env bash
DIR="$( cd "$( dirname "$0" )" && pwd )"
cd "${DIR}" || exit

nginx -c "${DIR}/nginx.conf" -p "${DIR}" -e "${DIR}/_runtime/nginx-error.log"
