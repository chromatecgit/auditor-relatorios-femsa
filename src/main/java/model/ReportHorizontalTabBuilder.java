package model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import interfaces.ReportTabBuilder;

public class ReportHorizontalTabBuilder implements ReportTabBuilder {
	
	private ReportTab tab;
	private Map<String, String> tableHeaders;
	private String tag;
	
	public ReportHorizontalTabBuilder(final String tag) {
		this.tab = new ReportTab();
		this.tableHeaders = new LinkedHashMap<>();
		this.tag = tag;
	}

	@Override
	public ReportTab build() {
		return this.tab;
	}

	@Override
	public void addCell(ReportCell cell) {
		if (cell.getLineIndex() == 1) { //&& cell.getValue().contains(tag)) {
			this.tableHeaders.put(cell.getColumnIndex(), cell.getValue());
		} else {
			String cellValue = this.tableHeaders.get(cell.getColumnIndex());
			ReportCellKey cellKey = new ReportCellKey();
			if (cellValue.equalsIgnoreCase("CONCAT")) {
				//TODO: Crar um mapa para os concats também, talvez,
				// para colocá-los em todas as células. Acho que nessa hora não vale a
				// pena filtrar pro SKU, por causa da verificacao de entrega
				// e ela precisa da tabela praticamente integral.
//				cellKey.setConcat(concat);
//				cellKey.setSku(sku);
			}
			this.tab.getCells().put(cellKey, cell);
		}
	}

	@Override
	public void addAndReset() {
		// TODO Auto-generated method stub
	}

	@Override
	public void addNumberOfRows(int rows) {
		this.tab.setNumberOfRows(rows);
	}

	@Override
	public void addNumberOfColumns(int columns) {
		this.tab.setNumberOfColumns(columns);
	}

	@Override
	public void addDocumentName(String fileName) {
		this.tab.setFileName(fileName);
	}

}
