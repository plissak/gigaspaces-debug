#!/bin/bash

###############################################################################
# Drives testing for Insufficient Data in Class module
###############################################################################


# include common.sh
source `dirname $0`/common.sh


# start grid
if [ "$1" == "grid" ]; then
	start_grid --gsc=3
	exit
fi


# deploy PU
if [ "$1" == "deploy" ]; then
	deploy_pu insufficient-data-pu 2
	deploy_pu secondary-ins-data-pu
	exit
fi


# unknown argument
echo "Must run with either 'grid' or 'deploy' arguments"
