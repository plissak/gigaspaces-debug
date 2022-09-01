package gs.debug.core.client.util;

import java.io.Closeable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientConnection implements Closeable {
	private final String config;
	private ApplicationContext context;

	public ClientConnection(String config) {
		super();
		this.config = config;
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	public synchronized void close() {
		if (context instanceof Lifecycle) {
			((Lifecycle)context).stop();
		}
		if (context instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext)context).close();
		}
		context = null;
	}

	public synchronized ApplicationContext getContext() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(config);
		}
		return context;
	}

	@Override
	public String toString() {
		return config;
	}
}
