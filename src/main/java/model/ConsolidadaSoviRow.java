package model;

public class ConsolidadaSoviRow {
	private String columnName;
	private int sovi;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getSovi() {
		return sovi;
	}

	public void setSovi(int sovi) {
		this.sovi = sovi;
	}

	@Override
	public String toString() {
		return "\n\t\tConsolidadaSoviRow [columnName=" + columnName + ", sovi=" + sovi + "]";
	}

}
