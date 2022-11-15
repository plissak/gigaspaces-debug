package gs.debug.core.server.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.gigaspaces.security.AccessDeniedException;
import com.gigaspaces.security.Authentication;
import com.gigaspaces.security.AuthenticationException;
import com.gigaspaces.security.Authority;
import com.gigaspaces.security.SecurityException;
import com.gigaspaces.security.SecurityManager;
import com.gigaspaces.security.authorities.MonitorAuthority;
import com.gigaspaces.security.authorities.MonitorAuthority.MonitorPrivilege;
import com.gigaspaces.security.authorities.SpaceAuthority;
import com.gigaspaces.security.authorities.SpaceAuthority.SpacePrivilege;
import com.gigaspaces.security.directory.DirectoryManager;
import com.gigaspaces.security.directory.User;
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

		List<Authority> list = new ArrayList<>();
		list.add(new SpaceAuthority(SpacePrivilege.READ));
		list.add(new SpaceAuthority(SpacePrivilege.EXECUTE));

		list.add(new SpaceAuthority(SpacePrivilege.WRITE));
		list.add(new SpaceAuthority(SpacePrivilege.TAKE));
		list.add(new SpaceAuthority(SpacePrivilege.ALTER));

		list.add(new MonitorAuthority(MonitorPrivilege.MONITOR_PU));
		list.add(new MonitorAuthority(MonitorPrivilege.MONITOR_JVM));

		Authority[] authorities = userDetails.getAuthorities();
		if (authorities != null) {
			for (Authority authority : authorities) {
				if (authority != null && ! list.contains(authority)) {
					list.add(authority);
				}
			}
		}

		return new Authentication(new User(userDetails.getUsername(), userDetails.getPassword(), list.toArray(new Authority[list.size()])));
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
