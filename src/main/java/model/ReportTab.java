package model;

import java.util.Map;
import java.util.TreeMap;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportTab implements Indentable {
	private String fileName;
	private String tabName;
	private int numberOfRows;
	private int numberOfColumns;
	private Map<ReportCellKey, ReportCell> cells;

	public ReportTab() {
		super();
		this.tabName = "";
		this.cells = new TreeMap<>();
	}

	public ReportTab(final String tabName) {
		super();
		this.tabName = tabName;
		this.cells = new TreeMap<>();
	}

	public ReportTab(String fileName, String tabName) {
		super();
		this.fileName = fileName;
		this.tabName = tabName;
	}

	public ReportTab(String fileName, String tabName, int numberOfRows, int numberOfColumns,
			Map<ReportCellKey, ReportCell> cells) {
		super();
		this.fileName = fileName;
		this.tabName = tabName;
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		this.cells = cells;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_1;
	}

	public Map<ReportCellKey, ReportCell> getCells() {
		return cells;
	}

	public void setCells(Map<ReportCellKey, ReportCell> cells) {
		this.cells = cells;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "ReportTab [fileName=" + fileName + ", tabName=" + tabName
				+ ", numberOfRows=" + numberOfRows + ", numberOfColumns=" + numberOfColumns + ", cells=" + cells + "]";
	}

}
