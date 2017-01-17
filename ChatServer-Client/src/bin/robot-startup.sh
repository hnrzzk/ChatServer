#!/bin/bash
currentpath="$(cd "$(dirname "$0")" && pwd)"
echo $currentpath

basepath=$currentpath"/../"

libsDir=$basepath"libs/*"

export CLASSPATH=
for file in $libsDir
do
        CLASSPATH=$CLASSPATH:$file
done

CLASSPATH=${CLASSPATH#*:}

configDir=$basepath"/config/"
java -Xmx2g -cp $CLASSPATH com.prefect.chatserver.client.Robot
