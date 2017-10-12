package builders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import interfaces.ReportTabBuilder;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.FileManager;
import utils.FormattingUtils;
import utils.MyLogPrinter;
import utils.ReportTabBuilderIndexVO;

//FIXME: Ver comentario do Builder Vertical
public class ReportHorizontalTabBuilder implements ReportTabBuilder {
	
	private ReportTab tab;
	// Column Letter Index X Column Name
	private Map<String, String> tableHeaders;
	// Concat X Line Index
	private ReportTabBuilderIndexVO indexVO;
	
	public ReportHorizontalTabBuilder() {
		this.tab = new ReportTab();
		this.tab.setCells(new TreeMap<ReportCellKey, ReportCell>());
		this.tableHeaders = new LinkedHashMap<>();
		this.indexVO = new ReportTabBuilderIndexVO();
	}

	@Override
	public ReportTab build() {
		MyLogPrinter.printBuiltMessage("ReportHorizontalTabBuilder_orphan_cells");
		this.tab.setTableHeaders(tableHeaders);
		return this.tab;
	}

	@Override
	public void addCell(ReportCell cell) {
		if (cell.getLineIndex() == 1) { //&& cell.getValue().contains(tag)) {
			this.tableHeaders.put(cell.getColumnIndex(), cell.getValue());
		} else {
			String correspondingHeader = this.tableHeaders.get(cell.getColumnIndex());
			if (correspondingHeader != null) {
				if (correspondingHeader.equalsIgnoreCase("CONCAT")) {
					indexVO.setConcat(cell.getValue());
					indexVO.setLineIndex(cell.getLineIndex());
				} else if (cell.getLineIndex() == indexVO.getLineIndex() && !cell.getValue().equals("0")) {
					this.addAndReset(cell, correspondingHeader);
				}
			} else {
				MyLogPrinter.addToBuiltMessage("Valor inválido em :" + cell.getAddress());
			}
		}
	}

	@Override
	public void addAndReset(ReportCell cell, String correspondingHeader) {
		ReportCellKey cellKey = new ReportCellKey();
		cellKey.setConcat(indexVO.getConcat());
		cellKey.setColumnName(correspondingHeader);
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
