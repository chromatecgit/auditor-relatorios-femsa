package model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import interfaces.ReportTabBuilder;
import utils.MyLogPrinter;
import utils.ReportTabBuilderIndexVO;

public class ReportVerticalTabBuilder implements ReportTabBuilder {

	private ReportTab tab;
	// Column Letter Index X Column Name
	private Map<String, String> tableHeaders;
	private String[] filters;
	// Concat X Line Index
	private ReportTabBuilderIndexVO indexVO;
	private String currentSKU;
	private String currentPoc;

	public ReportVerticalTabBuilder(final String[] filters) {
		this.tab = new ReportTab();
		this.tab.setCells(new TreeMap<ReportCellKey, ReportCell>());
		this.tableHeaders = new LinkedHashMap<>();
		this.currentSKU = "";
		this.indexVO = new ReportTabBuilderIndexVO();
		this.filters = filters;
		this.currentPoc = "";
	}

	@Override
	public ReportTab build() {
		MyLogPrinter.printBuiltMessage("ReportVerticalTabBuilder_orphan_cells");
		return this.tab;
	}

	@Override
	public void addCell(ReportCell cell) {
		if (cell.getLineIndex() == 1) { // && cell.getValue().contains(tag)) {
			this.tableHeaders.put(cell.getColumnIndex(), cell.getValue());
		} else {
			String cellValue = this.tableHeaders.get(cell.getColumnIndex());
			if (cellValue != null) {
				if (cellValue.equalsIgnoreCase("CONCAT")) {
					indexVO.setConcat(cell.getValue());
					indexVO.setLineIndex(cell.getLineIndex());
				} else if (cellValue.equals("PRODUTO")) {
					this.currentSKU = cell.getValue();
				} else if (cellValue.equals("POC")) {
					this.currentPoc = cell.getValue();
				} else if (cell.getLineIndex() == indexVO.getLineIndex()) {
					if (!this.isInFilter(cellValue)) {
						this.addAndReset(cell, cellValue);
					}
				} else {
					MyLogPrinter.addToBuiltMessage("Valor inv�lido em :" + cell.getAddress());
				}
			}
		}
	}

	private boolean isInFilter(String cellValue) {
		if (filters != null) {
			for (String filter : filters) {
				if (cellValue.contains(filter))
					return true;
			}
		}
		return false;
	}

	@Override
	public void addAndReset(ReportCell cell, String cellValue) {
		ReportCellKey cellKey = new ReportCellKey();
		cellKey.setConcat(this.indexVO.getConcat());
		cellKey.setColumnName(this.currentSKU.isEmpty() ? cellValue : currentSKU);
		// TODO: Arrumar um jeito melhor de fazer isso
		if (this.tableHeaders.get(cell.getColumnIndex()).equalsIgnoreCase("SOVI")) {
			cell.getPocInfos().put(this.currentPoc, Integer.valueOf(cell.getValue()));
		}
		ReportCell result = this.tab.getCells().put(cellKey, cell);
		if (result != null && cellKey.getColumnName().startsWith("SOVI")) {
			ReportCell reportCell = this.tab.getCells().get(cellKey);
			reportCell.getPocInfos().putAll(result.getPocInfos());
			reportCell.setValue(
					String.valueOf(Integer.valueOf(reportCell.getValue()) + Integer.valueOf(result.getValue())));
			this.tab.getCells().put(cellKey, reportCell);
		}
		this.currentSKU = "";
		this.currentPoc = "";
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
