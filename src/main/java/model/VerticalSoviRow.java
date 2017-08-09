package model;

public class VerticalSoviRow {
	private String poc;
	private String sku;
	private int sovi;

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

	public int getSovi() {
		return sovi;
	}

	public void setSovi(int sovi) {
		this.sovi = sovi;
	}

	@Override
	public String toString() {
		return "VerticalSoviRow [poc=" + poc + ", sku=" + sku + ", sovi=" + sovi + "]";
	}

}
