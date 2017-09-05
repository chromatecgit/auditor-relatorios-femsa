package model;

import enums.IndentationEnum;
import interfaces.Indentable;

public class SoviConsolidadaCell implements Indentable {
	private String address;
	private Integer value;

	public SoviConsolidadaCell() {
		super();
	}

	public SoviConsolidadaCell(String address, Integer value) {
		super();
		this.address = address;
		this.value = value;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SoviConsolidadaCell [address=" + address + ", value="
				+ value + "]";
	}

	@Override
	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_3;
	}

}
