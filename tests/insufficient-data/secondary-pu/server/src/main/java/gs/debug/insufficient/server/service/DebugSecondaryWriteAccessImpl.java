package gs.debug.insufficient.server.service;

import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Required;

import gs.debug.insufficient.common.service.DebugSecondaryWriteAccess;

@SuppressWarnings("deprecation")
public class DebugSecondaryWriteAccessImpl implements DebugSecondaryWriteAccess {
	private GigaSpace space;

	public GigaSpace getSpace() {
		return space;
	}

	@Required
	public void setSpace(GigaSpace space) {
		this.space = space;
	}

}
