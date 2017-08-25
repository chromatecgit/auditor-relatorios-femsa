package model;

import java.util.LinkedHashMap;
import java.util.Map;

import interfaces.ReportTabBuilder;
import utils.ReportTabBuilderIndexVO;

public class ReportVerticalTabBuilder implements ReportTabBuilder {

	private ReportTab tab;
	// Column Letter Index X Column Name
	private Map<String, String> tableHeaders;
	private String tag;
	// Concat X Line Index
	private ReportTabBuilderIndexVO currentConcatLineIndex;
	private String currentSKU;

	public ReportVerticalTabBuilder(final String tag) {
		this.tab = new ReportTab();
		this.tableHeaders = new LinkedHashMap<>();
		this.currentSKU = "";
		this.currentConcatLineIndex = new ReportTabBuilderIndexVO();
		this.tag = tag;
	}

	@Override
	public ReportTab build() {
		return this.tab;
	}

	@Override
	public void addCell(ReportCell cell) {
		if (cell.getLineIndex() == 1) { // && cell.getValue().contains(tag)) {
			this.tableHeaders.put(cell.getColumnIndex(), cell.getValue());
		} else {
			String cellValue = this.tableHeaders.get(cell.getColumnIndex());
			if (cellValue.equalsIgnoreCase("CONCAT")) {
				currentConcatLineIndex.setConcat(cell.getValue());
				currentConcatLineIndex.setLineIndex(cell.getLineIndex());
			} else if (cellValue.equals("PRODUTO")) {
				this.currentSKU = cell.getValue();
			} else if (cell.getLineIndex() == currentConcatLineIndex.getLineIndex()) {
				this.addAndReset(cell, cellValue);
			}
		}
	}

	@Override
	public void addAndReset(ReportCell cell, String cellValue) {
		ReportCellKey cellKey = new ReportCellKey();
		cellKey.setConcat(currentConcatLineIndex.getConcat());
		cellKey.setColumnName(this.currentSKU.isEmpty() ? cellValue : currentSKU);
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

	@Override
	public void addDocumentName(String fileName) {
		this.tab.setFileName(fileName);
	}

	@Override
	public void addTabName(String tabName) {
		this.tab.setTabName(tabName);
	}

}
