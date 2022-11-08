package gs.debug.hnpe.server.service;

import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import gs.debug.hnpe.common.domain.Part;
import gs.debug.hnpe.common.domain.Widget;
import gs.debug.hnpe.common.service.DebugWriteAccess;

@SuppressWarnings("deprecation")
public class DebugWriteAccessImpl implements DebugWriteAccess {
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
	public Widget write(Widget widget) {
		if (widget != null) {
			if (widget.getId() == null) {
				widget.setId(idGenerator.getNextIdentifier(Widget.class.getName()));
			}

			if (widget.getParts() != null) {
				for (Part part : widget.getParts()) {
					if (part.getId() == null) {
						part.setId(idGenerator.getNextIdentifier(Part.class.getName()));
					}
				}
			}

			space.write(widget);
			return space.readById(Widget.class, widget.getId());
		}
		return null;
	}

	@Override
	public Widget deleteWidget(Long id) {
		if (id != null) {
			return space.takeById(Widget.class, id);
		}
		return null;
	}

}
