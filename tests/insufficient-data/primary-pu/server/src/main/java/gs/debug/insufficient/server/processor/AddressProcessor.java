package gs.debug.insufficient.server.processor;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import gs.debug.insufficient.common.service.AddressReadAccess;
import gs.debug.insufficient.common.service.AddressWriteAccess;

@SuppressWarnings("deprecation")
public class AddressProcessor implements InitializingBean {

	private GigaSpace space;
	private AddressReadAccess readAccess;
	private AddressWriteAccess writeAccess;

	private Logger logger = Logger.getLogger(getClass());

	@Required
	public void setSpace(GigaSpace space) {
		this.space = space;
	}

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


		//TODO create random address objects only if the partition is even (or only one partition) AND no data is present
		//TODO log activity

	}

}
