package model;

import enums.IndentationEnum;

public class ReportKeyColumn {
	private String index;
	private String value;
	private IndentationEnum hierarchy;

	public ReportKeyColumn() {
		this.hierarchy = IndentationEnum.LEVEL_3;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public IndentationEnum getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(IndentationEnum hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.hierarchy.getIndentationEntity() + "ReportKeyColumns [index=" + index + ", value=" + value + "]";
	}

}
