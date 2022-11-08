package gs.debug.hnpe.common.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jadira.usertype.dateandtime.joda.PersistentLocalDate;
import org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;

@Entity(name = "WIDGET")
@Access(AccessType.PROPERTY)
@TypeDefs({
		@TypeDef(name = "joda-localdate", typeClass = PersistentLocalDate.class),
		@TypeDef(name = "joda-localdatetime", typeClass = PersistentLocalDateTime.class)
})
public class Widget implements Serializable {
	private static final long serialVersionUID = 3073785123613802499L;

	private Long id;
	private String name;
	private Set<String> codes;
	private Set<Part> parts;
	private List<Long> orderedIdentifiers;
	private LocalDate createdDate;
	private LocalDateTime updatedTime;

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
	@Column(name = "WIDGET_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Fetch(value = FetchMode.SELECT)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "WIDGET_CODE", joinColumns = @JoinColumn(name = "WIDGET_ID"))
	public Set<String> getCodes() {
		return codes;
	}

	public void setCodes(Set<String> codes) {
		this.codes = codes;
	}

	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "WIDGET_ID")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Part.class)
	public Set<Part> getParts() {
		return parts;
	}

	public void setParts(Set<Part> parts) {
		this.parts = parts;
	}

	@Fetch(value=FetchMode.SELECT)
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="WIDGET_ID_TABLE", joinColumns=@JoinColumn(name="WIDGET_ID"))
	@OrderColumn(name="IDENTIFIER_INDEX")
	@Column(name="IDENTIFIER")
	public List<Long> getOrderedIdentifiers() {
		return orderedIdentifiers;
	}
	public void setOrderedIdentifiers(List<Long> orderedIdentifiers) {
		this.orderedIdentifiers = orderedIdentifiers;
	}

	@Column(name = "CREATED_DATE")
	@Type(type = "joda-localdate")
	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "UPDATED_TIME")
	@Type(type = "joda-localdatetime")
	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
