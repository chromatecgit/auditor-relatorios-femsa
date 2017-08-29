package model;

import java.util.LinkedHashMap;
import java.util.Map;

import interfaces.ReportTabBuilder;
import utils.ReportTabBuilderIndexVO;

public class ReportVerticalTabBuilder implements ReportTabBuilder {

	private ReportTab tab;
	// Column Letter Index X Column Name
	private Map<String, String> tableHeaders;
	private String[] filters;
	// Concat X Line Index
	private ReportTabBuilderIndexVO currentConcatLineIndex;
	private String currentSKU;
	private String currentPoc;

	public ReportVerticalTabBuilder(final String[] filters) {
		this.tab = new ReportTab();
		this.tableHeaders = new LinkedHashMap<>();
		this.currentSKU = "";
		this.currentConcatLineIndex = new ReportTabBuilderIndexVO();
		this.filters = filters;
		this.currentPoc = "";
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
			} else if (cellValue.equals("POC")) {
				this.currentPoc = cell.getValue();
			} else if (cell.getLineIndex() == currentConcatLineIndex.getLineIndex()) {
				if (!this.isInFilter(cellValue)) {
					this.addAndReset(cell, cellValue);
				}
			}
		}
	}
	
	private boolean isInFilter(String cellValue) {
		for (String filter : filters) {
			if (cellValue.contains(filter))
				return true;
		}
		return false;
	}

	@Override
	public void addAndReset(ReportCell cell, String cellValue) {
		ReportCellKey cellKey = new ReportCellKey();
		cellKey.setConcat(currentConcatLineIndex.getConcat());
		cellKey.setColumnName(this.currentSKU.isEmpty() ? cellValue : currentSKU);
		this.currentSKU = "";
		this.currentPoc = "";
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
