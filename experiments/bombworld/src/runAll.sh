#!/bin/bash

for BLAH in {1..100} ; do
./runTests.sh
./runTestsNormative.sh
done

ruby genrandom.sh
