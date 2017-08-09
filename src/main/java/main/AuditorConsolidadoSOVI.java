package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.ConsolidadoSOVIExtractor;
import utils.MyFileVisitor;

public class AuditorConsolidadoSOVI {
	
	public static void main(String[] args) {
		System.out.println("AuditorConsolidadoSOVI");
		try {
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			Path pathTarget = generateTargetFilesPath();
			ConsolidadoSOVIExtractor extractor = new ConsolidadoSOVIExtractor();
			MyFileVisitor fv1 = new MyFileVisitor(pathRef, glob);
			MyFileVisitor fv2 = new MyFileVisitor(pathTarget, glob);
			
			extractor.getResults(fv1.getPaths(), fv2.getPaths());
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
				.append("consolidado-sovi")
				.append(File.separator)
				.append("ref");
		return Paths.get(sb.toString());
	}
	
	private static Path generateTargetFilesPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				System.getProperty("user.dir"))
				.append(File.separator)
				.append("src")
				.append(File.separator)
				.append("consolidado-sovi")
				.append(File.separator)
				.append("target");
		return Paths.get(sb.toString());
	}

}
