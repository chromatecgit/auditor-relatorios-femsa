package utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.ReportDocument;
import model.ReportTab;
import model.TabNamesMap;

public class FileManager {
	
	private static List<ReportTab> tabs = new ArrayList<>();
	
	public static ReportDocument fetchDocumentBy(final String fileName, final Path path, final ProcessStageEnum processStage) {
		
		ExcelExtractor extractor = new ExcelExtractor(fileName);
		extractor.process(path, processStage);
		return extractor.getDocumentBuilder().build();
	}
	
}
