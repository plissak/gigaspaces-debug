package gs.debug.insufficient.common.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.annotation.pojo.SpaceRouting;

@Entity(name = "ADDRESS")
@Access(AccessType.PROPERTY)
@SpaceClass(persist=true)
public class Address implements Serializable {
	private static final long serialVersionUID = -7222084435514086312L;

	private Long id;
	private String name;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private Long routingKey;

	@Id
	@SpaceId(autoGenerate = false)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@SpaceIndex
	@Column(name = "ADDRESS_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "STREET")
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "STATE")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "ZIP_CODE")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

    @SpaceRouting
	@Column(name="ROUTING_KEY", nullable=false, columnDefinition = "bigint default 0")
    public Long getRoutingKey() {
    	return routingKey;
    }

    public void setRoutingKey(Long routingKey) {
    	this.routingKey = routingKey;
    }

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}