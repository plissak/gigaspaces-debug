package gs.debug.hnpe.client.util;

import java.util.Iterator;

import org.apache.log4j.Logger;

import gs.debug.core.common.util.LogUtil;
import gs.debug.core.common.util.SpaceUtil;
import gs.debug.hnpe.common.domain.Widget;

public class PrintWidgetsMain {

	public static void main(String[] args) {
		try {
			LogUtil.configure();

			PrintWidgetsMain main = new PrintWidgetsMain();
			main.readAll();
			main.iterateAll();
		}
		catch (Throwable t) {
			Logger.getLogger(InitializeMain.class).error(t.getMessage(), t);
			System.exit(1);
		}
		System.exit(0);
	}

	private final Logger logger = Logger.getLogger(getClass());

	public void readAll() {
		logger.info("Begin read all");
		for (Widget widget : DebugClientConnection.getReadAccess().getWidgets()) {
			process(widget, false);
		}
		logger.info("End read all");
	}

	public void iterateAll() {
		logger.info("Begin iterate all");
		for (Iterator<Widget> iterator = SpaceUtil.iterator(DebugClientConnection.getSpace(), new Widget()); iterator.hasNext();) {
			process(iterator.next(), false);
		}
		logger.info("End iterate all");
	}

	private void process(Widget widget, boolean isDelete) {
		logger.info("Widget " + (isDelete ? "DELETE " : "") + "\"" + widget.getName() + "\"; parts=" + widget.getParts() + "; codes="
				+ widget.getCodes());
	}

}
