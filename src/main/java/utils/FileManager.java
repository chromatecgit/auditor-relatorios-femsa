package utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import model.ReportDocument;
import model.TabNamesMap;

public class FileManager {
	
	private static List<ReportDocument> files = new ArrayList<>();
	private static List<TabNamesMap> tabs = new ArrayList<>();
	public static int lastVisitedLine;
	
	public static void addFileToRepository(final ReportDocument file) {
		if (!FileManager.has(file)) {
			files.add(file);
		}
	}
	
	public static void addTabToRepository(final TabNamesMap tab) {
		tabs.add(tab);
	}
	
	private static Boolean has(final ReportDocument file) {
		return files.stream().anyMatch(e -> e.equals(file));
	}
	
	private static Boolean has(final String fileName) {
		return files.stream().anyMatch(e -> e.getFileName().equals(fileName));
	}
	
	public static ReportDocument fetchDocumentBy(final String fileName, final Path path, final ProcessStageEnum processStage) {
		if (!FileManager.has(fileName)) {
			FileManager.processFile(path, fileName, processStage);
		}
		ReportDocument fileToSend = (files.stream().filter(e -> e.getFileName().equals(fileName)).findFirst().orElse(null));
		return fileToSend;
	}
	
	public static List<ReportDocument> fetchDocumentsBy(final Map<String, Path> pathMaps, final ProcessStageEnum processStage) {

		List<ReportDocument> filesToSend = new ArrayList<>();
		
		for (String fileName : pathMaps.keySet()) {
			if (!FileManager.has(fileName)) {
				FileManager.processFile(pathMaps.get(fileName), fileName, processStage);
			}
			filesToSend.add(files.stream().filter(e -> e.getFileName().equals(fileName)).findFirst().orElse(null));
		}
		
		return filesToSend;
	}
	
	public static List<ReportDocument> fetchAllDocumentsInMemory() {
		return files;
	}
	
	private static void processFile(final Path filePath, final String fileName, final ProcessStageEnum processStage) {
		ExcelExtractor extractor = new ExcelExtractor(fileName);
		extractor.process(filePath, processStage);
		lastVisitedLine = extractor.getDocumentBuilder().getLastVisitedLine();
		files.add(extractor.getDocumentBuilder().build());
	}
}
