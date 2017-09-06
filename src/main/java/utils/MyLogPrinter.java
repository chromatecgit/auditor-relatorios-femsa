package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import config.ProjectConfiguration;
import enums.IndentationEnum;

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
	
	public static void printCollection(Collection<? extends Object> objs, String fileName) {
		PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);

		for (Object obj : objs) {
			pw.write(obj.toString());
		}
		pw.close();
		System.out.println("PRINTED " + fileName + "!");
	}
	
	public static void printCollectionWithMessage(String message, List<? extends Object> objs, String fileName) {
		PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);
		pw.write(message + "\n");
		for (Object obj : objs) {
			pw.write(obj.toString());
		}
		pw.close();
		System.out.println("PRINTED " + fileName + "!");
	}
	
//	public static void printCollectionWithIndentation(List<? extends Indentable> indentables, String fileName) {
//		PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);
//		StringBuilder sb = new StringBuilder();
//		for (Indentable identable : indentables) {
//			sb.append(identable.toString());
//			sb.append("\n");
//			pw.write(sb.toString());
//		}
//		pw.close();
//		System.out.println("PRINTED " + fileName + "!");
//	}
	
	public synchronized static void addToBuiltMessage(String message) {
		Optional<String> m = Optional.ofNullable(message);
		MyLogPrinter.builtMessage.append(m.orElse(""));
		addLineBreak();
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
		if (builtMessage != null && builtMessage.length() > 0) {
			PrintWriter pw = MyLogPrinter.getPrintWriter(fileName);
			pw.write(builtMessage.toString() + "\n");
			pw.close();
			builtMessage = new StringBuilder();
			System.out.println("PRINTED " + fileName + "!");
		}
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
	
	public synchronized static void addToBuiltMessageWithLevel(IndentationEnum indentation, String message) {
		Optional<String> m = Optional.ofNullable(message);
		
		MyLogPrinter.builtMessage.append(indentation.getIndentationEntity());
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
