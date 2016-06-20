package orm.common.util;

import java.io.FileNotFoundException;
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

public class Producer {

	public static List<String> scanner = new ArrayList<>();

	public static Model model = new Model();

	public static void showClassesHierarchy() {
		// Main.scanInputFile();
		// Main.PrepareModel();
		System.out.println(model.getName());
		System.out.println("-----------------------------");
		for (Map.Entry<String, Class> entry : model.classes.entrySet()) {
			String key = entry.getKey();
			Class value = entry.getValue();

			if (!value.isActive()) {
				continue;
			}

			System.out.print(key);
			if (value.isChildClass()) {
				System.out.print(" < " + value.getInheritFrom());
			}

			System.out.println("\n--------");
			for (int i = 0; i < value.attributes.size(); i++) {
				Attribute b = value.attributes.get(i);
				System.out.println(b.getName() + " :: " + b.getType() + " ---> " + b.getReferenceTable());
			}
			System.out.println("------------------");

		}
	}

	public static void showAssociationHierarchy() {
		// Main.scanInputFile();
		// Main.PrepareModel();

		for (int i = 0; i < model.associations.size(); i++) {
			Association ass = model.associations.get(i);

			System.out.print(ass.getName());
			System.out.println("\n--------");
			System.out.println(ass.getClass1Name() + " :: " + ass.getClass1AssoType());
			System.out.println(ass.getClass2Name() + " :: " + ass.getClass2AssoType());
			System.out.println("------------------");
		}

	}

}
