package utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import config.PathBuilder;
import config.ProjectConfiguration;
import extractors.ExcelExtractor;
import model.ReportDocument;

public class FilesManager {
	
	private static List<ReportDocument> files = new ArrayList<>();
	private static Stream<ReportDocument> stream = files.stream();
	
	public static void addFileToRepository(final ReportDocument file) {
		if (!FilesManager.has(file)) {
			files.add(file);
		}
	}
	
	private static Boolean has(final ReportDocument file) {
		return stream.anyMatch(e -> e.equals(file));
	}
	
	private static Boolean has(final String fileName) {
		return stream.anyMatch(e -> e.getFileName().equals(fileName));
	}
	
	public static List<ReportDocument> fetchDocumentsBy(final List<Path> fileNames) {

		List<ReportDocument> filesToSend = new ArrayList<>();
		
		for (Path fileName : fileNames) {
//			if (!FilesManager.has(fileName)) {
//				FilesManager.processFile(fileName);
//			}
			filesToSend.add(stream.filter(e -> e.getFileName().equals(fileName)).findFirst().orElse(null));
		}
		
		return filesToSend;
	}
	
	public static List<ReportDocument> fetchAllDocumentsInMemory() {
		return files;
	}
	
	private static void processFile(String fileName) {
		ExcelExtractor extractor = new ExcelExtractor();
//		extractor.process(fileName);
	}
}
