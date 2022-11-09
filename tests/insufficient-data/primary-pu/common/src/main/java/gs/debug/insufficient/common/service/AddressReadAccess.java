package gs.debug.insufficient.common.service;

import org.openspaces.core.GigaSpace;

import gs.debug.insufficient.common.domain.Address;

public class AddressReadAccess {
	private GigaSpace space;

	public Address[] getAddresses() {
		return space.readMultiple(new Address());
	}

	public Address[] getAddresses(String name) {
		Address template = new Address();
		template.setName(name);
		return space.readMultiple(template);
	}

	public Address getAddress(Long id) {
		return id == null ? null : space.readById(Address.class, id);
	}

	public GigaSpace getSpace() {
		return space;
	}

	public void setSpace(GigaSpace space) {
		this.space = space;
	}

}
