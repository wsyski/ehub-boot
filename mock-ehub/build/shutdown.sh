#! /bin/sh

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

chmod +x $CATALINA_HOME/bin/*.sh
$CATALINA_HOME/bin/shutdown.sh

processName=mock-ehub
pid=`"${JAVA_HOME}/bin/jps" -l -v | grep "processName=${processName} " | grep -v grep | awk '{printf"%s",$1}'`
if [ -n "${pid}" ]
then
  echo "Killing process pid: ${pid}"
  ${killCmd} -9 ${killOpts} ${pid}
  sleep 3
fi