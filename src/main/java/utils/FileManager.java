package utils;

import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.PathBuilderMapValue;
import model.ReportDocument;
import model.ReportVerticalTabBuilder;
import model.TabNamesMap;

public class FileManager {
	
	public static ReportDocument fetchDocumentsBy(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage) {
		
		try {
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader = new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			if (pathMap.isVertical()) {
				ReportVerticalTabBuilder reportVerticalTabBuilder = new ReportVerticalTabBuilder("PRECO");
				reportVerticalTabBuilder.addDocumentName(fileName);
				ExcelExtractor e = new ExcelExtractor(fileName, reportVerticalTabBuilder);
				for (TabNamesMap tabMap : tabNamesMapList) {
					e.process(reader, tabMap, processStage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return null;
		
	}
}
