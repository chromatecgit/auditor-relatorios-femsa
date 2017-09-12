package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import enums.ProcessStageEnum;
import exceptions.WarningException;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.PathBuilderMapValue;
import model.ReportConsolidadaSoviTabBuilder;
import model.ReportConsolidadaTabBuilder;
import model.ReportDocument;
import model.ReportHorizontalTabBuilder;
import model.ReportProdutividadeTabBuilder;
import model.ReportTab;
import model.ReportVerticalTabBuilder;
import model.TabNamesMap;

public class FileManager {
	
	public static ReportTab fetchHorizontalDocument(final String fileName, final PathBuilderMapValue pathMap, final ProcessStageEnum processStage, final boolean skipOutros) {
		ReportTab processedTab = new ReportTab();
		try {
			final OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			final XSSFReader reader= new XSSFReader(pkg);
			final WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			final List<TabNamesMap> tabNamesMapList =  workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			
			if (skipOutros) {
				tabNamesMapList.removeIf( t -> t.getName().contains("OUTROS"));
			}
			
			processedTab = tabNamesMapList.stream().map( tabMap ->  {
				final ReportHorizontalTabBuilder reportHorizontalTabBuilder = new ReportHorizontalTabBuilder();
				reportHorizontalTabBuilder.addDocumentName(fileName);
				final ExcelExtractor e = new ExcelExtractor(fileName, reportHorizontalTabBuilder);
				e.process(reader, tabMap, processStage);
				return e.getProcessedTab();
				//MyLogPrinter.printObject(e.getProcessedTab(), "Processed Horizontal Tab");
			}).findFirst().orElse(null);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return processedTab;
	}
	
	public static ReportTab fetchProdutividadeDocument(final String fileName, final PathBuilderMapValue pathMap, final ProcessStageEnum processStage) {
		ReportTab processedTab = new ReportTab();
		try {
			final OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			final XSSFReader reader= new XSSFReader(pkg);
			final WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			final List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());

			processedTab = tabNamesMapList.stream().map( tabMap ->  {
				final ReportProdutividadeTabBuilder reportProdutividadeTabBuilder = new ReportProdutividadeTabBuilder();
				reportProdutividadeTabBuilder.addDocumentName(fileName);
				final ExcelExtractor e = new ExcelExtractor(fileName, reportProdutividadeTabBuilder);
				e.process(reader, tabMap, processStage);
				return e.getProcessedTab();
				//MyLogPrinter.printObject(e.getProcessedTab(), "Processed Horizontal Tab");
			}).findFirst().orElse(null);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return processedTab;
	}
	
	public static ReportTab fetchConsolidadaDocument(final String fileName, final PathBuilderMapValue pathMap, final ProcessStageEnum processStage) {
		ReportTab processedTab = new ReportTab();
		try {
			final OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			final XSSFReader reader= new XSSFReader(pkg);
			final WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			final List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());

			processedTab = tabNamesMapList.stream().map( tabMap ->  {
				final ReportConsolidadaTabBuilder reportConsolidadaTabBuilder = new ReportConsolidadaTabBuilder();
				reportConsolidadaTabBuilder.addDocumentName(fileName);
				final ExcelExtractor e = new ExcelExtractor(fileName, reportConsolidadaTabBuilder);
				e.process(reader, tabMap, processStage);
				return e.getProcessedTab();
				//MyLogPrinter.printObject(e.getProcessedTab(), "Processed Horizontal Tab");
			}).findFirst().orElse(null);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return processedTab;
	}
	
	public static ReportDocument fetchVerticalDocument(final String fileName, final PathBuilderMapValue pathMap, final ProcessStageEnum processStage, final String[] filters) {
		final ReportDocument document = new ReportDocument();
		final Map<String, ReportTab> processedTabs = new HashMap<>();
		try {
			final OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
			final XSSFReader reader = new XSSFReader(pkg);
			final WorkbookExtractor workbookExtractor = new WorkbookExtractor();
			final List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
			
			tabNamesMapList.stream().forEach( tabMap ->  {
				final ReportVerticalTabBuilder reportVerticalTabBuilder = new ReportVerticalTabBuilder(filters);
				reportVerticalTabBuilder.addDocumentName(fileName);
				final ExcelExtractor e = new ExcelExtractor(fileName, reportVerticalTabBuilder);
				e.process(reader, tabMap, processStage);
				processedTabs.put(tabMap.getName(), e.getProcessedTab());
			});
			//MyLogPrinter.printCollection(verticalProcessedTabs, "Processed Vertical Tab");
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
		document.setTabs(processedTabs);
		document.setFileName(fileName);
		
		return document;

	}
	
	public static ReportTab fetchConsolidadaSoviDocument(final String fileName, final PathBuilderMapValue pathMap, final ProcessStageEnum processStage) throws WarningException {
		if (pathMap != null && pathMap.getPath() != null) {
			ReportTab processedTab = new ReportTab();
			try {
				final OPCPackage pkg = OPCPackage.open(pathMap.getPath().toFile());
				final XSSFReader reader= new XSSFReader(pkg);
				final WorkbookExtractor workbookExtractor = new WorkbookExtractor();
				final List<TabNamesMap> tabNamesMapList = workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
				
				processedTab = tabNamesMapList.stream().map( tabMap ->  {
					final ReportConsolidadaSoviTabBuilder reportConsolidadaSoviTabBuilder = new ReportConsolidadaSoviTabBuilder();
					reportConsolidadaSoviTabBuilder.addDocumentName(fileName);
					final ExcelExtractor e = new ExcelExtractor(fileName, reportConsolidadaSoviTabBuilder);
					e.process(reader, tabMap, processStage);
					return e.getProcessedTab();
					//MyLogPrinter.printObject(e.getProcessedTab(), "Processed Horizontal Tab");
				}).findFirst().orElse(null);
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return processedTab;
		} else {
			throw new WarningException("Arquivo de Sovi Consolidada nao foi encontrado");
		}
	}
	
	public static ReportDocument fetchDocument(final String fileKey, final PathBuilderMapValue pathMap, final ProcessStageEnum processStage, final String[] filters ) throws WarningException {
		if (pathMap != null && pathMap.getPath() != null) {
			ReportDocument document = new ReportDocument();
			
			switch (pathMap.getFileClass().getCode()) {
				case 1:
					document.getTabs().put(fileKey, FileManager.fetchHorizontalDocument(pathMap.getFileName(), pathMap, processStage, true));
					break;
				case 2:
					document = FileManager.fetchVerticalDocument(pathMap.getFileName(), pathMap, processStage, filters);
					break;
				case 3:
					document.getTabs().put(fileKey, FileManager.fetchConsolidadaSoviDocument(pathMap.getFileName(), pathMap, processStage));
					break;
				case 4:
					document.getTabs().put(fileKey, FileManager.fetchConsolidadaDocument(pathMap.getFileName(), pathMap, processStage));
					break;
				default:
					document.getTabs().put(fileKey, FileManager.fetchProdutividadeDocument(pathMap.getFileName(), pathMap, processStage));
			}
			
			return document;
		} else {
			throw new WarningException("Arquivo "+ pathMap.getFileName() +" nao foi encontrado");
		}
	}
	
}
