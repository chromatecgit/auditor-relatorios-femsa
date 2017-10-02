package model;

import enums.IndentationEnum;
import interfaces.Indentable;

public class SoviConsolidadaCell implements Indentable {
	private String address;
	private Integer value;
	private SoviConsolidadaCellAppendix appendix;

	public SoviConsolidadaCell() {
		super();
		this.appendix = new SoviConsolidadaCellAppendix();
	}

	public SoviConsolidadaCell(String address, Integer value) {
		super();
		this.appendix = new SoviConsolidadaCellAppendix();
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

	public SoviConsolidadaCellAppendix getAppendix() {
		return appendix;
	}

	public void setAppendix(SoviConsolidadaCellAppendix appendix) {
		this.appendix = appendix;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "SoviConsolidadaCell [address=" + address + ", value="
				+ value + ", appendix=" + appendix + "]";
	}

	@Override
	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_3;
	}

}
