#!/bin/bash

WORKDIR=../../../

cp -f bombagentNonNorm.asl bombagent.asl

export CLASSPATH=.:${WORKDIR}/bin:${WORKDIR}/lib/jason-csp.jar:${WORKDIR}/lib/jason.jar

for BLAH in {1..10} ; do
printf "MAS bombworldt { \n infrastructure: Centralised \n environment: BombEnvironment(bombworld%s.properties) \n agents: bombagent; \n}" "$BLAH" > bombWorldT.mas2j ;
java -cp $CLASSPATH -Xms512m -Xmx512m jason.infra.centralised.RunCentralisedMAS bombWorldT.mas2j ;
done

rm bombWorldT.mas2j
rm .stop___MAS
