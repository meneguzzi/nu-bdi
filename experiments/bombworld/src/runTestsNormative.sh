#!/bin/bash

WORKDIR=~/Documents/workspace/nu-BDI/

export CLASSPATH=.:${WORKDIR}/lib/nu-BDI.jar:${WORKDIR}/lib/jason-csp.jar:${WORKDIR}/lib/jason.jar

for BLAH in {1..10} ; do
    echo "MAS bombworldt {
          infrastructure: Centralised
          environment: BombEnvironment(bombworld$BLAH.properties,norms)
          agents: bombagent agentClass edu.meneguzzi.nubdi.agent.nu.NuAgent; 
    }" > bombWorldT.mas2j ;
    java -Xms512m -Xmx512m jason.infra.centralised.RunCentralisedMAS bombWorldT.mas2j ;
done

rm bombWorldT.mas2j