package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.ValoresIguaisExtractor;
import utils.MyFileVisitor;

public class AuditorValoresIguais {
	public static void main(String[] args) {
		System.out.println("AuditorConsolidadoPreco");
		try {
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			ValoresIguaisExtractor extractor = new ValoresIguaisExtractor();
			MyFileVisitor fv1 = new MyFileVisitor(pathRef, glob);
			
			extractor.getResults(fv1.getPaths());
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
				.append("valores-iguais");

		return Paths.get(sb.toString());
	}
}
