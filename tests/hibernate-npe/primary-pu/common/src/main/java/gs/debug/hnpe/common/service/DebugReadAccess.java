package gs.debug.hnpe.common.service;

import org.openspaces.core.GigaSpace;

import gs.debug.hnpe.common.domain.Widget;

public class DebugReadAccess {
	private GigaSpace space;

	public Widget[] getWidgets() {
		return space.readMultiple(new Widget());
	}

	public Widget[] getWidgets(String name) {
		Widget template = new Widget();
		template.setName(name);
		return space.readMultiple(template);
	}

	public Widget getWidget(Long id) {
		return id == null ? null : space.readById(Widget.class, id);
	}

	public GigaSpace getSpace() {
		return space;
	}

	public void setSpace(GigaSpace space) {
		this.space = space;
	}

}
