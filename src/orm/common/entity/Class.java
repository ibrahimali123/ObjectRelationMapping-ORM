package orm.common.entity;

import java.util.ArrayList;
import java.util.List;

public class Class {

	private String name;
	private String key;
	public List<Attribute> attributes;
	private boolean isChildClass;
	private boolean isSuperClass;
	private String inheritFrom;
	private boolean active = true;

	public Class() {
		attributes = new ArrayList<Attribute>();
		isChildClass = false;
	}

	public Class(String name, boolean isChildClass, String inheritFrom) {
		super();
		this.name = name;
		this.isChildClass = isChildClass;
		this.inheritFrom = inheritFrom;

		isChildClass = isSuperClass = false;
		attributes = new ArrayList<Attribute>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public boolean isChildClass() {
		return isChildClass;
	}

	public void setChildClass(boolean isChildClass) {
		this.isChildClass = isChildClass;
	}

	public String getInheritFrom() {
		return inheritFrom;
	}

	public void setInheritFrom(String inheritFrom) {
		this.inheritFrom = inheritFrom;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSuperClass() {
		return isSuperClass;
	}

	public void setSuperClass(boolean isSuperClass) {
		this.isSuperClass = isSuperClass;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
