package model;

import java.util.List;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportTab implements Indentable {
	private String name;
	private List<ReportRow> rows;
	private ReportTableDimensions dimensions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ReportRow> getRows() {
		return rows;
	}

	public void setRows(List<ReportRow> rows) {
		this.rows = rows;
	}

	public ReportTableDimensions getDimensions() {
		return dimensions;
	}

	public void setDimensions(ReportTableDimensions dimensions) {
		this.dimensions = dimensions;
	}

	public IndentationEnum getHierarchy() {
		return  IndentationEnum.LEVEL_2;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + 
				"ReportTab [name=" + name + ", rows=" + rows + ", dimensions=" + dimensions + "]";
	}

}
