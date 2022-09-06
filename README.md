# GigaSpaces Debug Project


## Environment Variables
These environment variables must be set first:
* `JAVA_HOME` - JDK installation directory
* `GS_HOME` - GigaSpaces installation directory


## Maven Build
This is a Maven project which must be built:

```
mvn package -Drevision=1.0.0-SNAPSHOT

```
*Beforehand you'll have to update the root POM file (at bottom of file) to reference the appropriate Maven repository or repositories.*


## Install PU Common JARs
Unzip the "pu-common" JAR file that is built by this project into the GigaSpaces installation folder:

```
unzip pu-common/target/pu-common-1.0.0-SNAPSHOT.zip -d ${GS_HOME}/lib/optional/pu-common/

```


## Hibernate NPE
Corresponding support ticket: [6820](https://support2.gigaspaces.com/support/tickets/6820)

To demonstrate this bug, first bring up the grid by running the following script:

```
scripts/hibernate-npe.sh grid

```
Stopping this script will bring the grid down.

Next, in a separate terminal deploy the PUs by running:

```
scripts/hibernate-npe.sh deploy

```

As the PUs deploy, space objects are added to the space and a space iterator is used to read those space objects.  Log files are located under:

```
~/logs/debug

```

The `NullPointerException` and relevant Hibernate debug messages will be located in this file:

```
~/logs/debug/HibernateSecondaryNPE/HibernateSecondaryNPE_1.log

```

The `NullPointerException` appears in **any** remote client of the space object's PU.  That means a simple Java program to read the data will generate the exception.  Run the following program to read the data and attempt to print it:

```
gs.debug.hnpe.client.util.PrintWidgetsMain

```
