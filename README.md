# Nu-BDI

nu-BDI is an interpreter for a normative agent that uses constraints
on plan instantiations to enforce normative stipulations of obligations
and prohibitions. It is implemented as an extension of the popular
Jason interpreter. 

First release: March 2011.
nu-BDI is distributed under LGPL (see file LICENSE).

Java 1.5 is required to run this application, it is available 
at http://java.sun.com.

Library Requirements

- Jason 1.3.1 (https://sourceforge.net/projects/jason/files/jason/version%201.3.1/)
- ANT 1.8.2 (http://ant.apache.org/)
- (optional) Ruby 1.8.7 (http://www.ruby-lang.org/)

In order to compile nu-BDI (in Unix/Mac environments)

1. Ensure you have Java installed 1.5
2. Ensure that the Apache ANT is installed and in the classpath
3. Decompress the distribution file nu-bdi-X.Y.zip to folder <nu-BDI>
4. cd <nu-BDI>
5. ant
6. You should now have "nu-bdi.jar" in the "lib" folder (you need to have this jar included in your Jason project)

To run the tests from the paper

1. Build nu-BDI (as above)
2. Ensure you have Ruby installed 
  -> Only if you want runtime stats aggregated after the tests are run 
     (they will be located in stats.txt and statsNormative.txt)
3. cd experiments/bombworld/src
4. ant
5. You should now have the required binaries in <nu-bdi>/bin
6. chmod u+x *.sh
7. ./createDomains.sh
8. Now you should have about 60 new property files in your current folder
9. ./runAll.sh
10. Wait for a very long time.