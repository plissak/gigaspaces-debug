package gs.debug.hnpe.client.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import gs.debug.hnpe.common.domain.Part;
import gs.debug.hnpe.common.domain.Widget;
import gs.debug.hnpe.common.util.LogUtil;

public class InitializeMain {
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

	public static void main(String[] args) {
		try {
			LogUtil.configure();

			DebugClientConnection connection = DebugClientConnection.getConnection();

			for (int i = 0; i < 100; i++) {
				Widget widget = createWidget();
				Widget saved = connection.getWriteAccess().write(widget);

				if (saved == null || saved.getId() == null) {
					throw new Exception("Missing identifier");
				}
			}
		}
		catch (Throwable t) {
			Logger.getLogger(InitializeMain.class).error(t.getMessage(), t);
			System.exit(1);
		}
		System.exit(0);
	}


	private static Widget createWidget() {
		Widget widget = new Widget();

		widget.setName(randomString(12));
		widget.setCreatedDate(new LocalDate());
		widget.setUpdatedTime(new LocalDateTime());

		Set<String> codes = new HashSet<>();
		for (int i = 0; i < 10; i++) {
			codes.add(randomString(16));
		}
		widget.setCodes(codes);

		List<Long> identifiers = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			identifiers.add(RANDOM.nextLong());
		}
		widget.setOrderedIdentifiers(identifiers);


		Set<Part> parts = new HashSet<>();
		for (int i = 0; i < 5; i++) {
			Part part = new Part();
			part.setName(randomString(12));
			part.setComment(randomString(64));
			parts.add(part);
		}
		widget.setParts(parts);

		return widget;
	}

}
