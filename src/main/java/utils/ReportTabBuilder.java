package utils;

import java.util.ArrayList;
import java.util.List;

import model.ReportCell;
import model.ReportRow;
import model.ReportTab;
import model.ReportTableDimensions;

public class ReportTabBuilder {
	private ReportTab tab;
	private ReportTableDimensions dimensions;
	private List<ReportRow> rows;
	private List<ReportCell> cells;
	private int lastLineIndex;

	public ReportTabBuilder() {
		this.tab = new ReportTab();
		this.cells = new ArrayList<>();
		this.rows = new ArrayList<>();
		this.dimensions = new ReportTableDimensions();
		this.lastLineIndex = 1;
	}

	public ReportTab build() {
		this.tab.setRows(this.rows);
		this.tab.setDimensions(this.dimensions);
		MyLogPrinter.printObject(rows, "Rows");
		return this.tab;
	}

	public void addNumberOfRows(int rows) {
		this.dimensions.setRows(rows);
	}

	public void addNumberOfColumns(int columns) {
		this.dimensions.setColumns(columns);
	}

	public void addCell(final ReportCell cell) {
		if (this.lastLineIndex == cell.getLineIndex()) {
			this.cells.add(cell);
		} else {
			this.addAndReset(cell.getLineIndex());
		}
	}

	private void addAndReset(int newIndex) {
		ReportRow row = new ReportRow();
		row.setCells(this.cells);
		row.setIndex(lastLineIndex);
		this.rows.add(row);
		this.lastLineIndex = newIndex;
		this.cells = new ArrayList<>();
	}

	public int getLastLineIndex() {
		return lastLineIndex;
	}

	public void setLastLineIndex(int lastLineIndex) {
		this.lastLineIndex = lastLineIndex;
	}

}
