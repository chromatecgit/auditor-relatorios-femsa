package model;

import java.util.List;

public class HorizontalPrecoID {
	private String id;
	private List<HorizontalPrecoRow> horizontalPrecoRows;
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

	public List<HorizontalPrecoRow> getHorizontalPrecoRows() {
		return horizontalPrecoRows;
	}

	public void setHorizontalPrecoRows(List<HorizontalPrecoRow> horizontalPrecoRows) {
		this.horizontalPrecoRows = horizontalPrecoRows;
	}

	@Override
	public String toString() {
		return "HorizontalPrecoID [id=" + id + ", horizontalPrecoRows=" + horizontalPrecoRows + ", totalSovi="
				+ totalSovi + "]";
	}

}
