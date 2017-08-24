package model;

import java.util.HashMap;
import java.util.Map;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportTab implements Indentable {

	private String name;
	private Map<ReportCellKey, ReportCell> cells = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_2;
	}

	public Map<ReportCellKey, ReportCell> getCells() {
		return cells;
	}

	public void setCells(Map<ReportCellKey, ReportCell> cells) {
		this.cells = cells;
	}

}
