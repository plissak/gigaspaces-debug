package gs.debug.core.common.util;

import com.gigaspaces.security.Authority;
import com.gigaspaces.security.directory.UserDetails;

public class BasicUserDetails implements UserDetails {
	private static final long serialVersionUID = -7876876090817073585L;

	private String username = System.getProperty("user.name");
	private String password = "password";

	@Override
	public Authority[] getAuthorities() {
		return new Authority[0];
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
