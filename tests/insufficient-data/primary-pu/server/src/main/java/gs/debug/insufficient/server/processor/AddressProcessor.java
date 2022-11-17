package gs.debug.insufficient.server.processor;

import org.apache.log4j.Logger;
import org.openspaces.core.cluster.ClusterInfo;
import org.openspaces.core.cluster.ClusterInfoContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import gs.debug.core.common.util.SpaceUtil;
import gs.debug.core.common.util.StringUtil;
import gs.debug.insufficient.common.domain.Address;
import gs.debug.insufficient.common.service.AddressReadAccess;
import gs.debug.insufficient.common.service.AddressWriteAccess;

public class AddressProcessor implements InitializingBean {
	private static final int RANDOM_ADDRESS_COUNT = 5;

	private static boolean IS_READ_ADDRESSES = false;

    @ClusterInfoContext
    protected ClusterInfo clusterInfo;

	private AddressReadAccess readAccess;
	private AddressWriteAccess writeAccess;

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	public void setReadAccess(AddressReadAccess readAccess) {
		this.readAccess = readAccess;
	}

	@Autowired
	public void setWriteAccess(AddressWriteAccess writeAccess) {
		this.writeAccess = writeAccess;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (IS_READ_ADDRESSES) {
			if (SpaceUtil.isEvenContainer(clusterInfo)) {
				if (getAddressCount() == 0) {
					logger.info("Creating random addresses in this partition");
					createAddresses();
				}
				else {
					logger.info("Addresses were created via SQL for this partition");
				}
			}
			else {
				logger.info("Not auto-generating addresses in this partition");
			}

			logger.info("Counted " + getAddressCount() + " addresses in this partition");
		}
	}

	private void createAddresses() {
		Long routingKey = SpaceUtil.getRoutingKey(clusterInfo);
		for (int i = 0; i < RANDOM_ADDRESS_COUNT; i++) {
			writeAccess.write(createAddress(routingKey));
		}
	}

	private Address createAddress(Long routingKey) {
		Address address = new Address();
		address.setRoutingKey(routingKey);
		address.setCity(StringUtil.randomString(12));
		address.setName(StringUtil.randomString(12));
		address.setState(StringUtil.randomLetterString(2).toUpperCase());
		address.setStreet(StringUtil.randomString(12));
		address.setZipCode(StringUtil.randomNumberString(5));
		return address;
	}

	private int getAddressCount() {
		Address[] addresses = readAccess.getAddresses();
		return addresses == null ? 0 : addresses.length;
	}

}
