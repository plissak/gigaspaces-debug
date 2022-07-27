package gs.debug.core.server.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Parses file resource into SQL lines for org.apache.commons.dbcp2.BasicDataSource
 *
 * @author plissak
 */
public class SQLParser {

	public static Collection<String> parse(String fileName) throws IOException {
		if (fileName == null || fileName.isEmpty()) {
			Logger.getLogger(SQLParser.class).info("Parsed 0 lines");
			return Collections.EMPTY_LIST;
		}

		Logger.getLogger(SQLParser.class).info("Parsing file: " + fileName);
		Collection<String> sql = new ArrayList();
		List<String> lines = IOUtils.readLines(SQLParser.class.getResource(fileName).openStream());

		if (lines != null) {
			for (String line : lines) {
				String trimmed = line.trim();
				if (! trimmed.isEmpty() && ! trimmed.startsWith("--")) {
					sql.add(trimmed);
				}
			}
		}

		Logger.getLogger(SQLParser.class).info("Parsed " + sql.size() + " lines (" + fileName + ")");
		return sql;
	}

}
