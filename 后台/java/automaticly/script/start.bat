@echo off
set  CURRENT=%cd%
cd ..
SET EARLYWARNING_HOME=%cd%
set LOG4J_LOGPATH=%EARLYWARNING_HOME%\logs
set CONFPATH=%EARLYWARNING_HOME%\config
set RUNJAR=%EARLYWARNING_HOME%/${project.build.finalName}.jar
rem some properties which need by app
set PROPERTIES_LOG4J_LOGPATH=log4j.logPath
rem run!
@echo on
@java -Xbootclasspath/a:%CONFPATH% -D%PROPERTIES_LOG4J_LOGPATH%=%LOG4J_LOGPATH% -jar %RUNJAR%