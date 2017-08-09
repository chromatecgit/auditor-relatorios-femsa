package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.ExcluirAndFotoByIDExtractor;
import utils.MyFileVisitor;

public class AuditorExcluirAndFotoByID {
	
	public static void main(String[] args) {
		System.out.println("AuditorExcluirAndFotoByID");
		try {
			String verificationType = "verificacao1";
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			Path pathTarget = generateTargetFilesPath();
			ExcluirAndFotoByIDExtractor extractor = new ExcluirAndFotoByIDExtractor();
			MyFileVisitor fileVisitor = new MyFileVisitor(pathTarget, glob);
			
			extractor.getResults(pathRef.toFile(), fileVisitor.getPaths(), verificationType);
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
				.append("excluir-and-foto-by-id")
				.append(File.separator)
				.append("ref")
				.append(File.separator)
				.append("IDs.xlsx");
		return Paths.get(sb.toString());
	}
	
	private static Path generateTargetFilesPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				System.getProperty("user.dir"))
				.append(File.separator)
				.append("src")
				.append(File.separator)
				.append("excluir-and-foto-by-id")
				.append(File.separator)
				.append("target");
		return Paths.get(sb.toString());
	}
}
