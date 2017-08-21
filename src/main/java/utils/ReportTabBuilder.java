package utils;

import java.util.ArrayList;
import java.util.List;

import model.ReportCell;
import model.ReportKeyColumns;
import model.ReportRow;
import model.ReportTab;
import model.ReportTableDimensions;

public class ReportTabBuilder {
	private ReportTab tab;
	private ReportTableDimensions dimensions;
	private List<ReportRow> rows;
	private List<ReportKeyColumns> tableColumns;
	private List<ReportCell> cells;

	public ReportTabBuilder() {
		this.tab = new ReportTab();
		this.cells = new ArrayList<>();
		this.tableColumns = new ArrayList<>();
		this.rows = new ArrayList<>();
		this.dimensions = new ReportTableDimensions();
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
			ReportKeyColumns r = new ReportKeyColumns();
			r.setIndex(cell.getColumnIndex());
			r.setValue(cell.getValue());
			this.tableColumns.add(r);
		} else {
			this.addAndReset(cell);
		}
	}

	private void addAndReset(ReportCell cell) {
		ReportRow row = new ReportRow();
		row.setCells(this.cells);
		this.rows.add(row);
		this.cells = new ArrayList<>();
		this.cells.add(cell);
	}


}
