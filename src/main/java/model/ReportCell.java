package model;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportCell implements Indentable {
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
		return this.getHierarchy().getIndentationEntity() + 
				"ReportCell [address=" + address + ", value=" + value + "]";
	}

	@Override
	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_4;
	}

}
