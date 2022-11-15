#!/bin/bash

###############################################################################
# Insufficient Data in Class module's Primary PU Script
###############################################################################


# include common.sh
source `dirname $0`/common.sh


# start grid
if [ "$1" == "grid" ]; then
	start_grid primary --manager --gsc=2
	exit
fi


# deploy PU
if [ "$1" == "deploy" ]; then
	deploy_pu primary insufficient-data-pu 2
	exit
fi


# unknown argument
echo "Must run with either 'grid' or 'deploy' arguments"
