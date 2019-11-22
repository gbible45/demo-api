#!/bin/bash

set -eu

function header(){
    local title=$1
    echo ""
    echo "###########################################################################################################"
    echo "                                  ${title}"
    echo "###########################################################################################################"
    echo ""
}

deploy_api_name="demo-api"
deploy_api_version="1.0"

echo "version = ${deploy_api_version}"

header "kill demo api"
deploy_pid=`ps -ef | grep java | grep ${deploy_api_name} | awk '{ print $2 }'`

if [ -z $deploy_pid ]
then
    echo " no demo api process"
else
    echo " kill pid [${deploy_pid}]"
    kill -9 ${deploy_pid}
fi
