package model;

import java.util.HashMap;
import java.util.Map;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportCell implements Indentable {
	private String address;
	private String value;
	private Map<String, Integer> pocInfos;

	public ReportCell() {
		super();
		this.pocInfos = new HashMap<>();
	}

	public ReportCell(String address, String value) {
		super();
		this.address = address;
		this.value = value;
		this.pocInfos = new HashMap<>();
	}

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

	public Map<String, Integer> getPocInfos() {
		return pocInfos;
	}

	public void setPocInfos(Map<String, Integer> pocInfos) {
		this.pocInfos = pocInfos;
	}

	@Override
	public String toString() {
		return "ReportCell [address=" + address + ", value=" + value + ", pocInfos=" + pocInfos + "]";
	}

	@Override
	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_3;
	}

}
