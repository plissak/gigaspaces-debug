package gs.debug.core.common.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.gigaspaces.security.Authority;
import com.gigaspaces.security.AuthorityFactory;
import com.gigaspaces.security.directory.UserDetails;

public class DebugUserDetails implements UserDetails {
	private static final long serialVersionUID = -7876876090817073585L;

	private String username = System.getProperty("user.name");
	private String password = "password";
	private Authority[] authorities = new Authority[0];

	@Override
	public Authority[] getAuthorities() {
		return authorities;
	}

	public void setAuthorityNames(List<Object> list) {
		Logger.getLogger(getClass()).info("Setting authorities: " + list);

		this.authorities = new Authority[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			this.authorities[i] = AuthorityFactory.create(list.get(i).toString());
		}
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
