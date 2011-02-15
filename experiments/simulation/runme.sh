#!/bin/bash
for I in {1..100} ; do
swipl -L0 -G0  -T0 -A0 -f agent.pl -t "doFullRun(0,8)" > out$I
done
