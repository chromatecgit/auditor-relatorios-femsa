package model;

public class ReportCell {
	private int columnIndex;
	private String address;
	private String value;

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ReportCell [columnIndex=" + columnIndex + ", address=" + address + ", value=" + value + "]";
	}

}
