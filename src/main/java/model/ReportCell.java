package model;

public class ReportCell {
	private String address;
	private String value;

	public String getColumnIndex() {
		String s = address;
		return s.replaceAll("\\d", "");
	}

	public int getLineIndex() {
		String s = address;
		return Integer.parseInt(s.replaceAll("\\D", ""));
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
		return "ReportCell [address=" + address + ", value=" + value + "]";
	}

}
