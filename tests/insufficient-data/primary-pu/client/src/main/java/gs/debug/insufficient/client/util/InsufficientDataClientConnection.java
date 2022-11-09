package gs.debug.insufficient.client.util;

import org.openspaces.core.GigaSpace;

import gs.debug.core.client.util.ClientConnection;
import gs.debug.insufficient.common.service.AddressReadAccess;
import gs.debug.insufficient.common.service.AddressWriteAccess;

public class InsufficientDataClientConnection {
	private static ClientConnection connection = new ClientConnection("InsufficientData-Client-Connection.xml");

	public static AddressReadAccess getReadAccess() {
		return connection.getContext().getBean(AddressReadAccess.class);
	}

	public static AddressWriteAccess getWriteAccess() {
		return connection.getContext().getBean(AddressWriteAccess.class);
	}

	public static GigaSpace getSpace() {
		return connection.getContext().getBean("insufficientDataGigaSpace", GigaSpace.class);
	}
}
