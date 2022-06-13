#!/bin/bash

###############################################################################
# Common script for module-specific scripts to include
###############################################################################

# Basic Options
export LOC_PORT=4174
export LOG_GRID=/tmp/grid.log
export LOG_DEPLOY=/tmp/deploy.log
export ENABLE_DEBUG=Y

export XAP_NIC_ADDRESS=${HOSTNAME}
export XAP_LOOKUP_LOCATORS=${HOSTNAME}:${LOC_PORT}
export XAP_MANAGER_SERVERS="${HOSTNAME};lus=${LOC_PORT}"
export XAP_LOOKUP_GROUPS=${USER}
export XAP_ZONE=generic

if [ "${PU_COUNT}" == "" ]; then
	PU_COUNT=1
fi
export PU_COUNT


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


# Java Options
COMMON_PARAMS="-server -Xss1m -Dcom.gs.multicast.enabled=false -Dcom.gs.security.disable-commit-abort-authentication=true -Dcom.gs.zones=${XAP_ZONE}"
JAVA_VERSION=`${JAVA_HOME}/bin/java -version 2>&1 | head -n 1 | awk -F'"' '{print $2}'`
JAVA_MAJOR=`echo ${JAVA_VERSION} | awk -F'.' '{print $1}'`
if [ ${JAVA_MAJOR} -ge 9 ]; then
	COMMON_PARAMS="${COMMON_PARAMS} --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-modules=ALL-SYSTEM"
fi

export XAP_OPTIONS_EXT="-Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=${LOC_PORT}"
export XAP_GSA_OPTIONS="${COMMON_PARAMS} -DPROC_DESC=GigaSpaces_${XAP_LOOKUP_GROUPS} -Xmx256m"
export XAP_GSM_OPTIONS="${COMMON_PARAMS} -Xmx256m"
export XAP_LUS_OPTIONS="${COMMON_PARAMS} -Xmx256m"
export XAP_MANAGER_OPTIONS="${COMMON_PARAMS} -Xmx256m"
export XAP_WEBUI_OPTIONS="${COMMON_PARAMS} -Xmx256m"


# GSC Options
GSC_OPTIONS="${GSC_OPTIONS} ${COMMON_PARAMS} -DmyGroup=${XAP_LOOKUP_GROUPS} -XX:+HeapDumpOnOutOfMemoryError"
if [ "${GSC_HEAP_MAX_MB}" == "" ]; then
        GSC_HEAP_MAX_MB=2048
fi
GSC_OPTIONS="${GSC_OPTIONS} -Xmx${GSC_HEAP_MAX_MB}m -Xms${GSC_HEAP_MAX_MB}m -XX:NewSize=200m -XX:MaxNewSize=200m"
GSC_OPTIONS="${GSC_OPTIONS} -DcombinedPuBackupCount=0 -Dspace-config.mirror-service.cluster.backups-per-partition=0"
export XAP_GSC_OPTIONS=${GSC_OPTIONS}


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


# Start Grid
start_grid() {
	echo "Starting grid..."
	${GS_HOME}/bin/gs.sh host run-agent --manager --gsc=${PU_COUNT} $* 2>&1 | tee "${LOG_GRID}"
}


# Deploy PU
deploy_pu() {
	ARTIFACT=$1
	ARTIFACT_JAR=`find "${PROJECT_PATH}" -name "${ARTIFACT}*.jar" | grep -v "sources.jar"`

	if [ "$ARTIFACT_JAR" == "" ]; then
		echo "Cannot find $ARTIFACT JAR. Please build Maven project."
		echo ""
		exit 1
	fi
  
  	echo "Deploying ${ARTIFACT} PU..."
	${GS_HOME}/bin/gs.sh --timeout=3600 pu deploy --max-instances-per-vm=1 --zones=${XAP_ZONE} --backups=0 --partitions=${PU_COUNT} "${XAP_LOOKUP_GROUPS}-${ARTIFACT}" "${ARTIFACT_JAR}" 2>&1 | tee "${LOG_DEPLOY}"  
}
