#!/bin/bash

WORKDIR=../../../

cp -f bombagentNorm.asl bombagent.asl

export CLASSPATH=.:${WORKDIR}/lib/nuBDI.jar:${WORKDIR}/lib/jason-csp.jar:${WORKDIR}/lib/jason.jar

for BLAH in {1..10} ; do
    printf "MAS bombworldt { \n infrastructure: Centralised \n environment: BombEnvironment(bombworld%s.properties,norms) \n agents: bombagent agentClass edu.meneguzzi.nubdi.agent.nu.NuAgent; \n}" "$BLAH" > bombWorldT.mas2j ;
    java -cp $CLASSPATH -Xms512m -Xmx512m jason.infra.centralised.RunCentralisedMAS bombWorldT.mas2j ;
done

rm bombWorldT.mas2j
rm .stop___MAS