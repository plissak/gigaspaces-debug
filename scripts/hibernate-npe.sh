#!/bin/bash

###############################################################################
# Drives testing for Hibernate NPE module
###############################################################################


# include common.sh
source `dirname $0`/common.sh


# start grid
if [ "$1" == "grid" ]; then
	start_grid generic --manager --gsc=3
	exit
fi


# deploy PU
if [ "$1" == "deploy" ]; then
	deploy_pu generic hibernate-npe-pu
	deploy_pu generic secondary-npe-pu 2
	exit
fi


# undeploy PU
if [ "$1" == "undeploy" ]; then
	undeploy_pu generic secondary-npe-pu
	undeploy_pu generic hibernate-npe-pu
	exit
fi


# unknown argument
echo "Must run with either 'grid' or 'deploy' arguments"
