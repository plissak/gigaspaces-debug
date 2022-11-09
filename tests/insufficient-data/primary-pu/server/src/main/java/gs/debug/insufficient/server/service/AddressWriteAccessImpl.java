package gs.debug.insufficient.server.service;

import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import gs.debug.core.server.util.IdGenerator;
import gs.debug.insufficient.common.domain.Address;
import gs.debug.insufficient.common.service.AddressWriteAccess;

@SuppressWarnings("deprecation")
public class AddressWriteAccessImpl implements AddressWriteAccess {
	private GigaSpace space;
	private IdGenerator idGenerator;

	public GigaSpace getSpace() {
		return space;
	}

	@Required
	public void setSpace(GigaSpace space) {
		this.space = space;
	}

	public IdGenerator getIdGenerator() {
		return idGenerator;
	}

	@Autowired
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Override
	public Address write(Address address) {
		if (address != null) {
			if (address.getId() == null) {
				address.setId(idGenerator.getNextIdentifier(Address.class.getName()));
			}

			space.write(address);
			return space.readById(Address.class, address.getId());
		}
		return null;
	}

}
