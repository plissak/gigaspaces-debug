package gs.debug.hnpe.server.security;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.gigaspaces.security.AccessDeniedException;
import com.gigaspaces.security.Authentication;
import com.gigaspaces.security.AuthenticationException;
import com.gigaspaces.security.SecurityException;
import com.gigaspaces.security.SecurityManager;
import com.gigaspaces.security.directory.DirectoryManager;
import com.gigaspaces.security.directory.UserDetails;

public class DebugSecurityManager implements SecurityManager {
	private final Logger logger = Logger.getLogger(getClass());
	private Properties properties;

	@Override
	public void init(Properties properties) throws SecurityException {
		this.properties = properties;
	}

	@Override
	public Authentication authenticate(UserDetails userDetails) throws AuthenticationException {
		logger.debug("Authenticate[" + userDetails.getUsername() + "]: " + properties);
		return new Authentication(userDetails);
	}

	@Override
	public DirectoryManager createDirectoryManager(UserDetails userDetails) throws AuthenticationException, AccessDeniedException {
		return null;
	}

	@Override
	public void close() {
		this.properties = null;
	}

}
