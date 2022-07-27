package gs.debug.core.server.security;

import org.openspaces.core.space.SecurityConfig;

import com.gigaspaces.security.directory.CredentialsProvider;
import com.gigaspaces.security.directory.UserDetails;

public class DebugSecurityConfig extends SecurityConfig {
	private CredentialsProvider credentialsProvider;

	@Override
	public CredentialsProvider getCredentialsProvider() {
		return credentialsProvider;
	}

	public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
	}

	@Override
	public String getUsername() {
		UserDetails details = getUserDetails();
		return details == null ? null : details.getUsername();
	}

	@Override
	public String getPassword() {
		UserDetails details = getUserDetails();
		return details == null ? null : details.getPassword();
	}

	@Override
	public boolean isFilled() {
		return credentialsProvider != null;
	}

	public UserDetails getUserDetails() {
		return credentialsProvider == null ? null : credentialsProvider.getUserDetails();
	}
}
