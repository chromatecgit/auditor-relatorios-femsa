package model;

import java.util.List;

public class VerticalSoviID {
	private String id;
	private List<VerticalSoviRow> verticalSoviRows;
	private int totalSovi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<VerticalSoviRow> getVerticalSoviRows() {
		return verticalSoviRows;
	}

	public void setVerticalSoviRows(List<VerticalSoviRow> verticalSoviRows) {
		this.verticalSoviRows = verticalSoviRows;
	}

	public int getTotalSovi() {
		return totalSovi;
	}

	public void setTotalSovi(int totalSovi) {
		this.totalSovi = totalSovi;
	}

	@Override
	public String toString() {
		return "VerticalSoviID [id=" + id + ", verticalSoviRows=" + verticalSoviRows + ", totalSovi=" + totalSovi + "]";
	}
	
	public VerticalSoviRow findVerticalSoviRowBySKU(final String sku) {
		return this.verticalSoviRows.stream().filter(e -> e.equals(sku)).findFirst().orElse(null);
	}
	
	public VerticalSoviRow findVerticalSoviRowByPOC(final String poc) {
		return this.verticalSoviRows.stream().filter(e -> e.equals(poc)).findFirst().orElse(null);
	}
}
