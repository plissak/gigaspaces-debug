package gs.debug.insufficient.client.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import gs.debug.insufficient.common.service.DebugSecondaryReadAccess;

public class DebugSecondaryClientConnection {
	private static final String CONTEXT_PATH = "InsufficientData-Secondary-Client-Spring.xml";

	private static DebugSecondaryClientConnection connection;

	public synchronized static DebugSecondaryClientConnection getConnection() {
		if (connection == null) {
			connection = new DebugSecondaryClientConnection();
		}
		return connection;
	}

	private ApplicationContext context;

	public DebugSecondaryReadAccess getReadAccess() {
		return getContext(CONTEXT_PATH).getBean(DebugSecondaryReadAccess.class);
	}


	private synchronized ApplicationContext getContext(String path) {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(path);
			if (context instanceof Lifecycle) {
				((Lifecycle)context).start();
			}
			if (context instanceof ConfigurableApplicationContext) {
				((ConfigurableApplicationContext)context).registerShutdownHook();
			}
		}
		return context;
	}

}
