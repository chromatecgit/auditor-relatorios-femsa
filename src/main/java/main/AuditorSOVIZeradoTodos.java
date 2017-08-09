package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.SOVIZeradoTodosExtractor;
import utils.MyFileVisitor;

public class AuditorSOVIZeradoTodos {
	
	public static void main(String[] args) {
		System.out.println("AuditorSOVIZeradoTodos");
		try {
			String verificationType = "verificacao1";
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			Path pathTarget = generateTargetFilesPath();
			SOVIZeradoTodosExtractor extractor = new SOVIZeradoTodosExtractor();
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
				.append("sovi-zerado-todos")
				.append(File.separator)
				.append("target");
		return Paths.get(sb.toString());
	}
}
