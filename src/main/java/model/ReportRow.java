package model;

import java.util.List;

import enums.IndentationEnum;
import exceptions.WarningException;
import interfaces.Indentable;
import utils.CallCounter;

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
		return this.cells.stream().filter(c -> c.getColumnIndex().equals(key)).findFirst().orElse(new ReportCell("-1", "-1"));
	}
	
	public HVPrecoEntry parseReportRowPrecoInfos(List<ReportKeyColumn> keyColumns, boolean excludeZero) {
		ReportCell idCell = this.findCellByColumn("A");
		CallCounter.parseReportRowPrecoInfos++;
		HVPrecoEntry entry = new HVPrecoEntry();
		entry.setId(idCell.getValue());
		for (ReportKeyColumn keyColumn : keyColumns) {
			CallCounter.parseReportRowPrecoInfos_keycolumn++;
			ReportCell cell = this.findCellByColumn(keyColumn.getIndex());
			if (excludeZero) {
				if (!cell.getValue().equals("0")) {
					HVPrecoInfos infos = new HVPrecoInfos();
					infos.setPreco(cell.getValue());
					infos.setSku(keyColumn.getValue());
					entry.getInfos().add(infos);
				}
			} else {
				HVPrecoInfos infos = new HVPrecoInfos();
				infos.setPreco(cell.getValue());
				infos.setSku(keyColumn.getValue());
				entry.getInfos().add(infos);
			}
		}
		
		return entry;
	}
	
	public String getRowID() {
		return this.cells.stream().filter( c -> c.getColumnIndex().equals("A")).map( c -> c.getValue()).findFirst().orElse("-1");
	}
}
