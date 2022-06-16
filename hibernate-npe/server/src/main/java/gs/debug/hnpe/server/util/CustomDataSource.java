package gs.debug.hnpe.server.util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.InitializingBean;

public class CustomDataSource extends BasicDataSource implements InitializingBean {
	private Collection<String> initSqls;
	private boolean isShowSql;

	public void setInitSqls(Collection<String> initSqls) {
		this.initSqls = initSqls;
	}

	public Collection<String> getInitSqls() {
		return initSqls;
	}

	public void setShowSql(boolean isShowSql) {
		this.isShowSql = isShowSql;
	}

	public boolean isShowSql() {
		return isShowSql;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (initSqls != null) {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();

			for (String sql : initSqls) {
				if (sql != null && ! sql.isEmpty()) {
					if (isShowSql) {
						System.out.println("SQL: " + sql);
					}
					statement.execute(sql);
				}
			}
		}
	}

}
