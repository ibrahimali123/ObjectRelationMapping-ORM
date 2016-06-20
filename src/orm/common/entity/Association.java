package orm.common.entity;

public class Association {

	private String name;
	private String class1Name;
	private String class1AssoType;
	private String class2Name;
	private String class2AssoType;

	public Association() {
	}

	public Association(String name, String class1Name, String class1AssoType, String class2Name,
			String class2AssoType) {
		super();
		this.name = name;
		this.class1Name = class1Name;
		this.class1AssoType = class1AssoType;
		this.class2Name = class2Name;
		this.class2AssoType = class2AssoType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClass1Name() {
		return class1Name;
	}

	public void setClass1Name(String class1Name) {
		this.class1Name = class1Name;
	}

	public String getClass1AssoType() {
		return class1AssoType;
	}

	public void setClass1AssoType(String class1AssoType) {
		this.class1AssoType = class1AssoType;
	}

	public String getClass2Name() {
		return class2Name;
	}

	public void setClass2Name(String class2Name) {
		this.class2Name = class2Name;
	}

	public String getClass2AssoType() {
		return class2AssoType;
	}

	public void setClass2AssoType(String class2AssoType) {
		this.class2AssoType = class2AssoType;
	}

}
