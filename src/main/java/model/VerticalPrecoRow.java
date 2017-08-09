package model;

public class VerticalPrecoRow {
	private String poc;
	private String sku;
	private double preco;

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

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
		return "VerticalPrecoRow [poc=" + poc + ", sku=" + sku + ", preco=" + preco + "]";
	}

}
