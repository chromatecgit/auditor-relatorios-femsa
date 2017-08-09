package model;

public class DiffMap {
	private String id;
	private Integer verticalSovi;
	private Integer horizontalSovi;
	private String horizontalOrigin;
	private String verticalOrigin;
	private Double horizontalPreco;
	private Double verticalPreco;
	private String sku;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVerticalSovi() {
		return verticalSovi;
	}

	public void setVerticalSovi(Integer verticalSovi) {
		this.verticalSovi = verticalSovi;
	}

	public Integer getHorizontalSovi() {
		return horizontalSovi;
	}

	public void setHorizontalSovi(Integer horizontalSovi) {
		this.horizontalSovi = horizontalSovi;
	}

	public String getHorizontalOrigin() {
		return horizontalOrigin;
	}

	public void setHorizontalOrigin(String horizontalOrigin) {
		this.horizontalOrigin = horizontalOrigin;
	}

	public String getVerticalOrigin() {
		return verticalOrigin;
	}

	public void setVerticalOrigin(String verticalOrigin) {
		this.verticalOrigin = verticalOrigin;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getHorizontalPreco() {
		return horizontalPreco;
	}

	public void setHorizontalPreco(Double horizontalPreco) {
		this.horizontalPreco = horizontalPreco;
	}

	public Double getVerticalPreco() {
		return verticalPreco;
	}

	public void setVerticalPreco(Double verticalPreco) {
		this.verticalPreco = verticalPreco;
	}

	@Override
	public String toString() {
		return "DiffMap [id=" + id + ", verticalSovi=" + verticalSovi + ", horizontalSovi=" + horizontalSovi
				+ ", horizontalOrigin=" + horizontalOrigin + ", verticalOrigin=" + verticalOrigin + ", horizontalPreco="
				+ horizontalPreco + ", verticalPreco=" + verticalPreco + ", sku=" + sku + "]";
	}

}
