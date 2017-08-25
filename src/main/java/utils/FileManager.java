package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.PathBuilderMapValue;
import model.ReportHorizontalTabBuilder;
import model.ReportTab;
import model.ReportVerticalTabBuilder;
import model.TabNamesMap;

public class FileManager {
	
	public static ReportTab fetchHorizontalDocument(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage, boolean skipOutros) {
		
		ReportTab processedTab = new ReportTab();
		
		try {
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader = new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			if (skipOutros) {
				tabNamesMapList.removeIf( t -> t.getName().contains("OUTROS"));
			}
			
			processedTab = tabNamesMapList.stream().map( tabMap ->  {
				ReportHorizontalTabBuilder reportHorizontalTabBuilder = new ReportHorizontalTabBuilder("PRECO");
				reportHorizontalTabBuilder.addDocumentName(fileName);
				ExcelExtractor e = new ExcelExtractor(fileName, reportHorizontalTabBuilder);
				e.process(reader, tabMap, processStage);
				return e.getProcessedTab();
				//MyLogPrinter.printObject(e.getProcessedTab(), "Processed Horizontal Tab");
			}).findFirst().orElse(null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processedTab;
	}
	
	public static ReportTab fetchVerticalDocument(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage) {
		
		final List<ReportTab> processedTabs = new ArrayList<>();
		
		try {
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader = new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			
			tabNamesMapList.parallelStream().forEach( tabMap ->  {
				ReportVerticalTabBuilder reportVerticalTabBuilder = new ReportVerticalTabBuilder("PRECO");
				reportVerticalTabBuilder.addDocumentName(fileName);
				ExcelExtractor e = new ExcelExtractor(fileName, reportVerticalTabBuilder);
				e.process(reader, tabMap, processStage);
				processedTabs.add(e.getProcessedTab());
			});
			//MyLogPrinter.printCollection(verticalProcessedTabs, "Processed Vertical Tab");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		ReportTab mergedTabs = FileManager.merge(processedTabs, fileName);
		processedTabs.clear();
		
		return mergedTabs;

	}
	
	private static ReportTab merge(List<ReportTab> tabs, String fileName) {
		ReportTab newTab = new ReportTab();
		StringBuilder sbTabName = new StringBuilder();
		tabs.parallelStream().forEach( t -> {
			sbTabName.append("_");
			sbTabName.append(t.getTabName());
			sbTabName.append("_");
			newTab.getCells().putAll(t.getCells()); 
		});
		newTab.setFileName(fileName);
		newTab.setTabName(sbTabName.toString());
		return newTab;
	}
	
	//TODO: fazer um metodo para processar aba por aba - fetchSheetBySheet
	//TODO: fazer um metodo para processar aba por aba - fetchSheetBySheet
	//TODO: fazer um metodo para processar apenas um arquivo
}
