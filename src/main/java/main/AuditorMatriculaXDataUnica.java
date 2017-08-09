package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.MatriculaXDataUnicaExtractor;
import utils.MyFileVisitor;

public class AuditorMatriculaXDataUnica {
	
	public static void main(String[] args) {
		System.out.println("AuditorMatriculaXDataUnica");
		try {
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			MatriculaXDataUnicaExtractor extractor = new MatriculaXDataUnicaExtractor();
			MyFileVisitor fv1 = new MyFileVisitor(pathRef, glob);
			
			extractor.getResults(fv1.getPaths());
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
				.append("matricula-x-data-unica");
		return Paths.get(sb.toString());
	}

}
