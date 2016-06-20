package orm.common.util;

public class Constant {

	/*
	 * 
	 * Map the entire class hierarchy to a single table (TPH) Map each concrete
	 * class to its own table (TPC) Map each class to its own table (TPT)
	 * 
	 */
	public final static String TPH = "1";
	public final static String TPC = "2";
	public final static String TPT = "3";
}
