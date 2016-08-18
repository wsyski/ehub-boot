#!/usr/bin/env bash

#set -v -x
profile=false
jrebel=true
OPTIND=1
while getopts :pr c
do
  case ${c} in
  p)
    profile=true ;;
  r)
    jrebel=false ;;
  :)
    echo "Invalid empty option ${OPTARG} argument"
    exit 1 ;;
  \?) # Invalid option
    echo "Invalid option: ${OPTARG}"
    exit 2 ;;
  esac
done
shift `expr ${OPTIND} - 1`
if [ ${#} -ne 0 ]
then
  echo "Usage startup.sh [-pr]"
  exit 4
fi
scriptDir=$(cd `dirname $0` && pwd)
#echo "scriptDir: ${scriptDir}"
osName=`uname`
killCmd=/bin/kill
killOpts=
if [ ${osName} = 'CYGWIN_NT-6.1' ]
then
  killOpts='-f'
fi
CATALINA_VERSION=7.0.52
CATALINA_HOME=${scriptDir}/target/tomcat-${CATALINA_VERSION}/tomcat
export CATALINA_HOME

processName=mock-ehub
pid=`"${JAVA_HOME}/bin/jps" -l -v | grep "processName=${processName} " | grep -v grep | awk '{printf"%s",$1}'`
if [ -n "${pid}" ]
then
  echo "Killing process pid: ${pid}"
  ${killCmd} -9 ${killOpts} ${pid}
  sleep 3
fi

if [ -n "${YJP_HOME}" -a ${profile} = true ]
then
  CATALINA_OPTS="${CATALINA_OPTS} -agentpath:${YJP_HOME}/bin/linux-x86-64/libyjpagent.so=builtinprobes=none,disabletracing,disablealloc,disablej2ee​ $CATALINA_OPTS"
  export CATALINA_OPTS
else
  JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=7987"
  export JAVA_OPTS
fi
if [ -n "${JREBEL_HOME}" -a ${jrebel} = true ]
then
  #JAVA_OPTS="${JAVA_OPTS} -javaagent:${JREBEL_HOME}/jrebel.jar"
  JAVA_OPTS="${JAVA_OPTS} -agentpath:${JREBEL_HOME}/lib/libjrebel64.so"
fi
rm -f  ${CATALINA_HOME}/logs/*
rm -rf ${CATALINA_HOME}/temp/*
rm -rf ${CATALINA_HOME}/work/*
chmod +x ${CATALINA_HOME}/bin/*.sh

${CATALINA_HOME}/bin/startup.sh


