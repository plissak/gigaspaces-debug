package gs.debug.hnpe.client.util;

import org.apache.log4j.Logger;

import gs.debug.hnpe.common.domain.Widget;
import gs.debug.hnpe.common.util.LogUtil;

public class InitializeMain {

	public static void main(String[] args) {
		try {
			LogUtil.configure();

			DebugClientConnection connection = DebugClientConnection.getConnection();

			Widget[] widgets = connection.getReadAccess().getWidgets();


		}
		catch (Throwable t) {
			Logger.getLogger(InitializeMain.class).error(t.getMessage(), t);
		}
	}

}
