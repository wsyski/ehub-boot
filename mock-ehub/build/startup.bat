@echo on #off
set TITLE=mock-eHUB
setlocal
set CATALINA_VERSION=7.0.52
set CATALINA_HOME=target\tomcat-%CATALINA_VERSION%\tomcat
set JAVA_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=7991

IF "%1"=="autotest" set JAVA_OPTS=%JAVA_OPTS% -DalmaDir=%CATALINA_HOME%\autotest

set PATH=%CATALINA_HOME%\bin;%PATH%
call %CATALINA_HOME%\bin\startup.bat
endlocal
