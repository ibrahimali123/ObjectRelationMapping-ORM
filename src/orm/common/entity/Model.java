package orm.common.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

	private String name;
	public Map<String, Class> classes;
	public List<Association> associations;

	public Model() {
		classes = new HashMap<String, Class>();
		associations = new ArrayList<Association>();
	}

	public Model(String name) {
		super();
		this.name = name;
		classes = new HashMap<String, Class>();
		associations = new ArrayList<Association>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Class> getClasses() {
		return classes;
	}

	public void setClasses(Map<String, Class> classes) {
		this.classes = classes;
	}

	public List<Association> getAssociations() {
		return associations;
	}

	public void setAssociations(List<Association> associations) {
		this.associations = associations;
	}

}
