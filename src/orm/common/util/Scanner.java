package orm.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import orm.common.entity.Association;
import orm.common.entity.Attribute;
import orm.common.entity.Class;
import orm.common.entity.Model;

public class Scanner {

	public static List<String> scanner = new ArrayList<>();
	public static List<String> errors = new ArrayList<>();

	public static Model model = new Model();

	public static void PrepareModel() {
		String tempString = "";
		Class currentClass = null;
		Attribute attribute = null;
		Association association = null;
		boolean isClassDeclared = false, isAttributeDeclared = false;
		boolean isAssoDeclared = false, isFirstAsso = false;
		String[] splitters;
		for (int i = 0; i < scanner.size(); i++) {
			tempString = scanner.get(i).trim();

			if (tempString.isEmpty())
				continue;

			if (tempString.startsWith("--"))
				continue;

			if (tempString.contains("model")) {
				splitters = tempString.split(" ");
				model.setName(splitters[1]);
				continue;
			}

			if (tempString.contains("end")) {
				if (isAssoDeclared) {
					model.associations.add(association);
					isAssoDeclared = isFirstAsso = false;
				} else {
					model.classes.put(currentClass.getName(), currentClass);
					isClassDeclared = isAttributeDeclared = false;
				}
				continue;
			}

			if (tempString.contains("attributes")) {
				isAttributeDeclared = true;
				continue;
			}

			if (isAssoDeclared) {
				String relationType = tempString.substring(tempString.indexOf('['));
				String _className = tempString.substring(0, tempString.indexOf('['));
				if (!isFirstAsso) {
					isFirstAsso = true;
					association.setClass1Name(_className);
					association.setClass1AssoType(relationType);
				} else {
					isFirstAsso = false;
					association.setClass2Name(_className);
					association.setClass2AssoType(relationType);
				}
			}

			if (isAttributeDeclared && isClassDeclared) {
				splitters = tempString.split(":");
				attribute = new Attribute();
				attribute.setName(splitters[0]);
				attribute.setType(splitters[1]);
				currentClass.attributes.add(attribute);
				continue;
			}

			if (tempString.contains("class") && !tempString.contains("<") && !tempString.contains("classes")) {
				isClassDeclared = true;
				splitters = tempString.split(" ");
				currentClass = new Class();
				currentClass.setName(splitters[1]);
				continue;
			}

			if (tempString.contains("class") && tempString.contains("<")) {
				isClassDeclared = true;
				splitters = tempString.split(" ");
				currentClass = new Class();
				currentClass.setName(splitters[1]);
				currentClass.setChildClass(true);
				currentClass.setInheritFrom(splitters[3]);
				model.classes.get(splitters[3]).setSuperClass(true);
				continue;
			}

			if (tempString.contains("association") && !tempString.contains("associations")) {
				isAssoDeclared = true;
				splitters = tempString.split(" ");
				association = new Association();
				association.setName(splitters[1]);
				continue;
			}
		}
	}

	public static void syntaxAnalyzer() {
		String tempString = "";
		boolean isClassDeclared = false, isAttributeDeclared = false;
		String[] splitters;
		for (int i = 0; i < scanner.size(); i++) {
			tempString = scanner.get(i);
			if (tempString.isEmpty())
				continue;

			if (tempString.startsWith("--"))
				continue;

			if (tempString.startsWith("-") && !tempString.startsWith("--")) {
				errors.add("Error:: undef - in line number " + (i + 1));
				continue;
			}

			if (tempString.contains("-") && !tempString.contains("--")) {
				errors.add("Error:: undef - in line number " + (i + 1));
				continue;
			}

			if (tempString.contains("model")) {
				splitters = tempString.split(" ");
				if (splitters.length == 1) {
					errors.add("Error:: model name must be found on the same line in line number " + (i + 1));
					continue;
				}
			}

			if (isClassDeclared && tempString.contains("class")) {
				errors.add("Error:: un ended class declared prev before line number " + (i + 1));
				continue;
			}

			if (tempString.contains("end")) {
				isClassDeclared = isAttributeDeclared = false;
				continue;
			}

			if (tempString.contains("class") && !tempString.contains("<") && !tempString.contains("classes")) {
				splitters = tempString.split(" ");
				isClassDeclared = true;
				if (splitters.length != 2) {
					errors.add("Error:: missing class name in line number " + (i + 1));
					continue;
				}
			}

			if (tempString.contains("class") && tempString.contains("<")) {
				isClassDeclared = true;
				splitters = tempString.split(" ");
				if (splitters.length != 4) {
					errors.add("Error:: error inheritance syntax in line number " + (i + 1));
					continue;
				}
			}

			if (tempString.contains("attributes")) {
				isAttributeDeclared = true;
				continue;
			}

			if (isAttributeDeclared && isClassDeclared) {
				splitters = tempString.split(":");
				if (splitters.length != 2) {
					errors.add("Error:: missing attributes name or type in line number " + (i + 1));
					continue;
				} else {
					if (!splitters[1].equals("Integer") && !splitters[1].equals("String")
							&& splitters[1].equals("Boolean") && !splitters[1].equals("Double")
							&& !splitters[1].equals("Float") && !splitters[1].equals("Date")) {
						errors.add("Error:: unknown attributes type in line number " + (i + 1));
						continue;
					}
				}
			}

			if (tempString.contains("association") && !tempString.contains("associations")) {
				splitters = tempString.split(" ");
				if (splitters.length != 3) {
					errors.add(
							"Error:: association must be decalred  [association name between] line number " + (i + 1));
					continue;
				}
			}
		}
	}

