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

SCRIPT_DIR=$(dirname $0)
deploy_api_home=${SCRIPT_DIR}/..
deploy_api_home=`realpath $deploy_api_home`
deploy_log_home=${deploy_api_home}/logs

DEPLOY_JAVA_OPTS="-Dspring.profiles.active=dev"
#DEPLOY_JAVA_OPTS="${DEPLOY_JAVA_OPTS} -Dspring.config.location=file:config/"
DEPLOY_JAVA_OPTS="${DEPLOY_JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom"
DEPLOY_JAVA_OPTS="${DEPLOY_JAVA_OPTS} -Duser.timezone=Asia/Seoul"

deploy_api_name="demo-api"
deploy_api_version="1.0"

tail_log="no"

OIFS="$IFS"
for i in $*
do
    echo $i
    if [ "${i}" == "-t" ]
    then
        tail_log="yes"
    elif [ "${i}" == "-init" ]
    then
        DEPLOY_JAVA_OPTS="${DEPLOY_JAVA_OPTS} -Dportal.init=true"
    else
        IFS='='
        read -a params <<< "${i}"
        paramKey="${params[0]}"
        paramValue="${params[1]}"
        if [ "${paramKey}" == "-v" ]
        then
            deploy_api_version="${paramValue}"
        fi
    fi
done
IFS="$OIFS"

echo "version = ${deploy_api_version}"

mkdir -p ${deploy_log_home}

header "kill demo api"
deploy_pid=`ps -ef | grep java | grep ${deploy_api_name} | awk '{ print $2 }'`

if [ -z $deploy_pid ]
then
    echo " no demo api process"
else
    echo " kill pid [${deploy_pid}]"
    kill -9 ${deploy_pid}
fi

pushd ${deploy_api_home} > /dev/null
header "mvn clean install -- api"
mvn clean install -Dmaven.test.skip=true
popd > /dev/null
sleep 1

header "run demo api"

export DEPLOYAPI_LOGDIR=$deploy_log_home

pushd ${deploy_api_home} > /dev/null
nohup java -jar ${DEPLOY_JAVA_OPTS} ${deploy_api_home}/target/${deploy_api_name}-${deploy_api_version}.jar >/dev/null &
sleep 2
popd > /dev/null

deploy_pid=`ps -ef | grep java | grep ${deploy_api_name} | awk '{ print $2 }'`
echo "run demo api [${deploy_pid}]"


header "api log started"
