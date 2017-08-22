package model;

import java.util.ArrayList;
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
	
	public List<HVPrecoInfos> parseReportRowPrecoInfos(List<ReportKeyColumn> keyColumns, boolean excludeZero) {
		List<HVPrecoInfos> collection = keyColumns.stream().map(c -> {
			HVPrecoInfos precoInfos = new HVPrecoInfos();
			if (c.getValue().contains("PRECO")) {
				ReportCell cell = this.findCellByColumn(c.getIndex());
				if (!cell.getValue().equals("0")) {
					precoInfos.setPreco(cell.getValue());
					precoInfos.setSku(c.getValue());
					precoInfos.setId(this.findCellByColumn("A").getValue());
					return precoInfos;
				}
			} else {
				return null;
			}
			return null;
		}).collect(Collectors.toList());
		while(collection.remove(null));
		return collection;
	}
	
	public List<HVPrecoInfos> parseReportRowPrecoInfosVertical(List<ReportKeyColumn> keyColumns, boolean excludeZero) {
		List<ReportKeyColumn> collected = keyColumns.stream().map(c -> {
			if (c.getValue().equals("PRODUTO") || c.getValue().equals("PRECO") || c.getValue().equals("CONCAT")) {
				return c;
			}
			return null;
		}).collect(Collectors.toList());
		
		while(collected.remove(null));

		
		List<HVPrecoInfos> list = new ArrayList<>();
		HVPrecoInfos infos = new HVPrecoInfos();
		for (ReportKeyColumn k : collected) {
			ReportCell cell = this.findCellByColumn(k.getIndex());
			if (k.getValue().contains("PRODUTO")) {
				infos.setSku(cell.getValue());
			}
			
			if (k.getValue().contains("PRECO")) {
				infos.setPreco(cell.getValue());
			}
			
			if (k.getValue().contains("CONCAT")) {
				infos.setId(cell.getValue());
			}
		}
		list.add(infos);
		return list;
	}
}
