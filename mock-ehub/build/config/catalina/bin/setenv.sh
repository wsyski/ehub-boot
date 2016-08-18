:
#CATALINA_HOME=/usr/local/tomcat
#export CATALINA_HOME
#unset JAVA_HOME
#JRE_HOME=/usr/local/java/jre
#export JRE_HOME
#JAVA_HOME=/usr/local/java/jdk
#export JAVA_HOME
#LC_CTYPE=iso_8859_1
#export LC_CTYPE
CATALINA_OUT=${CATALINA_HOME}/logs/arena.log
export CATALINA_OUT
JAVA_OPTS="${JAVA_OPTS} -DprocessName=mock-ehub -Djava.util.logging.config.file=${CATALINA_HOME}/common/classes/logging.properties -Dlog4j.configuration=log4j.properties -server -Dfile.encoding=utf-8 -Xms256m -Xmx1024m"
export JAVA_OPTS
CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote -Djava.rmi.server.useLocalHostname=true -Dcom.sun.management.jmxremote.port=16526 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
export CATALINA_OPTS


