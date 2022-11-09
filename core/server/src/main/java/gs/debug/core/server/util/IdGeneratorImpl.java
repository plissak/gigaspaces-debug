package gs.debug.core.server.util;

import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Required;

import gs.debug.core.server.domain.IdentifierDomain;

@SuppressWarnings("deprecation")
public class IdGeneratorImpl implements IdGenerator {
	private GigaSpace space;

	@Override
	public synchronized Long getNextIdentifier(String domain) {
		IdentifierDomain template = new IdentifierDomain();
		template.setDomain(domain);
		IdentifierDomain record = space.read(template);

		if (record == null) {
			record = new IdentifierDomain();
			record.setDomain(domain);
			record.setLastId(0L);
		}

		Long identifier = record.getLastId() + 1;
		record.setLastId(identifier);
		space.write(record);

		return identifier;
	}

	public GigaSpace getSpace() {
		return space;
	}

	@Required
	public void setSpace(GigaSpace space) {
		this.space = space;
	}

}
