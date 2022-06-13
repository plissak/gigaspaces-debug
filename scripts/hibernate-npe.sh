#!/bin/bash

###############################################################################
# Drives testing for Hibernate NPE module
###############################################################################


# include common.sh
source `dirname $0`/common.sh


# start grid
if [ "$1" == "grid" ]; then
	start_grid
	exit
fi


# deploy PU
if [ "$1" == "deploy" ]; then
	deploy_pu hibernate-npe-pu
	exit
fi


# unknown argument
echo "Must run with either 'grid' or 'deploy' arguments"
