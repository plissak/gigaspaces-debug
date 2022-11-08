package gs.debug.hnpe.common.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import gs.debug.hnpe.common.domain.Part;
import gs.debug.hnpe.common.domain.Widget;

public class WidgetUtil {
	private static final char[] RANDOM_CHARS;
	private static final Random RANDOM = new Random();

	static {
		List<Character> charList = new ArrayList<>();
		char ch = '0';
		while (ch <= '9') {
			charList.add(ch);
			ch = (char)(ch + 1);
		}

		ch = 'a';
		while (ch <= 'z') {
			charList.add(ch);
			charList.add(Character.toUpperCase(ch));
			ch = (char)(ch + 1);
		}

		RANDOM_CHARS = new char[charList.size()];
		for (int i = 0; i < charList.size(); i++) {
			RANDOM_CHARS[i] = charList.get(i);
		}
	}

	public synchronized static String randomString(int length) {
		if (length < 1) {
			return "";
		}
		char[] buffer = new char[length];
		for (int idx = 0; idx < buffer.length; ++idx) {
			buffer[idx] = RANDOM_CHARS[RANDOM.nextInt(RANDOM_CHARS.length)];
		}
		return new String(buffer);
	}

	public static Widget randomWidget() {
		Widget widget = new Widget();

		widget.setName(randomString(12));
		widget.setCreatedDate(new LocalDate());
		widget.setUpdatedTime(new LocalDateTime());

		Set<String> codes = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			codes.add(randomString(16));
		}
		widget.setCodes(codes);

		List<Long> identifiers = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			identifiers.add(RANDOM.nextLong());
		}
		widget.setOrderedIdentifiers(identifiers);


		Set<Part> parts = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			Part part = new Part();
			part.setName(randomString(12));
			part.setComment(randomString(64));
			parts.add(part);
		}
		widget.setParts(parts);

		return widget;
	}
}
