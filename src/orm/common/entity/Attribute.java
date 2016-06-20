package orm.common.entity;

public class Attribute {

	private String name;
	private String type;
	private String defaultValue;
	private boolean isPrimerykey;
	private boolean isForeignkey;
	private String referenceTable;

	public Attribute() {
	}

	public Attribute(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public Attribute(String name, String type, String defaultValue, boolean isPrimerykey, boolean isForeignkey,
			String referenceTable) {
		super();
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.isPrimerykey = isPrimerykey;
		this.isForeignkey = isForeignkey;
		this.referenceTable = referenceTable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isPrimerykey() {
		return isPrimerykey;
	}

	public void setPrimerykey(boolean isPrimerykey) {
		this.isPrimerykey = isPrimerykey;
	}

	public boolean isForeignkey() {
		return isForeignkey;
	}

	public void setForeignkey(boolean isForeignkey) {
		this.isForeignkey = isForeignkey;
	}

	public String getReferenceTable() {
		return referenceTable;
	}

	public void setReferenceTable(String referenceTable) {
		this.referenceTable = referenceTable;
	}

}
