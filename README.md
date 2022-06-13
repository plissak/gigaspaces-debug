# GigaSpaces Debug Project

## Maven Build
This is a Maven project which must be built before testing can begin.


## Install PU Common JARs
Unzip the "pu-common" JAR file that is built by this project into the "lib/optional/pu-common" GigaSpaces installation folder.


## Environment Variables
These environment variables must be set before running any scripts:
* JAVA_HOME - JDK installation directory
* GS_HOME - GigaSpaces installation directory


## Hibernate NPE
Corresponding support ticket: 6820

### Start Grid
Start the GigaSpaces grid:

```
scripts/hibernate-npe.sh grid

```
Stopping this script will bring the grid down.

### Deploy PU
Deploy the PU:

```
scripts/hibernate-npe.sh deploy

```


### Test Client

