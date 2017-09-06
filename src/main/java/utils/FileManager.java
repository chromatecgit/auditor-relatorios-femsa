package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.PathBuilderMapValue;
import model.ReportConsolidadaSoviTabBuilder;
import model.ReportDocument;
import model.ReportHorizontalTabBuilder;
import model.ReportProdutividadeTabBuilder;
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
	
	public static ReportTab fetchProdutividadeDocument(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage) {
		
		ReportTab processedTab = new ReportTab();
		
		try {
			
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader= new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());

			processedTab = tabNamesMapList.stream().map( tabMap ->  {
				ReportProdutividadeTabBuilder reportProdutividadeTabBuilder = new ReportProdutividadeTabBuilder();
				reportProdutividadeTabBuilder.addDocumentName(fileName);
				ExcelExtractor e = new ExcelExtractor(fileName, reportProdutividadeTabBuilder);
				e.process(reader, tabMap, processStage);
				return e.getProcessedTab();
				//MyLogPrinter.printObject(e.getProcessedTab(), "Processed Horizontal Tab");
			}).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processedTab;
	}
	
	public static ReportDocument fetchVerticalDocument(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage, String[] filters) {
		
		final ReportDocument document = new ReportDocument();
		final Map<String, ReportTab> processedTabs = new HashMap<>();
		
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
				processedTabs.put(tabMap.getName(), e.getProcessedTab());
			});
			//MyLogPrinter.printCollection(verticalProcessedTabs, "Processed Vertical Tab");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		document.setTabs(processedTabs);
		document.setFileName(fileName);
		
		return document;

	}
	
	public static ReportTab fetchConsolidadaDocument(String fileName, PathBuilderMapValue pathMap, ProcessStageEnum processStage) {
		
		ReportTab processedTab = new ReportTab();
		
		try {
			
			OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			XSSFReader reader= new XSSFReader(pkg);
			WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			
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
	
	
	//TODO: fazer um metodo para processar aba por aba - fetchSheetBySheet
	//TODO: fazer um metodo para processar aba por aba - fetchSheetBySheet
	//TODO: fazer um metodo para processar apenas um arquivo
}
