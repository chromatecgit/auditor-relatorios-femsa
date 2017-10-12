package builders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import interfaces.ReportTabBuilder;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.MyLogPrinter;
import utils.ReportTabBuilderProdIndexVO;

public class ReportProdutividadeTabBuilder implements ReportTabBuilder {

	private ReportTab tab;
	// Column Letter Index X Column Name
	private Map<String, String> tableHeaders;
	// Concat X Line Index
	private ReportTabBuilderProdIndexVO indexVO;

	public ReportProdutividadeTabBuilder() {
		this.tab = new ReportTab();
		this.tab.setCells(new TreeMap<ReportCellKey, ReportCell>());
		this.tableHeaders = new LinkedHashMap<>();
		this.indexVO = new ReportTabBuilderProdIndexVO();
	}

	@Override
	public ReportTab build() {
		MyLogPrinter.printBuiltMessage("ReportProdutividadeTabBuilder_orphan_cells");
		return this.tab;
	}

	@Override
	public void addCell(ReportCell cell) {
		if (cell.getLineIndex() == 1) { // && cell.getValue().contains(tag)) {
			this.tableHeaders.put(cell.getColumnIndex(), cell.getValue());
		} else {
			String cellValue = this.tableHeaders.get(cell.getColumnIndex());
			if (cellValue != null) {
				if (cellValue.equalsIgnoreCase("MATRICULA")) {
					indexVO.setMatricula(cell.getValue());
					indexVO.setLineIndex(cell.getLineIndex());
				} else if (cellValue.equalsIgnoreCase("DATA")) {
					indexVO.setData(cell.getValue());
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
		cellKey.setConcat(indexVO.getMatricula() + "_" + indexVO.getData());
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
