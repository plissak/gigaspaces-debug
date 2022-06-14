package gs.debug.hnpe.server.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.IOUtils;

/**
 * Parses file resource into SQL lines for org.apache.commons.dbcp2.BasicDataSource
 *
 * @author plissak
 */
public class SQLParser {

	public static Collection<String> parse(String fileName) throws IOException {
		if (fileName == null || fileName.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		Collection<String> sql = new ArrayList();
		for (String line : IOUtils.readLines(SQLParser.class.getResource(fileName).openStream())) {
			String trimmed = line.trim();
			if (! trimmed.isEmpty() && ! trimmed.startsWith("--")) {
				sql.add(trimmed);
			}
		}
		return sql;
	}

}
