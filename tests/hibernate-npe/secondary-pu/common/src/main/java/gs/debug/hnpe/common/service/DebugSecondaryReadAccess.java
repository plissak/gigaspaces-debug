package gs.debug.hnpe.common.service;

import org.openspaces.core.GigaSpace;

public class DebugSecondaryReadAccess {
	private GigaSpace space;

	public GigaSpace getSpace() {
		return space;
	}

	public void setSpace(GigaSpace space) {
		this.space = space;
	}

}
