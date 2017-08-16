package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import config.ProjectConfiguration;

public class MyLogPrinter {
	
	private static StringBuilder builtMessage = new StringBuilder();
	
	public static void printCollection(List<? extends Object> objs, String fileName) {
		try {
			File file = new File(ProjectConfiguration.logFolder.concat(fileName + ".txt"));
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
	
	public static void printCollection(String message, List<? extends Object> objs, String fileName) {
		try {
			File file = new File(ProjectConfiguration.logFolder.concat(fileName + ".txt"));
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			pw.write(message + "\n");
			for (Object obj : objs) {
				pw.write(obj.toString());
			}
			pw.close();
			System.out.println("PRINTED " + fileName + "!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void addToBuiltMessage(String str) {
		MyLogPrinter.builtMessage.append(str);
	}
	
	public static void printBuiltMessage(String fileName) {
		try {
			File file = new File(ProjectConfiguration.logFolder.concat(fileName + ".txt"));
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			pw.write(builtMessage.toString() + "\n");
			builtMessage = new StringBuilder();
			pw.close();
			System.out.println("PRINTED " + fileName + "!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void printBuiltMessageWithCollectionOf(List<? extends Object> objs, String fileName) {
		try {
			File file = new File(ProjectConfiguration.logFolder.concat(fileName + ".txt"));
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			pw.write(builtMessage.toString() + "\n");
			builtMessage = new StringBuilder();
			for (Object obj : objs) {
				pw.write(obj.toString());
			}
			pw.close();
			System.out.println("PRINTED " + fileName + "!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void printObject(final Object obj, final String fileName) {
		try {
			File file = new File(ProjectConfiguration.logFolder.concat(fileName + ".txt"));
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			pw.write(obj.toString());
			pw.close();
			System.out.println("PRINTED " + fileName + "!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
