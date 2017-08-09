package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.VelhoNovoExtractor;
import utils.MyFileVisitor;

public class AuditorVelhoNovo {
	
	public static void main(String[] args) {
		System.out.println("AuditorVelhoNovo");
		try {
			String glob = "glob:**/*.{xlsx}";
			
			Path pathRef = generateReferenceFilePath();
			Path pathTarget = generateTargetFilesPath();
			VelhoNovoExtractor extractor = new VelhoNovoExtractor();
			MyFileVisitor fv1 = new MyFileVisitor(pathRef, glob);
			MyFileVisitor fv2 = new MyFileVisitor(pathTarget, glob);
			
			extractor.getResults(fv1.getPaths(), fv2.getPaths());
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
				.append("velho-novo")
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
				.append("velho-novo")
				.append(File.separator)
				.append("target");
		return Paths.get(sb.toString());
	}

}
