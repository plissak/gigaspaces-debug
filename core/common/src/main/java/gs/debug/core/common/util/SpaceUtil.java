package gs.debug.core.common.util;

import java.util.Iterator;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.cluster.ClusterInfo;

public class SpaceUtil {
	public static final int DEFAULT_BATCH_SIZE = 1000;
	public static final int DEFAULT_BATCH_TIMEOUT_MS = 200;

//	public static SpaceIteratorConfiguration getIteratorConfiguration() {
//		SpaceIteratorConfiguration config = new SpaceIteratorConfiguration();
//		config.setBatchSize(DEFAULT_BATCH_SIZE);
//		config.setIteratorType(SpaceIteratorType.PREFETCH_UIDS); // w/o this the ISpaceFilter process method won't have a security context
//		config.setReadModifiers(ReadModifiers.READ_COMMITTED); // this is how GSIterator behaved by default
//		return config;
//	}

	public static <T> Iterator<T> iterator(GigaSpace space, T template) {
		return space.iterator(template);
	}

	/**
	 * Returns true if the given routing key is applicable for the given partition (identified by the given cluster information).
	 * Also returns true if there is no cluster or no routing key.
	 * <p>
	 * This method can be used for mapping non-partitioned space objects across partitioned PUs on a different space.
	 *
     * @param routingKey the data identifier where container routing is based on
	 * @param clusterInfo the container's cluster information
     * @return true if this container should include the corresponding data
	 */
    public static boolean accept(Long routingKey, ClusterInfo clusterInfo) {
        if (routingKey == null || clusterInfo == null) {
            return true;
        }
        return accept(routingKey, clusterInfo.getInstanceId(), clusterInfo.getNumberOfInstances());
    }

    /**
	 * Returns true if the given routing key is applicable for the given container.
	 * Also returns true if any of the given arguments are null.
	 * <p>
	 * This method can be used for mapping non-partitioned space objects across partitioned PUs on a different space.
	 *
     * @param routingKey the data identifier where container routing is based on
     * @param containerNumber the number associated with this container (starting at 1)
     * @param totalContainers the total number of containers we're distributing across
     * @return true if this container should include the corresponding data
     */
    public static boolean accept(Long routingKey, Integer containerNumber, Integer totalContainers) {
        if (routingKey == null || containerNumber == null || totalContainers == null) {
            return true;
        }
        return (routingKey % totalContainers) == (containerNumber.intValue() - 1);
    }

	public static boolean isEvenContainer(ClusterInfo clusterInfo) {
		Integer containerNumber = clusterInfo == null ? null : clusterInfo.getInstanceId();
		if (containerNumber == null) {
			return true;
		}
		return (containerNumber.intValue() - 1) % 2 == 0;
	}

	public static Long getRoutingKey(ClusterInfo clusterInfo) {
		return clusterInfo == null ? 0L : getRoutingKey(clusterInfo.getInstanceId(), clusterInfo.getNumberOfInstances());
	}

    public static Long getRoutingKey(Integer containerNumber, Integer totalContainers) {
        if (containerNumber == null || totalContainers == null) {
            return 0L;
        }
        int routingKey = (containerNumber.intValue() - 1) % totalContainers;
        return (long)routingKey;
    }

}
