package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.FilesPerModuleEnum;
import enums.ProcessStageEnum;
import extractors.ExcelExtractor;
import extractors.WorkbookExtractor;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportHorizontalTabBuilder;
import model.ReportTab;
import model.TabNamesMap;
import utils.FileManager;

public class SpreadsheetCreator {
	public static void main(String[] args) {
		final String[] fileNames = FilesPerModuleEnum.SOURCE.getExcelFileNames();
		final Path destinationPath = Paths.get("D:\\teste.xlsx");
		
		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)), ProjectConfiguration.sourcePath );

		final Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();
		
		
		
		for (String fileName : pathsMap.keySet()) {
			try {
				final OPCPackage pkg = OPCPackage.open(pathsMap.get(fileName).getPath().toFile());
				final XSSFReader reader= new XSSFReader(pkg);
				final WorkbookExtractor workbookExtractor = new WorkbookExtractor();
				final List<TabNamesMap> tabNamesMapList =  workbookExtractor.extractSheetNamesFrom(reader.getWorkbookData());
				
				List<ReportTab> tabs = tabNamesMapList.stream().map( tabMap ->  {
					final ReportHorizontalTabBuilder reportHorizontalTabBuilder = new ReportHorizontalTabBuilder();
					reportHorizontalTabBuilder.addDocumentName(fileName);
					final ExcelExtractor e = new ExcelExtractor(fileName, reportHorizontalTabBuilder);
					e.process(reader, tabMap, ProcessStageEnum.FULL);
					
					return e.getProcessedTab();
				}).collect(Collectors.toList());
				
				SpreadsheetCreator.create(tabs, destinationPath);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void create(List<ReportTab> tabs, Path path) {
		
		try {
			XSSFWorkbook newWorkBook = new XSSFWorkbook();
	
			tabs.forEach( t -> {
				List<String> tableHeaders = t.getTableHeaders().values().stream().collect(Collectors.toList());
				XSSFSheet sheet = newWorkBook.createSheet(t.getTabName());
				List<Entry<ReportCellKey, ReportCell>> entries = t.getCells().entrySet().stream().collect(Collectors.toList());
				for (int i = 0; i < t.getNumberOfRows(); i++) {
					XSSFRow row = sheet.createRow(i);
					for (int j = 0; j < tableHeaders.size(); j++) {
						if (i == 0) {
							row.createCell(j).setCellValue(tableHeaders.get(j));
						} else {
							if (j == 0) {
								row.createCell(j).setCellValue(entries.get(j).getKey().getConcat());
							} else {
								
							}
						}
					}
				}
			});
			
			FileOutputStream fileOut = new FileOutputStream("D:\\workbook.xlsx");
			newWorkBook.write(fileOut);
		    fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
