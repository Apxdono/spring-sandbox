package org.apx.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apx.model.handlers.DBObjectLazyHandler;
import org.apx.repo.handler.LazyInitHandler;
import org.apx.utils.Generator;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Created by oleg on 3/13/14.
 */
@MappedSuperclass
@LazyInitHandler(handlerClass = DBObjectLazyHandler.class)
public abstract class DBObject implements Serializable {
	private static final long serialVersionUID = 2425658928079456714L;

	String id;
	String displayName;
	String internalName;
	boolean active;
	long version;

	public DBObject() {
		id = Generator.UUID();
		displayName = "";
		internalName = "";
		active = true;
		version = 0L;
	}

	public DBObject(DBObject other) {
		id = other.id;
		displayName = other.displayName;
		internalName = other.internalName;
		active = other.active;
		version = other.version;
	}

	@Id
	@Column(name = "id",length = 36, nullable = false,unique = true)
	public String getId() {
		return id;
	}

	@Column(name = "display_name",length = 256, nullable = false)
	public String getDisplayName() {
		return displayName;
	}

	@Column(name = "internal_name",length = 256, nullable = false)
	public String getInternalName() {
		return internalName;
	}

	@Column(name = "active", nullable = false)
	public boolean isActive() {
		return active;
	}

	@Version
	@Column(name = "version", nullable = false)
	public long getVersion() {
		return version;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DBObject dbObject = (DBObject) o;

		if (!id.equals(dbObject.id)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(getClass().getCanonicalName()).toHashCode();
	}
}
