package gs.debug.hnpe.server.security;

import org.apache.log4j.Logger;

import com.gigaspaces.security.service.SecurityContext;
import com.j_spaces.core.IJSpace;
import com.j_spaces.core.SpaceContext;
import com.j_spaces.core.filters.ISpaceFilter;
import com.j_spaces.core.filters.entry.ISpaceFilterEntry;

public class DebugAccessFilter implements ISpaceFilter {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void init(IJSpace space, String filterId, String url, int priority) throws RuntimeException {
		// ignore
	}

	@Override
	public void process(SpaceContext context, ISpaceFilterEntry entry, int operationCode) throws RuntimeException {
		checkContext(context);
	}

	@Override
	public void process(SpaceContext context, ISpaceFilterEntry[] entries, int operationCode) throws RuntimeException {
		checkContext(context);
	}

	@Override
	public void close() throws RuntimeException {
		// ignore
	}

	private void checkContext(SpaceContext context) {
		SecurityContext security = context == null ? null : context.getSecurityContext();
		if (security == null) {
			logger.warn("Missing security context", new Exception("Missing security context"));
		}
	}

}
