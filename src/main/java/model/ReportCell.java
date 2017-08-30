package model;

import java.util.HashSet;
import java.util.Set;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportCell implements Indentable {
	private String address;
	private String value;
	private Set<String> pocs;

	public ReportCell() {
		super();
		this.pocs = new HashSet<>();
	}

	public ReportCell(String address, String value) {
		super();
		this.address = address;
		this.value = value;
		this.pocs = new HashSet<>();
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

	public Set<String> getPocs() {
		return pocs;
	}

	public void setPocs(Set<String> pocs) {
		this.pocs = pocs;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "ReportCell [address=" + address + ", value=" + value
				+ ", pocs=" + pocs + "]";
	}

	@Override
	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_3;
	}

}
