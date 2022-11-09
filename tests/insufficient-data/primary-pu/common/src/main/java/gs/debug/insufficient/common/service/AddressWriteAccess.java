package gs.debug.insufficient.common.service;

import org.openspaces.remoting.Routing;

import gs.debug.insufficient.common.domain.Address;

public interface AddressWriteAccess {

	public Address write(@Routing("getRoutingKey") Address address);
}
