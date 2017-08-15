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
	
	public ReportTabBuilder() {
		this.tab = new ReportTab();
		this.rows = new ArrayList<>();
		this.cells = new ArrayList<>();
		this.dimensions = new ReportTableDimensions();
	}
	
	public ReportTab build() {
		this.tab.setRows(rows);
		return this.tab;
	}
	
	public void addNumberOfRows(int rows) {
		this.dimensions.setRows(rows);
	}
	
	public void addNumberOfColumns(int columns) {
		this.dimensions.setRows(columns);
	}
	
	public void addCell(final ReportCell cell) {
		this.cells.add(cell);
	}
	
	public void jumpToNextRow(int currentIndex) {
		ReportRow row = new ReportRow();
		row.setCells(this.cells);
		row.setIndex(currentIndex);
		this.rows.add(row);
		this.cells = new ArrayList<>();
	}
	
}
