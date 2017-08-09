package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.NumeroLinhasExtractor;
import utils.MyFileVisitor;

public class AuditorNumeroLinhas {
	public static void main(String[] args) {
		System.out.println("AuditorNumeroLinhas");
		try {
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			NumeroLinhasExtractor extractor = new NumeroLinhasExtractor();
			MyFileVisitor fileVisitor = new MyFileVisitor(pathRef, glob);
			
			extractor.getResults(fileVisitor.getPaths());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Path generateReferenceFilePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				System.getProperty("user.dir"))
				.append(File.separator)
				.append("src")
				.append(File.separator)
				.append("numero-linhas");
		return Paths.get(sb.toString());
	}
	
}
