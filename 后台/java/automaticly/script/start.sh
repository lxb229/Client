#!/bin/bash
#create by neo
#获取执行上一级目录，目的是为了获取程序的根目录
EARLYWARNING_HOME=$(dirname $(pwd))
LOG4J_LOGPATH=$EARLYWARNING_HOME/logs
CONFPATH=$EARLYWARNING_HOME/conf
RUNJAR=$EARLYWARNING_HOME/${project.build.finalName}.jar
#some properties which need by app
PROPERTIES_LOG4J_LOGPATH=log4j.logPath
#run!
#命令结尾的`&`表示后台运行程序
java -Xbootclasspath/a:$CONFPATH -D$PROPERTIES_LOG4J_LOGPATH=$LOG4J_LOGPATH -jar $RUNJAR &