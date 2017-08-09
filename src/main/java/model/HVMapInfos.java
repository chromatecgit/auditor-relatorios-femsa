package model;

public class HVMapInfos implements Comparable<HVMapInfos> {
	private Integer sovi;
	private String sku;
	private String tabName;
	private String poc;
	private Double preco;

	public Integer getSovi() {
		return sovi;
	}

	public void setSovi(Integer sovi) {
		this.sovi = sovi;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	@Override
	public String toString() {
		return "\n\t\t\tHVMapInfos [sovi=" + sovi + ", sku=" + sku + ", tabName=" + tabName + ", poc=" + poc + ", preco="
				+ preco + "]";
	}

	@Override
	public int compareTo(HVMapInfos o) {
		return this.sku.compareTo(o.getSku());
	}

}
