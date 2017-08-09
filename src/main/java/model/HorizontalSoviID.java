package model;

import java.util.List;

public class HorizontalSoviID {
	private String id;
	private List<HorizontalSoviRow> horizontalSoviRows;
	private int totalSovi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTotalSovi() {
		return totalSovi;
	}

	public void setTotalSovi(int totalSovi) {
		this.totalSovi = totalSovi;
	}

	public List<HorizontalSoviRow> getHorizontalSoviRows() {
		return horizontalSoviRows;
	}

	public void setHorizontalSoviRows(List<HorizontalSoviRow> horizontalSoviRows) {
		this.horizontalSoviRows = horizontalSoviRows;
	}

	@Override
	public String toString() {
		return "HorizontalSoviID [id=" + id + ", horizontalSoviRows=" + horizontalSoviRows + ", totalSovi=" + totalSovi
				+ "]";
	}
	
	public HorizontalSoviRow findHorizontalSoviRowBySKU(final String sku) {
		return this.horizontalSoviRows.stream().filter(e -> e.equals(sku)).findFirst().orElse(null);
	}

}
