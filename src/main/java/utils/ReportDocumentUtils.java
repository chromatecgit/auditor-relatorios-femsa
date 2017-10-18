package utils;

import java.util.Map;
import java.util.TreeMap;

import model.ReportCell;
import model.ReportCellKey;
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
			//TODO: Achar jeito melhor de fazer isso
			Map<ReportCellKey, ReportCell> newMap = ReportDocumentUtils.concatTabName(t.getCells(), t.getTabName());
			//newTab.getCells().putAll(t.getCells());
			newTab.getCells().putAll(newMap);
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
	
	private static Map<ReportCellKey, ReportCell> concatTabName(Map<ReportCellKey, ReportCell> cells, final String tabName) {
		cells.forEach( (k, v) -> {
			v.setAddress(v.getAddress() + "_" + tabName);
		});
		
		return cells;
	}
}
