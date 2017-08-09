package model;

public class HorizontalPrecoRow {
	private String sku;
	private double preco;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	@Override
	public String toString() {
		return "HorizontalPrecoRow [sku=" + sku + ", preco=" + preco + "]";
	}

}
