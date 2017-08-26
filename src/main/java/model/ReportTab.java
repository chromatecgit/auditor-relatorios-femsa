package model;

import java.util.Map;
import java.util.TreeMap;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportTab implements Indentable {
	private String fileName;
	private String originalOrientation;
	private String tabName;
	private int numberOfRows;
	private int numberOfColumns;
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

	public Map<ReportCellKey, ReportCell> getCells() {
		return cells;
	}

	public void setCells(Map<ReportCellKey, ReportCell> cells) {
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

	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_2;
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

	public String getOriginalOrientation() {
		return originalOrientation;
	}

	public void setOriginalOrientation(String originalOrientation) {
		this.originalOrientation = originalOrientation;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "ReportTab [fileName=" + fileName + ", originalOrientation=" + originalOrientation + ", tabName="
				+ tabName + ", numberOfRows=" + numberOfRows + ", numberOfColumns=" + numberOfColumns + ", cells="
				+ cells + "]";
	}

}
