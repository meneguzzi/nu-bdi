#!/bin/bash

WORKDIR=../../../

export CLASSPATH=.:${WORKDIR}/bin:${WORKDIR}/lib/jason-csp.jar:${WORKDIR}/lib/jason.jar

java -cp ${CLASSPATH} MakeBombWorld 60 bombworldNew