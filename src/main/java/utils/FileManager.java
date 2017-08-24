package utils;

import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.PathBuilderMapValue;
import model.ReportDocument;
import model.TabNamesMap;

public class FileManager {
	
	public static ReportDocument fetchDocumentsBy(Map<String, PathBuilderMapValue> paths, ProcessStageEnum processStage) {
		
		for (String key : paths.keySet()) {
			
			PathBuilderMapValue pbmv = paths.get(key);
			OPCPackage pkg = OPCPackage.open(pbmv.getPath().toFile());
			XSSFReader reader = new XSSFReader(pkg);
			WorkbookExtractor we = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = we.extractSheetNamesFrom(reader.getWorkbookData());
			
			if (pbmv.isVertical()) {
				ExcelExtractor e = new ExcelExtractor();
				
			}
			
		}
		return null;
		
	}
}
