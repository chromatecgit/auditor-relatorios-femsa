package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.PathBuilderMapValue;
import model.ReportDocument;
import model.ReportHorizontalTabBuilder;
import model.ReportTab;
import model.ReportVerticalTabBuilder;
import model.TabNamesMap;

public class FileManager {

	public static ReportDocument fetchDocumentsBy(String fileName, PathBuilderMapValue pathMap,
			ProcessStageEnum processStage) {
		List<ReportTab> verticalProcessedTabs = new ArrayList<>();
		try {
			
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader = new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			if (!pathMap.isVertical()) {
				for (TabNamesMap tabMap : tabNamesMapList) {
					ReportHorizontalTabBuilder reportHorizontalTabBuilder = new ReportHorizontalTabBuilder("PRECO");
					reportHorizontalTabBuilder.addDocumentName(fileName);
					ExcelExtractor e = new ExcelExtractor(fileName, reportHorizontalTabBuilder);
					e.process(reader, tabMap, processStage);
					MyLogPrinter.printObject(e.getProcessedTab(), "Processed Horizontal Tab");
				}
			} else {
				for (TabNamesMap tabMap : tabNamesMapList) {
					ReportVerticalTabBuilder reportVerticalTabBuilder = new ReportVerticalTabBuilder("PRECO");
					reportVerticalTabBuilder.addDocumentName(fileName);
					ExcelExtractor e = new ExcelExtractor(fileName, reportVerticalTabBuilder);
					e.process(reader, tabMap, processStage);
					verticalProcessedTabs.add(e.getProcessedTab());
					
				}
				MyLogPrinter.printCollection(verticalProcessedTabs, "Processed Vertical Tab");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
}
