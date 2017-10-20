package builders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import interfaces.ReportTabBuilder;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.MyLogPrinter;

public class ReportConsolidadaSoviTabBuilder implements ReportTabBuilder {
	
	private ReportTab tab;
	//cell.getColumnIndex(), cell.getValue()
	private Map<String, String> tableHeaders;
	private String currentSKU;
	
	public ReportConsolidadaSoviTabBuilder() {
		this.tab = new ReportTab();
		this.tableHeaders = new LinkedHashMap<>();
		this.tab.setCells(new TreeMap<ReportCellKey, ReportCell>());
		this.currentSKU = "";
	}

	@Override
	public void addCell(ReportCell cell) {
		if (cell.getLineIndex() == 1) { // && cell.getValue().contains(tag)) {
			this.tableHeaders.put(cell.getColumnIndex(), cell.getValue());
		} else {
			String cellValue = this.tableHeaders.get(cell.getColumnIndex());
			if (cellValue != null) {
				if (cellValue.equalsIgnoreCase("CONCAT")) {
				} else if (cell.getLineIndex() == indexVO.getLineIndex() && !cell.getValue().equals("0")) {
					this.addAndReset(cell, cellValue);
				}
			} else {
				MyLogPrinter.addToBuiltMessage("Valor inv�lido em :" + cell.getAddress());
			}
		}
	}

	@Override
	public void addAndReset(final ReportCell cell, final String cellValue) {
		ReportCellKey cellKey = new ReportCellKey(indexVO.getConcat(), this.currentSKU.isEmpty() ? cellValue : currentSKU, "");
		this.currentSKU = "";
		this.tab.getCells().put(cellKey, cell);
	}
	
	@Override
	public ReportTab build() {
		MyLogPrinter.printBuiltMessage("ReportConsolidadaSoviTabBuilder_orphan_cells");
		return this.tab;
	}

	@Override
	public void addDocumentName(String fileName) {
		this.tab.setFileName(fileName);
	}

	@Override
	public void addTabName(String tabName) {
		this.tab.setTabName(tabName);
	}

	@Override
	public void addNumberOfRows(int rows) {
		this.tab.setNumberOfRows(rows);
	}

	@Override
	public void addNumberOfColumns(int columns) {
		this.tab.setNumberOfColumns(columns);
	}

}