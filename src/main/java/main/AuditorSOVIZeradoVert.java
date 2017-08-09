package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import extractors.SOVIZeradoVertExtractor;
import utils.MyFileVisitor;

public class AuditorSOVIZeradoVert {
	
	public static void main(String[] args) {
		System.out.println("AuditorSOVIZeradoVert");
		try {
			String verificationType = "verificacao1";
			String glob = "glob:**/*.{xlsx}";
			
			Path pathTarget = generateTargetFilesPath();
			SOVIZeradoVertExtractor extractor = new SOVIZeradoVertExtractor();
			MyFileVisitor fileVisitor = new MyFileVisitor(pathTarget, glob);
			
			extractor.getResults(fileVisitor.getPaths(), verificationType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Path generateTargetFilesPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				System.getProperty("user.dir"))
				.append(File.separator)
				.append("src")
				.append(File.separator)
				.append("sovi-zerado-vert")
				.append(File.separator)
				.append("target");
		return Paths.get(sb.toString());
	}
}
