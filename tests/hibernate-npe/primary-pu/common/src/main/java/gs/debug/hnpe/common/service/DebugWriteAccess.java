package gs.debug.hnpe.common.service;

import gs.debug.hnpe.common.domain.Widget;

public interface DebugWriteAccess {

	public Widget write(Widget widget);

	public Widget deleteWidget(Long id);
}
