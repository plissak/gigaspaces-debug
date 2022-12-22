#!/bin/bash

###############################################################################
# Insufficient Data in Class module's Secondary PU Script
###############################################################################


# include common.sh
source `dirname $0`/common.sh


# start grid
if [ "$1" == "grid" ]; then
	start_grid secondary --gsc=1
	exit
fi


# deploy PU
if [ "$1" == "deploy" ]; then
	deploy_pu secondary secondary-ins-data-pu
	exit
fi


# undeploy PU
if [ "$1" == "undeploy" ]; then
	undeploy_pu secondary secondary-ins-data-pu
	exit
fi


# unknown argument
echo "Must run with either 'grid' or 'deploy' arguments"