	public static void scanInputFile() {
		File file = new File("input.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;

			while ((line = br.readLine()) != null) {
				line = line.trim();
				scanner.add(line);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

	}

	public static void run(String inhertinace_type) {

		for (int i = 0; i < model.associations.size(); i++) {
			Association ass = model.associations.get(i);

			if (ass.getClass1AssoType().equals("[1]") && ass.getClass2AssoType().equals("[1]")) {
				Class dummy1Class = model.classes.get(ass.getClass1Name());
				Class dummy2Class = model.classes.get(ass.getClass2Name());

				String att1Name = ass.getClass1Name().toLowerCase() + "_id";
				String att2Name = ass.getClass2Name().toLowerCase() + "_id";

				Attribute attr1 = new Attribute(att1Name, "Integer", "0", false, true, ass.getClass2Name());
				dummy2Class.attributes.add(attr1);

				Attribute attr2 = new Attribute(att2Name, "Integer", "0", false, true, ass.getClass1Name());
				dummy1Class.attributes.add(attr2);

				model.classes.put(ass.getClass1Name(), dummy1Class);
				model.classes.put(ass.getClass2Name(), dummy2Class);
			} else if ((ass.getClass1AssoType().equals("[1]") || ass.getClass1AssoType().equals("[1..*]"))
					&& ass.getClass2AssoType().equals("[*]")) {
				Class dummyClass = model.classes.get(ass.getClass2Name());
				String attName = ass.getClass1Name().toLowerCase() + "_id";
				Attribute attr = new Attribute(attName, "Integer", "0", false, true, ass.getClass1Name());
				dummyClass.attributes.add(attr);

				model.classes.put(ass.getClass2Name(), dummyClass);
			} else if (ass.getClass1AssoType().equals("[*]")
					&& (ass.getClass2AssoType().equals("[1]") || ass.getClass2AssoType().equals("[1..*]"))) {
				//System.out.println(ass.getClass1Name());
				Class dummyClass = model.classes.get(ass.getClass1Name());
				String attName = ass.getClass2Name().toLowerCase() + "_id";
				Attribute attr = new Attribute(attName, "Integer", "0", false, true, ass.getClass2Name());
				dummyClass.attributes.add(attr);

				model.classes.put(ass.getClass1Name(), dummyClass);
			} else if (ass.getClass1AssoType().equals("[*]") && ass.getClass2AssoType().equals("[*]")) {
				Class dummyClass = new Class();
				dummyClass.setName(ass.getClass1Name() + "_" + ass.getClass2Name());

				String att1Name = ass.getClass1Name().toLowerCase() + "_id";
				String att2Name = ass.getClass2Name().toLowerCase() + "_id";

				Attribute attr1 = new Attribute(att1Name, "Integer", "0", false, true, ass.getClass1Name());
				Attribute attr2 = new Attribute(att2Name, "Integer", "0", false, true, ass.getClass2Name());

				dummyClass.attributes.add(attr1);
				dummyClass.attributes.add(attr2);
				model.classes.put(ass.getClass1Name() + "_" + ass.getClass2Name(), dummyClass);
			}
		}

		handleInheritanceCases(inhertinace_type);
	}

	public static boolean isTypeAdded(Class class_) {
		for (int i = 0; i < class_.attributes.size(); i++)
			if (class_.attributes.get(i).getName().equals("type"))
				return true;
		return false;
	}

	public static void updateReferencesTables(String old, String newClass) {
		for (Map.Entry<String, Class> entry : model.classes.entrySet()) {
			Class class_ = entry.getValue();

			if (!class_.isActive())
				continue;

			for (int i = 0; i < class_.attributes.size(); i++)
				if (class_.attributes.get(i).isForeignkey() && class_.attributes.get(i).getReferenceTable().equals(old))
					class_.attributes.get(i).setReferenceTable(newClass);

		}
	}

	public static void handleInheritanceCases(String case_) {

		for (Map.Entry<String, Class> entry : model.classes.entrySet()) {
			String key = entry.getKey();
			Class class_ = entry.getValue();

			// 1 class hierarchy
			if (class_.isChildClass() && case_.equals(Constant.TPH)) {
				Class superClass_ = model.classes.get(class_.getInheritFrom());
				Attribute attr = new Attribute("type", "String", "null", false, false, null);

				// super class
				if (!isTypeAdded(superClass_))
					superClass_.attributes.add(attr);
				superClass_.attributes.addAll(class_.attributes);

				// old class (child)
				class_.setActive(false);
				updateReferencesTables(key, class_.getInheritFrom());
			} else if (class_.isChildClass() && case_.equals(Constant.TPC)) {
				// super class
				Class superClass_ = model.classes.get(class_.getInheritFrom());
				superClass_.setActive(false);

				// class (child)
				Attribute attr = new Attribute("ID", "Integer", "0", true, false, class_.getInheritFrom());
				class_.attributes.add(attr);
				class_.attributes.addAll(superClass_.attributes);
			} else if (class_.isChildClass() && case_.equals(Constant.TPT)) {
				Attribute attr = new Attribute(class_.getInheritFrom() + "ID", "Integer", "0", true, true,
						class_.getInheritFrom());
				class_.setKey(class_.getInheritFrom() + "ID");
				class_.attributes.add(attr);
			}

		}
	}

	public static void produceQueries() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("query.sql", "UTF-8");
			writer.println("-- ----------------------------------------------------------");
			writer.println("-- AUTHOR: IBRAHIM ALI MOHAMED EBEIDO");
			writer.println("-- ENTITY DESIGNER DDL SCRIPT FOR SQL SERVER 2005, 2008, 2012");
			writer.println("-- DATE CREATED: " + new Date());
			writer.println("-- ----------------------------------------------------------\n\n");

			writer.println("-- ----------------------------------------------------------");
			writer.println("-- Creating Database");
			writer.println("-- ----------------------------------------------------------");

			writer.println("CREATE DATABASE IF NOT EXISTS " + model.getName() + ";\n");

			writer.println("-- ----------------------------------------------------------");
			writer.println("-- using database");
			writer.println("-- ----------------------------------------------------------");
			writer.println("USE " + model.getName() + ";\n");

			writer.println("-- ----------------------------------------------------------");
			writer.println("-- Creating tables");
			writer.println("-- ----------------------------------------------------------\n");

			for (Map.Entry<String, Class> entry : model.classes.entrySet()) {
				String key = entry.getKey();
				Class value = entry.getValue();

				if (!value.isActive())
					continue;

				writer.println("-- Creating table " + key);
				writer.println("CREATE TABLE IF NOT EXISTS " + key + " (");

				if (!value.isChildClass())
					writer.println("   ID int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,");

				for (int i = 0; i < value.attributes.size(); i++) {
					Attribute b = value.attributes.get(i);
					String colType = b.getType().trim();

					if (colType.equals("Integer"))
						colType = "int DEFAULT 0";
					else if (colType.equals("String"))
						colType = "VARCHAR(100) NULL";
					else if (colType.equals("Boolean"))
						colType = "int DEFAULT 0";
					else if (colType.equals("Float"))
						colType = "int Float 0";
					else if (colType.equals("DOUBLE"))
						colType = "int DOUBLE 0";
					else if (colType.equals("Date"))
						colType = "TIMESTAMP";

					if (!b.isForeignkey() && !b.isPrimerykey()) {
						writer.print("   " + b.getName().trim() + " " + colType);
					} else if (b.isPrimerykey()) {
						writer.print("   " + b.getName().trim() + " int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY");
					} else {
						writer.print("   " + b.getName().trim() + " " + colType);
					}

					if (i < value.attributes.size() - 1)
						writer.println(",");
					else
						writer.println();

					if (i == value.attributes.size() - 1) {
						writer.println(");\n");
					}
				}

			}

			writer.println("-- ----------------------------------------------------------");
			writer.println("-- Creating all FOREIGN KEY constraints ");
			writer.println("-- ----------------------------------------------------------\n");

			for (Map.Entry<String, Class> entry : model.classes.entrySet()) {
				String key = entry.getKey();
				Class value = entry.getValue();

				if (!value.isActive())
					continue;

				for (int i = 0; i < value.attributes.size(); i++) {
					Attribute b = value.attributes.get(i);

					if (b.isForeignkey()) {
						writer.println("-- Creating foreign key on [" + b.getName() + "] in table " + key);
						writer.println("ALTER TABLE " + key);
						writer.println("ADD CONSTRAINT FK_" + (b.getReferenceTable() + key).toLowerCase());
						writer.println("	FOREIGN KEY (" + b.getName() + ")");
						if (model.classes.get(b.getReferenceTable()).getKey() == null)
							writer.println("	REFERENCES " + b.getReferenceTable() + " (ID)");
						else
							writer.println("	REFERENCES " + b.getReferenceTable() + " ("
									+ model.classes.get(b.getReferenceTable()).getKey() + ")");
						writer.println("	ON DELETE NO ACTION ON UPDATE NO ACTION;\n\n");
					}

				}

			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
}
