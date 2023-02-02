package gs.debug.insufficient.client.util;

import java.util.Iterator;

import org.apache.log4j.Logger;

import gs.debug.core.common.util.LogUtil;
import gs.debug.core.common.util.SpaceUtil;
import gs.debug.insufficient.common.domain.Address;

public class PrintAddressesMain {

	public static void main(String[] args) {
		try {
			LogUtil.configure();

			PrintAddressesMain main = new PrintAddressesMain();
			main.readAll();
			main.iterateAll();
		}
		catch (Throwable t) {
			Logger.getLogger(PrintAddressesMain.class).error(t.getMessage(), t);
			System.exit(1);
		}
		System.exit(0);
	}

	private final Logger logger = Logger.getLogger(getClass());

	public void readAll() {
		logger.info("Begin read all");
		for (Address address : InsufficientDataClientConnection.getReadAccess().getAddresses()) {
			process(address, false);
		}
		logger.info("End read all");
	}

	public void iterateAll() {
		logger.info("Begin iterate all");
		for (Iterator<Address> iterator = SpaceUtil.iterator(InsufficientDataClientConnection.getSpace(), new Address()); iterator
				.hasNext();) {
			process(iterator.next(), false);
		}
		logger.info("End iterate all");
	}

	private void process(Address address, boolean isDelete) {
		logger.info("Address " + (isDelete ? "DELETE " : "") + "\"" + address.getName() + "\"; street=" + address.getStreet() + "; city="
				+ address.getCity() + "; state=" + address.getState());
	}

}
