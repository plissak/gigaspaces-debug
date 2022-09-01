package gs.debug.hnpe.client.util;

import org.openspaces.core.GigaSpace;

import gs.debug.core.client.util.ClientConnection;
import gs.debug.hnpe.common.service.DebugReadAccess;
import gs.debug.hnpe.common.service.DebugWriteAccess;

public class DebugClientConnection {
	private static ClientConnection connection = new ClientConnection("HNPE-Client-Connection.xml");

	public static DebugReadAccess getReadAccess() {
		return connection.getContext().getBean(DebugReadAccess.class);
	}

	public static DebugWriteAccess getWriteAccess() {
		return connection.getContext().getBean(DebugWriteAccess.class);
	}

	public static GigaSpace getSpace() {
		return connection.getContext().getBean("hnpeGigaSpace", GigaSpace.class);
	}
}
