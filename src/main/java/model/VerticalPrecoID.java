package model;

import java.util.ArrayList;
import java.util.List;

public class VerticalPrecoID {
	private String id;
	private List<VerticalPrecoRow> verticalPrecoRows;
	private double totalPreco;
	
	public VerticalPrecoID() {
		super();
		this.verticalPrecoRows = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<VerticalPrecoRow> getVerticalPrecoRows() {
		return verticalPrecoRows;
	}

	public void setVerticalPrecoRows(List<VerticalPrecoRow> verticalPrecoRows) {
		this.verticalPrecoRows = verticalPrecoRows;
	}

	public double getTotalPreco() {
		return totalPreco;
	}

	public void setTotalPreco(double totalPreco) {
		this.totalPreco = totalPreco;
	}

	@Override
	public String toString() {
		return "VerticalPrecoID [id=" + id + ", verticalPrecoRows=" + verticalPrecoRows + ", totalPreco=" + totalPreco
				+ "]";
	}

	public VerticalPrecoRow findVerticalPrecoRowBySKU(final String sku) {
		return this.verticalPrecoRows.stream().filter(e -> e.equals(sku)).findFirst().orElse(null);
	}

	public VerticalPrecoRow findVerticalPrecoRowByPOC(final String poc) {
		return this.verticalPrecoRows.stream().filter(e -> e.equals(poc)).findFirst().orElse(null);
	}
}
