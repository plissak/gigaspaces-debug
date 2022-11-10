package gs.debug.core.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringUtil {
	public static final Random RANDOM = new Random();

	private static final char[] RANDOM_CHARS;
	private static final char[] RANDOM_NUMBER_CHARS;
	private static final char[] RANDOM_LETTER_CHARS;

	static {
		List<Character> numberList = new ArrayList<>();
		char ch = '0';
		while (ch <= '9') {
			numberList.add(ch);
			ch = (char)(ch + 1);
		}

		List<Character> letterList = new ArrayList<>();
		ch = 'a';
		while (ch <= 'z') {
			letterList.add(ch);
			letterList.add(Character.toUpperCase(ch));
			ch = (char)(ch + 1);
		}

		RANDOM_NUMBER_CHARS = new char[numberList.size()];
		for (int i = 0; i < numberList.size(); i++) {
			RANDOM_NUMBER_CHARS[i] = numberList.get(i);
		}

		RANDOM_LETTER_CHARS = new char[letterList.size()];
		for (int i = 0; i < letterList.size(); i++) {
			RANDOM_LETTER_CHARS[i] = letterList.get(i);
		}

		RANDOM_CHARS = new char[numberList.size() + letterList.size()];
		int index = 0;
		for (int i = 0; i < numberList.size(); i++) {
			RANDOM_CHARS[index++] = numberList.get(i);
		}
		for (int i = 0; i < letterList.size(); i++) {
			RANDOM_CHARS[index++] = letterList.get(i);
		}
	}

	public static String randomString(int length) {
		return randomString(RANDOM_CHARS, length);
	}

	public static String randomNumberString(int length) {
		return randomString(RANDOM_NUMBER_CHARS, length);
	}

	public static String randomLetterString(int length) {
		return randomString(RANDOM_LETTER_CHARS, length);
	}

	private synchronized static String randomString(char[] possible, int length) {
		if (length < 1) {
			return "";
		}
		char[] buffer = new char[length];
		for (int idx = 0; idx < buffer.length; ++idx) {
			buffer[idx] = possible[RANDOM.nextInt(possible.length)];
		}
		return new String(buffer);
	}

}
