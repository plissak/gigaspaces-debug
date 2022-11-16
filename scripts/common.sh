#!/bin/bash

###############################################################################
# Common script for module-specific scripts to include
###############################################################################

# Basic Options
export LOC_PORT=4174
export ENABLE_DEBUG=Y
export LOG_FOLDER=~/logs/debug
export GS_CLI_VERBOSE=true


# Validation
if [ "$JAVA_HOME" == "" ]; then
	echo "Missing JAVA_HOME property"
	echo ""
	exit 1
fi
if [ "$GS_HOME" == "" ]; then
	echo "Missing GS_HOME property"
	echo ""
	exit 1
fi


# Grid Options
COMMON_PARAMS="-server -Xss1m -Dcom.gs.multicast.enabled=false -Dcom.gs.security.disable-commit-abort-authentication=true"
JAVA_VERSION=`${JAVA_HOME}/bin/java -version 2>&1 | head -n 1 | awk -F'"' '{print $2}'`
JAVA_MAJOR=`echo ${JAVA_VERSION} | awk -F'.' '{print $1}'`
if [ ${JAVA_MAJOR} -ge 9 ]; then
	COMMON_PARAMS="${COMMON_PARAMS} --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-modules=ALL-SYSTEM"
fi

export GSA_JAVA_OPTIONS="${COMMON_PARAMS} -Xmx256m"
export GSM_JAVA_OPTIONS="${COMMON_PARAMS} -Xmx256m"
export LUS_JAVA_OPTIONS="${COMMON_PARAMS} -Xmx256m"
export GSM_JAVA_OPTIONS="${COMMON_PARAMS} -Xmx256m"

if [ "${GSC_HEAP_MAX_MB}" == "" ]; then
	GSC_HEAP_MAX_MB=2048
fi
GSC_JAVA_OPTIONS="${GSC_JAVA_OPTIONS} ${COMMON_PARAMS} -Xmx${GSC_HEAP_MAX_MB}m -Xms${GSC_HEAP_MAX_MB}m -XX:NewSize=200m -XX:MaxNewSize=200m"
GSC_JAVA_OPTIONS="${GSC_JAVA_OPTIONS} -DcombinedPuBackupCount=0 -Dspace-config.mirror-service.cluster.backups-per-partition=0"
export GSC_JAVA_OPTIONS


# XAP Variables
export XAP_NIC_ADDRESS=${HOSTNAME}
export XAP_LOOKUP_LOCATORS=${HOSTNAME}:${LOC_PORT}
export XAP_MANAGER_SERVERS="${HOSTNAME};lus=${LOC_PORT}"
export XAP_OPTIONS_EXT="-Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=${LOC_PORT}"


# Determine Project Path
PROJECT_RELATIVE=`dirname $0`/..
which realpath > /dev/null 2>&1
if [ $? != 0 ]; then
	which readlink > /dev/null 2>&1
	if [ $? != 0 ]; then
		if [[ "${PROJECT_RELATIVE}" != /* ]]; then
			PROJECT_PATH=`pwd`"/"${PROJECT_RELATIVE}
		else
			PROJECT_PATH=${PROJECT_RELATIVE}
		fi
	else
		PROJECT_PATH=`readlink -f "${PROJECT_RELATIVE}"`
	fi
else
	PROJECT_PATH=`realpath "${PROJECT_RELATIVE}"`
fi
export PROJECT_PATH


# Initialize Variables Based on Zone
init_zone() {
	ZONE=$1

	export ZONES=${USER},${ZONE}
	export LOOKUP_GROUPS=${USER}_${ZONE}

	export LUS_JAVA_OPTIONS="${LUS_JAVA_OPTIONS} -Dcom.gs.zones=${ZONES}"
	export GSM_JAVA_OPTIONS="${GSM_JAVA_OPTIONS} -Dcom.gs.zones=${ZONES}"
	export GSA_JAVA_OPTIONS="${GSA_JAVA_OPTIONS} -Dcom.gs.zones=${ZONES} -DPROC_DESC=GigaSpaces_${LOOKUP_GROUPS}"
	export GSC_JAVA_OPTIONS="${GSC_JAVA_OPTIONS} -Dcom.gs.zones=${ZONES} -DmyGroup=${LOOKUP_GROUPS}"

	export XAP_LUS_OPTIONS="$LUS_JAVA_OPTIONS"
	export XAP_GSM_OPTIONS="$GSM_JAVA_OPTIONS"
	export XAP_GSA_OPTIONS="$GSA_JAVA_OPTIONS"
	export XAP_GSC_OPTIONS="$GSC_JAVA_OPTIONS"
	export XAP_LOOKUP_GROUPS="${LOOKUP_GROUPS}"
}


# Start Grid
start_grid() {
	init_zone $1
	shift

	echo "Starting grid..."
	mkdir -p "${LOG_FOLDER}"
	${GS_HOME}/bin/gs.sh host run-agent $* 2>&1 | tee "${LOG_FOLDER}/grid_${ZONE}.log"
}


# Deploy PU
deploy_pu() {
	init_zone $1
	shift

	ARTIFACT=$1
	PU_COUNT=$2

	ARTIFACT_JAR=`find "${PROJECT_PATH}" -name "${ARTIFACT}*.jar" | grep -v "sources.jar"`
	if [ "$ARTIFACT_JAR" == "" ]; then
		echo "Cannot find $ARTIFACT JAR. Please build Maven project."
		echo ""
		exit 1
	fi
  
	if [ "${PU_COUNT}" == "" ]; then
		PU_COUNT=1
	fi

  	echo "Deploying ${ARTIFACT} PU..."
	mkdir -p "${LOG_FOLDER}"
	${GS_HOME}/bin/gs.sh --timeout=3600 pu deploy --max-instances-per-vm=1 --zones=${ZONES} --backups=0 --partitions=${PU_COUNT} "${LOOKUP_GROUPS}-${ARTIFACT}" "${ARTIFACT_JAR}" 2>&1 | tee "${LOG_FOLDER}/deploy_${ZONE}.log"  
}
