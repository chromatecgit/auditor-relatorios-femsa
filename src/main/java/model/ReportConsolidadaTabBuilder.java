package model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import interfaces.ReportTabBuilder;
import utils.MyLogPrinter;
import utils.ReportTabBuilderIndexVO;

public class ReportConsolidadaTabBuilder implements ReportTabBuilder {

	private ReportTab tab;
	// Column Letter Index X Column Name
	private Map<String, String> tableHeaders;
	// Concat X Line Index
	private ReportTabBuilderIndexVO indexVO;

	public ReportConsolidadaTabBuilder() {
		this.tab = new ReportTab();
		this.tab.setCells(new TreeMap<ReportCellKey, ReportCell>());
		this.tableHeaders = new LinkedHashMap<>();
		this.indexVO = new ReportTabBuilderIndexVO();
	}

	@Override
	public ReportTab build() {
		MyLogPrinter.printBuiltMessage("ReportConsolidadaTabBuilder_orphan_cells");
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
					this.indexVO.setConcat(cell.getValue());
					this.indexVO.setLineIndex(cell.getLineIndex());
				} else if (cell.getLineIndex() == indexVO.getLineIndex()) {// Voltar essa condicao caso necessario && !cell.getValue().equals("0")) {
					this.addAndReset(cell, cellValue);
				}
			} else {
				MyLogPrinter.addToBuiltMessage("Valor inválido em :" + cell.getAddress());
			}
		}
	}

	@Override
	public void addAndReset(ReportCell cell, String cellValue) {
		ReportCellKey cellKey = new ReportCellKey();
		cellKey.setConcat(indexVO.getConcat());
		cellKey.setColumnName(cellValue);
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
