package model;

public class ReportCellInteger extends ReportCell {
	private int data;

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ReportCellInteger [data=" + data + ", columnIndex=" + columnIndex + ", address=" + address + "]";
	}

}
