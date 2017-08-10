package model;

public class ReportCellString extends ReportCell {
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ReportCellString [data=" + data + ", columnIndex=" + columnIndex + ", address=" + address + "]";
	}
	
}
