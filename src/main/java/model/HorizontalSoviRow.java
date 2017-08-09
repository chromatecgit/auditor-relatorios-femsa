package model;

public class HorizontalSoviRow {
	private String sku;
	private int sovi;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getSovi() {
		return sovi;
	}

	public void setSovi(int sovi) {
		this.sovi = sovi;
	}

	@Override
	public String toString() {
		return "HorizontalSoviRow [sku=" + sku + ", sovi=" + sovi + "]";
	}

}
