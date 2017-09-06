package model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import interfaces.ReportTabBuilder;
import utils.MyLogPrinter;
import utils.ReportTabBuilderIndexVO;

public class ReportConsolidadaSoviTabBuilder implements ReportTabBuilder {
	
	private ReportTab tab;
	//cell.getColumnIndex(), cell.getValue()
	private Map<String, String> tableHeaders;
	private ReportTabBuilderIndexVO currentConcatLineIndex;
	private String currentSKU;
	
	public ReportConsolidadaSoviTabBuilder() {
		this.tab = new ReportTab();
		this.tableHeaders = new LinkedHashMap<>();
		this.tab.setCells(new TreeMap<ReportCellKey, ReportCell>());
		this.currentSKU = "";
		this.currentConcatLineIndex = new ReportTabBuilderIndexVO();
	}

	@Override
	public ReportTab build() {
		MyLogPrinter.printBuiltMessage("ReportConsolidadaSoviTabBuilder_null_cells");
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
	public void addCell(ReportCell cell) {
		if (cell.getLineIndex() == 1) { // && cell.getValue().contains(tag)) {
			this.tableHeaders.put(cell.getColumnIndex(), cell.getValue());
		} else {
			String cellValue = this.tableHeaders.get(cell.getColumnIndex());
			if (cellValue != null) {
				if (cellValue.equalsIgnoreCase("CONCAT")) {
					currentConcatLineIndex.setConcat(cell.getValue());
					currentConcatLineIndex.setLineIndex(cell.getLineIndex());
				} else if (cell.getLineIndex() == currentConcatLineIndex.getLineIndex() && !cell.getValue().equals("0")) {
					this.addAndReset(cell, cellValue);
				}
			} else {
				MyLogPrinter.addToBuiltMessage("Valor inválido em :" + cell.getAddress());
			}
		}
	}

	@Override
	public void addAndReset(final ReportCell cell, final String cellValue) {
		ReportCellKey cellKey = new ReportCellKey(currentConcatLineIndex.getConcat(), this.currentSKU.isEmpty() ? cellValue : currentSKU, "");
		this.currentSKU = "";
		this.tab.getCells().put(cellKey, cell);
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
