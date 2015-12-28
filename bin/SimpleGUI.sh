#!/bin/sh

# JAR setting
DOWNLOAD_DIR=$HOME/workspace

SASAKAMA_JAR=$DOWNLOAD_DIR/Sasakama/jar/Sasakama.jar

GYUTAN_HOME=$DOWNLOAD_DIR/Gyutan
GYUTAN_JAR=$GYUTAN_HOME/jar/Gyutan.jar

SEN_HOME=$GYUTAN_HOME/sen
SEN_LIB=$SEN_HOME/lib
SEN_JAR=$SEN_LIB/sen.jar:$SEN_LIB/commons-logging.jar:$SEN_LIB/junit.jar

SIMPLE_GUI_JAR=$DOWNLOAD_DIR/SimpleGUI/jar/SimpleGUI.jar

# set classpath
CLASSPATH=$SEN_JAR:$SASAKAMA_JAR:$GYUTAN_JAR:$SIMPLE_GUI_JAR

java -Dorg.apache.commons.logging.simplelog.log.net.java=info \
     -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog  \
     -classpath ${CLASSPATH} \
     SimpleGUI
