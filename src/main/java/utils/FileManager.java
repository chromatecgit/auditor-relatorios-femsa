package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.PathBuilderMapValue;
import model.ReportConsolidadaSoviTabBuilder;
import model.ReportHorizontalTabBuilder;
import model.ReportTab;
import model.ReportVerticalTabBuilder;
import model.TabNamesMap;

public class FileManager {
	
	public static ReportTab fetchHorizontalDocument(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage, boolean skipOutros) {
		
		ReportTab processedTab = new ReportTab();
		
		try {
			
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader= new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList =  workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			
			if (skipOutros) {
				tabNamesMapList.removeIf( t -> t.getName().contains("OUTROS"));
			}
			
			processedTab = tabNamesMapList.stream().map( tabMap ->  {
				ReportHorizontalTabBuilder reportHorizontalTabBuilder = new ReportHorizontalTabBuilder();
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
	
	public static ReportTab fetchVerticalDocument(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage, String[] filters) {
		
		final List<ReportTab> processedTabs = new ArrayList<>();
		
		try {
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader = new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			
			tabNamesMapList.stream().forEach( tabMap ->  {
				ReportVerticalTabBuilder reportVerticalTabBuilder = new ReportVerticalTabBuilder(filters);
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
	
	public static ReportTab fetchConsolidadaDocument(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage) {
		
		ReportTab processedTab = new ReportTab();
		
		try {
			
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader= new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList =  workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			
			processedTab = tabNamesMapList.stream().map( tabMap ->  {
				ReportConsolidadaSoviTabBuilder reportConsolidadaSoviTabBuilder = new ReportConsolidadaSoviTabBuilder();
				reportConsolidadaSoviTabBuilder.addDocumentName(fileName);
				ExcelExtractor e = new ExcelExtractor(fileName, reportConsolidadaSoviTabBuilder);
				e.process(reader, tabMap, processStage);
				return e.getProcessedTab();
				//MyLogPrinter.printObject(e.getProcessedTab(), "Processed Horizontal Tab");
			}).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processedTab;
	}
	
	private static ReportTab merge(final List<ReportTab> tabs, final String fileName) {
		ReportTab newTab = new ReportTab();
		newTab.setCells(new TreeMap<>());
		StringBuilder sbTabName = new StringBuilder();
		
		tabs.stream().forEach( t -> {
			sbTabName.append("#");
			sbTabName.append(t.getTabName());
			newTab.setNumberOfColumns(newTab.getNumberOfColumns() + t.getNumberOfColumns());
			newTab.setNumberOfRows(newTab.getNumberOfRows() + t.getNumberOfRows());
			
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
