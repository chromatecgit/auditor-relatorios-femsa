package model;

import java.util.List;

public class ReportRow {
	private String index;
	private List<ReportCell> cells;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<ReportCell> getCells() {
		return cells;
	}

	public void setCells(List<ReportCell> cells) {
		this.cells = cells;
	}

}
