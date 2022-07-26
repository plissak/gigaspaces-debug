package gs.debug.hnpe.client.util;

import org.apache.log4j.Logger;

import gs.debug.core.common.util.LogUtil;
import gs.debug.hnpe.common.domain.Widget;
import gs.debug.hnpe.common.util.WidgetUtil;

public class InitializeMain {

	public static void main(String[] args) {
		try {
			LogUtil.configure();

			DebugClientConnection connection = DebugClientConnection.getConnection();

			for (int i = 0; i < 100; i++) {
				Widget widget = WidgetUtil.randomWidget();
				Widget saved = connection.getWriteAccess().write(widget);

				if (saved == null || saved.getId() == null) {
					throw new Exception("Missing identifier");
				}
			}
		}
		catch (Throwable t) {
			Logger.getLogger(InitializeMain.class).error(t.getMessage(), t);
			System.exit(1);
		}
		System.exit(0);
	}

}
