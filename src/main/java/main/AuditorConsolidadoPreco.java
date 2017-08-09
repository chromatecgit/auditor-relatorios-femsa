package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.ConsolidadoPrecoExtractor;
import utils.MyFileVisitor;

public class AuditorConsolidadoPreco {
	
	public static void main(String[] args) {
		System.out.println("AuditorConsolidadoPreco");
		try {
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			ConsolidadoPrecoExtractor extractor = new ConsolidadoPrecoExtractor();
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
				.append("consolidado-preco")
				.append(File.separator)
				.append("ref");
		return Paths.get(sb.toString());
	}

}
