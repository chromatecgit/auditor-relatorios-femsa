package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class MyLogPrinter {
	public static void printCollection(List<? extends Object> objs, String fileName) {
		try {
			File file = new File(System.getProperty("user.dir").concat("\\" + fileName + ".txt"));
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));

			for (Object obj : objs) {
				pw.write(obj.toString());
			}
			pw.close();
			System.out.println("PRINTED " + fileName + "!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
