package model;

public class ReportCellDouble extends ReportCell {
	private double data;

	public double getData() {
		return data;
	}

	public void setData(double data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ReportCellDouble [data=" + data + ", columnIndex=" + columnIndex + ", address=" + address + "]";
	}

}
