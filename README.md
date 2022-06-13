# GigaSpaces Debug Project

## Maven Build
First build this Maven project


## Install PU Common JARs
Unzip the "pu-common" JAR file that is built by this project into the "lib/optional/pu-common" folder.


## Hibernate NPE
Corresponding support ticket: 6820

### Start Grid
Start the GigaSpaces grid:

```
scripts/hibernate-npe.sh grid

```

### Deploy PU
Deploy the PU:

```
scripts/hibernate-npe.sh deploy

```


### Test Client

