package gs.debug.hnpe.common.util;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogUtil {
	public static final String LOG4J_LAYOUT = "%d %-5p [%t] [%c{1}] %m%n";

	public static void configure() {
		PatternLayout layout = new PatternLayout(LogUtil.LOG4J_LAYOUT);
		Appender appender = new ConsoleAppender(layout);
		BasicConfigurator.configure(appender);
		String level = System.getProperty("mainLoggingLevel");
		if (level == null) {
			Logger.getRootLogger().setLevel(Level.INFO);
		}
		else {
			Logger.getRootLogger().setLevel(Level.toLevel(level));
		}
	}
}
