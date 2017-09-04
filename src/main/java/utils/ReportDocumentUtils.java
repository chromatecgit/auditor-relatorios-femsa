package utils;

import java.util.TreeMap;

import model.ReportDocument;
import model.ReportTab;

public class ReportDocumentUtils {
	public static ReportTab merge(final ReportDocument document) {
		ReportTab newTab = new ReportTab();
		newTab.setCells(new TreeMap<>());
		StringBuilder sbTabName = new StringBuilder();
		
		document.getTabs().values().stream().forEach( t -> {
			sbTabName.append("#");
			sbTabName.append(t.getTabName());
			newTab.setNumberOfColumns(newTab.getNumberOfColumns() + t.getNumberOfColumns());
			newTab.setNumberOfRows(newTab.getNumberOfRows() + t.getNumberOfRows());
			
			newTab.getCells().putAll(t.getCells()); 
		});
		newTab.setFileName(document.getFileName());
		newTab.setTabName(sbTabName.toString());
		
		return newTab;
	}
	
	public static ReportTab group(final ReportDocument document) {
		ReportTab newTab = new ReportTab();
		newTab.setCells(new TreeMap<>());
		StringBuilder sbTabName = new StringBuilder();
		
		document.getTabs().values().stream().forEach( t -> {
			sbTabName.append("#");
			sbTabName.append(t.getTabName());
			newTab.setNumberOfColumns(newTab.getNumberOfColumns() + t.getNumberOfColumns());
			newTab.setNumberOfRows(newTab.getNumberOfRows() + t.getNumberOfRows());
			
			newTab.getCells().putAll(t.getCells()); 
		});
		newTab.setFileName(document.getFileName());
		newTab.setTabName(sbTabName.toString());
		
		return newTab;
	}
}
