package gs.debug.core.server.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.springframework.beans.factory.annotation.Required;

import gs.debug.core.common.util.LogUtil;

public class LogInitializer {
	private String processingUnit;
	private String loggingLevel;
	private String transactionLogLevel;
	private String hibernateLogLevel;
	private Boolean toConsole;
	private String maxFileSize;
	private Integer maxBackUpIndex;
	private String puInstanceId;
	private String puBackupId;

	private Logger logger = Logger.getLogger(getClass());

	private static Boolean isInitialized = false;

	public void init() throws IOException {
		if (isInitialized) {
			return;
		}
		Appender appender;

		//format of all log messages.
		PatternLayout layout = new PatternLayout(LogUtil.LOG4J_LAYOUT);

		if (toConsole) {
			appender = new ConsoleAppender(layout);
		}
		else {
			String home = System.getProperty("user.home");

			String instance;
			if (puBackupId == null || puBackupId.equals("")) {
				instance = puInstanceId;
			}
			else {
				instance = puInstanceId + "_" + puBackupId;
			}

			String logName = home + "/logs/debug/" + processingUnit + "/" + processingUnit + "_" + instance + ".log";
			File file = new File(logName);
			boolean prevlogExists = false;
			if (file.isFile()) {
				prevlogExists = true;
			}

			appender = new RollingFileAppender(layout, logName);
			((RollingFileAppender)appender).setMaxFileSize(maxFileSize);
			((RollingFileAppender)appender).setMaxBackupIndex(maxBackUpIndex);

			if (prevlogExists) {
				((RollingFileAppender)appender).rollOver();
			}
		}

		BasicConfigurator.configure(appender);
		Logger.getRootLogger().setLevel(Level.toLevel(loggingLevel));
		logger.info("Setting Root Log Level to " + loggingLevel);

		if (transactionLogLevel != null && ! transactionLogLevel.isEmpty()) {
			logger.info("Setting Transactional Log Level to " + transactionLogLevel);
			Logger.getLogger("org.springframework.transaction.support").setLevel(Level.toLevel(transactionLogLevel));
			Logger.getLogger("org.openspaces.core.transaction.manager").setLevel(Level.toLevel(transactionLogLevel));
		}

		if (hibernateLogLevel != null && ! hibernateLogLevel.isEmpty()) {
			logger.info("Setting Hibernate Log Level to " + hibernateLogLevel);
			Logger.getLogger("log4j.logger.org.hibernate.SQL").setLevel(Level.toLevel(hibernateLogLevel));
		}

        //TODO always enable Hibernate debug for now

		Logger.getLogger("log4j.logger.org.hibernate.internal").setLevel(Level.TRACE);
        Logger.getLogger("org.hibernate.internal").setLevel(Level.TRACE);

        Logger.getLogger("log4j.logger.org.hibernate.collection.internal").setLevel(Level.TRACE);
        Logger.getLogger("org.hibernate.collection.internal").setLevel(Level.TRACE);

        Logger.getLogger("log4j.logger.org.hibernate.engine.internal").setLevel(Level.TRACE);
        Logger.getLogger("org.hibernate.engine.internal").setLevel(Level.TRACE);

        Logger.getLogger("log4j.logger.org.hibernate.engine.transaction").setLevel(Level.TRACE);
        Logger.getLogger("org.hibernate.engine.transaction").setLevel(Level.TRACE);

		String host = System.getProperty("java.rmi.server.hostname");
		logger.info("This PU instance running on: " + host);

		isInitialized = true;
	}

	@Required
	public void setProcessingUnit(String processingUnit) {
		this.processingUnit = processingUnit;
	}

	@Required
	public void setLoggingLevel(String loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	@Required
	public void setTransactionLogLevel(String transactionLogLevel) {
		this.transactionLogLevel = transactionLogLevel;
	}

	@Required
	public void setHibernateLogLevel(String hibernateLogLevel) {
		this.hibernateLogLevel = hibernateLogLevel;
	}

	@Required
	public void setToConsole(Boolean toConsole) {
		this.toConsole = toConsole;
	}

	@Required
	public void setMaxFileSize(String maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	@Required
	public void setMaxBackUpIndex(Integer maxBackUpIndex) {
		this.maxBackUpIndex = maxBackUpIndex;
	}

	@Required
	public void setPuInstanceId(String puInstanceId) {
		this.puInstanceId = puInstanceId;
	}

	@Required
	public void setPuBackupId(String puBackupId) {
		this.puBackupId = puBackupId;
	}
}
