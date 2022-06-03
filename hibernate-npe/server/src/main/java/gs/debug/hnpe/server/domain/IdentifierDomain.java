package gs.debug.hnpe.server.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.gigaspaces.annotation.pojo.SpaceId;

@Entity(name = "ID_DOMAIN")
@Access(AccessType.PROPERTY)
public class IdentifierDomain implements Serializable {
	private static final long serialVersionUID = 1451205562463633992L;

	private String domain;
	private Long lastId;

	@Id
	@SpaceId(autoGenerate = false)
	@Column(name = "DOMAIN")
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "LAST_ID")
	public Long getLastId() {
		return lastId;
	}
	public void setLastId(Long lastId) {
		this.lastId = lastId;
	}
}
