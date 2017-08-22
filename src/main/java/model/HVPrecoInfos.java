package model;

public class HVPrecoInfos {
	private String id;
	private String sku;
	private String preco;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
