package model;

import java.util.List;
import java.util.stream.Collectors;

import enums.IndentationEnum;
import exceptions.WarningException;
import interfaces.Indentable;

public class ReportRow implements Indentable {
	private int index;
	private List<ReportCell> cells;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<ReportCell> getCells() {
		return cells;
	}

	public void setCells(List<ReportCell> cells) {
		this.cells = cells;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + 
				"ReportRow [index=" + index + ", cells=" + cells + "]";
	}

	public IndentationEnum getHierarchy() {
		return  IndentationEnum.LEVEL_3;
	}
	
	public ReportRow findEquivalentRowByColumnIndex(final List<ReportRow> anotherRows, final String columnID) throws WarningException {
		ReportCell keyCell = this.cells.stream().filter(
				c -> c.getColumnIndex().equals(columnID)).findFirst().orElse(null);
		for (ReportRow row : anotherRows) {
			for (ReportCell cell : row.getCells()) {
				if (cell.getValue().equals(keyCell.getValue())) {
					return row;
				}
			}
		}
		
		return null;
	}
	
	public ReportRow findEquivalentRowByColumnIndex(final List<ReportRow> anotherRows, final String[] columns) throws WarningException {
		ReportCell keyCell = this.cells.stream().filter(
				c -> c.getColumnIndex().equals(columns[0])).findFirst().orElse(null);
		ReportCell keyCell2 = this.cells.stream().filter(
				c -> c.getColumnIndex().equals(columns[1])).findFirst().orElse(null);
		
		for (ReportRow row : anotherRows) {
			ReportCell cell1 = row.findCellByColumn(keyCell.getColumnIndex());
			ReportCell cell2 = row.findCellByColumn(keyCell2.getColumnIndex());
			if (cell1.getValue().equals(keyCell.getValue()) && cell2.getValue().equals(keyCell2.getValue())) {
				return row;
			}
		}
		
		return null;
	}
	
	public ReportCell findCellByColumn(final String key) {
		return this.cells.stream().filter(c -> c.getColumnIndex().equals(key)).findFirst().orElse(null);
	}
	
	public HVPrecoInfos parseReportRowPrecoInfos(List<ReportKeyColumn> keyColumns) {
		
		keyColumns.stream().map(c -> {
			if (c.getValue().contains("PRECO")) {
				HVPrecoInfos precoInfos = new HVPrecoInfos();
				ReportCell cell = this.findCellByColumn(c.getIndex());
				precoInfos.setPreco(cell.getValue());
				precoInfos.setSku(c.getValue());
				return precoInfos;
			}
		}).collect(Collectors.toList());
	}
}
