package model;

import java.util.ArrayList;
import java.util.List;

import enums.IndentationEnum;
import interfaces.Indentable;

public class ReportTab implements Indentable {
	private String name;
	private List<ReportKeyColumn> tableColumns;
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

	public List<ReportKeyColumn> getTableColumns() {
		return tableColumns;
	}

	public void setTableColumns(List<ReportKeyColumn> tableColumns) {
		this.tableColumns = tableColumns;
	}

	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_2;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "ReportTab [name=" + name + ", tableColumns=" + tableColumns
				+ ", rows=" + rows + ", dimensions=" + dimensions + "]";
	}

	public ReportRow getFirstRow() {
		return rows.stream().filter(e -> e.getIndex() == 1).findFirst().orElse(null);
	}

	public ReportColumn getFirstColumn() {
		ReportColumn column = new ReportColumn();
		List<ReportCell> columnCells = new ArrayList<>();
		for (ReportRow row : this.rows) {
			columnCells.add(
					row.getCells().stream().filter(c -> c.getAddress().matches("A[0-9]+")).findFirst().orElse(null));
		}

		column.setColumnCells(columnCells);
		return column;
	}
	
	public ReportKeyColumn getKeyColumnByName(String name) {
		return this.tableColumns.stream().filter(c -> c.getValue().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public ReportKeyColumn getKeyColumnByNameLike(String name) {
		return this.tableColumns.stream().filter(c -> c.getValue().contains(name)).findFirst().orElse(null);
	}
	
}
