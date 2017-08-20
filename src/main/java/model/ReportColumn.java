package model;

import java.util.List;

public class ReportColumn {
	private String index;
	private List<ReportCell> columnCells;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<ReportCell> getColumnCells() {
		return columnCells;
	}

	public void setColumnCells(List<ReportCell> columnCells) {
		this.columnCells = columnCells;
	}

	@Override
	public String toString() {
		return "ReportColumn [index=" + index + ", columnCells=" + columnCells + "]";
	}

}
