package model;

public class ReportTableDimensions {
	private int columns;
	private int rows;
	
	public ReportTableDimensions() {
		super();
	}

	public ReportTableDimensions(int columns, int rows) {
		super();
		this.columns = columns;
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "ReportTableDimensions [columns=" + columns + ", rows=" + rows + "]";
	}

}
