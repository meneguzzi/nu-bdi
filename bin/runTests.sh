#!/bin/bash

WORKDIR=../../../

cp -f bombagentNonNorm.asl bombagent.asl

DOMAINPREFIX=bombworldNew
MAXBOMBS=40

export CLASSPATH=.:${WORKDIR}/bin:${WORKDIR}/lib/jason-csp.jar:${WORKDIR}/lib/jason.jar

for number in {1..40} ; do
printf "MAS bombworldt { \n infrastructure: Centralised \n environment: BombEnvironment(%s%s.properties) \n agents: bombagent; \n}" "${DOMAINPREFIX}" "${number}" > bombWorldT.mas2j ;
java -cp $CLASSPATH -Xms1g -Xmx1g jason.infra.centralised.RunCentralisedMAS bombWorldT.mas2j ;
done

rm bombWorldT.mas2j
rm .stop___MAS
