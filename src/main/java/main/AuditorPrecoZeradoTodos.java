package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.PrecoZeradoTodosExtractor;
import utils.MyFileVisitor;

public class AuditorPrecoZeradoTodos {
	
	public static void main(String[] args) {
		System.out.println("AuditorPrecoZeradoTodos");
		try {
			String verificationType = "verificacao1";
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			Path pathTarget = generateTargetFilesPath();
			PrecoZeradoTodosExtractor extractor = new PrecoZeradoTodosExtractor();
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
				.append("preco-zerado-todos")
				.append(File.separator)
				.append("target");
		return Paths.get(sb.toString());
	}
}
