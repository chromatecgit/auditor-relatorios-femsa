package model;

public class ReportColumnHeader {
	private int index;
	private String columnName;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public String toString() {
		return "ReportColumnHeader [index=" + index + ", columnName=" + columnName + "]";
	}

}
