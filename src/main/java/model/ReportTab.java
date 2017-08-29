package model;

import java.util.Map;
import java.util.TreeMap;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportTab implements Indentable {
	protected String fileName;
	protected String tabName;
	protected int numberOfRows;
	protected int numberOfColumns;
	private Map<ReportCellKey, ReportCell> cells;

	public ReportTab() {
		super();
		this.tabName = "";
		this.cells = new TreeMap<>();
	}

	public ReportTab(final String name) {
		super();
		this.tabName = name;
		this.cells = new TreeMap<>();
	}

	public ReportTab(final String tabName, final Map<ReportCellKey, ReportCell> cells) {
		super();
		this.tabName = tabName;
		this.cells = new TreeMap<>(cells);
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

	public Map<ReportCellKey, ReportCell> getCells() {
		return cells;
	}

	public void setCells(Map<ReportCellKey, ReportCell> cells) {
		this.cells = cells;
	}

	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_1;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "ReportTab [fileName=" + fileName + ", tabName=" + tabName
				+ ", numberOfRows=" + numberOfRows + ", numberOfColumns=" + numberOfColumns + ", cells=" + cells + "]";
	}

}
