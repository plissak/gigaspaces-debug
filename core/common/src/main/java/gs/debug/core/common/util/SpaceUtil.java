package gs.debug.core.common.util;

import com.gigaspaces.client.ReadModifiers;
import com.gigaspaces.client.iterator.SpaceIteratorConfiguration;
import com.gigaspaces.client.iterator.SpaceIteratorType;

public class SpaceUtil {
	public static final int DEFAULT_BATCH_SIZE = 1000;
	public static final int DEFAULT_BATCH_TIMEOUT_MS = 200;

	public static SpaceIteratorConfiguration getIteratorConfiguration() {
		SpaceIteratorConfiguration config = new SpaceIteratorConfiguration();
		config.setBatchSize(DEFAULT_BATCH_SIZE);
		config.setIteratorType(SpaceIteratorType.PREFETCH_UIDS); // w/o this the ISpaceFilter process method won't have a security context
		config.setReadModifiers(ReadModifiers.READ_COMMITTED); // this is how GSIterator behaved by default
		return config;
	}

}
