package gs.debug.core.server.security;

import com.gigaspaces.security.directory.CredentialsProvider;
import com.gigaspaces.security.directory.UserDetails;

public class DebugCredentialsProvider extends CredentialsProvider {
	private UserDetails userDetails;

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	@Override
	public UserDetails getUserDetails() {
		return userDetails;
	}
}
