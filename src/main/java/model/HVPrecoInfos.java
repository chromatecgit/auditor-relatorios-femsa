package model;

public class HVPrecoInfos {
	private String sku;
	private String preco;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	@Override
	public String toString() {
		return "HVPrecoInfos [sku=" + sku + ", preco=" + preco + "]";
	}

}
