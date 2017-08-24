package utils;

import java.util.ArrayList;
import java.util.List;

import model.ReportCell;
import model.ReportColumn;
import model.ReportKeyColumn;
import model.ReportRow;
import model.ReportTab;
import model.ReportTableDimensions;

public class ReportTabBuilder {
	private ReportTab tab;
	private ReportTableDimensions dimensions;
	private List<ReportRow> rows;
	private List<ReportKeyColumn> tableColumns;
	private List<ReportCell> cells;
	private int lastLineIndex;

	public ReportTabBuilder() {
		this.tab = new ReportTab();
		this.cells = new ArrayList<>();
		this.tableColumns = new ArrayList<>();
		this.rows = new ArrayList<>();
		this.dimensions = new ReportTableDimensions();
		this.lastLineIndex = 1;
	}

	public ReportTab build() {
		this.tab.setTableColumns(tableColumns);
		this.tab.setRows(this.rows);
		this.tab.setDimensions(this.dimensions);
		return this.tab;
	}

	public void addNumberOfRows(int rows) {
		this.dimensions.setRows(rows);
	}

	public void addNumberOfColumns(int columns) {
		this.dimensions.setColumns(columns);
	}

	public void addCell(final ReportCell cell) {
		if (cell.getLineIndex() == 1) {
			ReportKeyColumn r = new ReportKeyColumn();
			r.setIndex(cell.getColumnIndex());
			r.setValue(cell.getValue());
			this.tableColumns.add(r);
		} else {
			if (this.lastLineIndex == cell.getLineIndex()) {
				this.cells.add(cell);
			} else {
				this.addAndReset(cell);
			}
		}
	}

	private void addAndReset(ReportCell cell) {
		if (this.lastLineIndex == 1) {
			this.cells.add(cell);
		} else {
			ReportRow row = new ReportRow();
			row.setCells(this.cells);
			row.setIndex(lastLineIndex);
			this.rows.add(row);
			this.cells = new ArrayList<>();
			this.cells.add(cell);
			
		}
		this.lastLineIndex = cell.getLineIndex();
	}

	public int getLastLineIndex() {
		return lastLineIndex;
	}

	public void setLastLineIndex(int lastLineIndex) {
		this.lastLineIndex = lastLineIndex;
	}

}
