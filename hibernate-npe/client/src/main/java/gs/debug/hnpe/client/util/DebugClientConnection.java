package gs.debug.hnpe.client.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import gs.debug.hnpe.common.service.DebugReadAccess;
import gs.debug.hnpe.common.service.DebugWriteAccess;

public class DebugClientConnection {
	private static final String CONTEXT_PATH = "HNPE-Client-Connection.xml";

	private static DebugClientConnection connection;

	public synchronized static DebugClientConnection getConnection() {
		if (connection == null) {
			connection = new DebugClientConnection();
		}
		return connection;
	}

	private ApplicationContext context;

	public DebugReadAccess getReadAccess() {
		return getContext(CONTEXT_PATH).getBean(DebugReadAccess.class);
	}

	public DebugWriteAccess getWriteAccess() {
		return getContext(CONTEXT_PATH).getBean(DebugWriteAccess.class);
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
