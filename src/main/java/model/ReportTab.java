package model;

import java.util.Map;
import java.util.TreeMap;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportTab implements Indentable {
	private String name;
	private Map<ReportCellKey, ReportCell> cells;

	public ReportTab() {
		super();
		this.cells = new TreeMap<>();
	}

	public ReportTab(String name) {
		super();
		this.name = name;
		this.cells = new TreeMap<>();
	}

	public ReportTab(String name, Map<ReportCellKey, ReportCell> cells) {
		super();
		this.name = name;
		this.cells = new TreeMap<>(cells);
	}

	@Override
	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<ReportCellKey, ReportCell> getCells() {
		return cells;
	}

	public void setCells(Map<ReportCellKey, ReportCell> cells) {
		this.cells = cells;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "ReportTab [name=" + name + ", cells=" + cells + "]";
	}
	
	

}
