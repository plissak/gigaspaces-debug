package gs.debug.hnpe.common.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import gs.debug.core.common.util.StringUtil;
import gs.debug.hnpe.common.domain.Part;
import gs.debug.hnpe.common.domain.Widget;

public class WidgetUtil {

	public static Widget randomWidget() {
		Widget widget = new Widget();

		widget.setName(StringUtil.randomString(12));
		widget.setCreatedDate(new LocalDate());
		widget.setUpdatedTime(new LocalDateTime());

		Set<String> codes = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			codes.add(StringUtil.randomString(16));
		}
		widget.setCodes(codes);

		List<Long> identifiers = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			identifiers.add(StringUtil.RANDOM.nextLong());
		}
		widget.setOrderedIdentifiers(identifiers);

		Set<Part> parts = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			Part part = new Part();
			part.setName(StringUtil.randomString(12));
			part.setComment(StringUtil.randomString(64));
			parts.add(part);
		}
		widget.setParts(parts);

		return widget;
	}
}
