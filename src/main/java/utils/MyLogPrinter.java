package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import config.ProjectConfiguration;
import enums.MessageHierarchyEnum;

public class MyLogPrinter {
	
	private static StringBuilder builtMessage = new StringBuilder();
	
	private synchronized static PrintWriter getPrintWriter(String fileName) {
		try {
			File file = new File(ProjectConfiguration.logFolder.concat(fileName + ".txt"));
			return new PrintWriter((new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo: " + fileName + " não foi encontrado");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void printCollection(List<? extends Object> objs, String fileName) {
		PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);

		for (Object obj : objs) {
			pw.write(obj.toString());
		}
		pw.close();
		System.out.println("PRINTED " + fileName + "!");
	}
	
	public static void printCollection(String message, List<? extends Object> objs, String fileName) {
		PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);
		pw.write(message + "\n");
		for (Object obj : objs) {
			pw.write(obj.toString());
		}
		pw.close();
		System.out.println("PRINTED " + fileName + "!");
	}
	
	public synchronized static void addToBuiltMessage(String message) {
		Optional<String> m = Optional.ofNullable(message);
		MyLogPrinter.builtMessage.append(m.orElse(""));
	}
	
	public synchronized static void addBuiltMessageTitle(String title, String subject) {
		Optional<String> s = Optional.ofNullable(subject);
		MyLogPrinter.builtMessage.append(title).append(":").append(s.orElse(""));
		addLineBreak();
	}
	
	private synchronized static void addLineBreak() {
		MyLogPrinter.builtMessage.append("\n");
	}
	
	public static void printBuiltMessage(String fileName) {
		PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);
		pw.write(builtMessage.toString() + "\n");
		builtMessage = new StringBuilder();
		pw.close();
		System.out.println("PRINTED " + fileName + "!");
	}
	
	public static void printBuiltMessageWithCollectionOf(List<? extends Object> objs, String fileName) {
		PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);
		pw.write(builtMessage.toString() + "\n");
		builtMessage = new StringBuilder();
		for (Object obj : objs) {
			pw.write(obj.toString());
		}
		pw.close();
		System.out.println("PRINTED " + fileName + "!");
	}
	
	public synchronized static void addToBuiltMessageWithLevel(MessageHierarchyEnum level, String message) {
		Optional<String> m = Optional.ofNullable(message);
		for (short i = 1; i <= level.getLevel(); i++) {
			MyLogPrinter.builtMessage.append("\t");
		}
		
		MyLogPrinter.builtMessage.append(m.orElse(""));
		addLineBreak();
	}
	
	public static void printObject(final Object obj, final String fileName) {
		PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);
		pw.write(obj.toString());
		pw.close();
		System.out.println("PRINTED " + fileName + "!");
	}

}
