package model;

public class LogHelper {
	private String concat;
	private String sku;

	public String getConcat() {
		return concat;
	}

	public void setConcat(String concat) {
		this.concat = concat;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Override
	public String toString() {
		return "LogHelper [concat=" + concat + ", sku=" + sku + "]" + "\n";
	}

}
