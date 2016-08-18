REM SET CATALINA_HOME=c:\usr\local\tomcat
REM SET JAVA_HOME=c:\usr\local\java\jdk
REM SET JAVA_HOME=
REM SET JRE_HOME=c:\usr\local\java\jre
SET JAVA_OPTS=%JAVA_OPTS% -DprocessName=mock-ehub -Djava.util.logging.config.file=%CATALINA_HOME%\common\classes\logging.properties -Dlog4j.configuration=log4j.properties -server -XX:+UseParallelGC -Xms256m -Xmx512m -Xss512k -Dfile.encoding=utf-8
SET CATALINA_OPTS=-Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
