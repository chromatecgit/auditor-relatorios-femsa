package model;

public class ReportCellMultipleKey implements Comparable<ReportCellMultipleKey>{
	private String concat;
	private String sku;
	private String poc;
	private String rule;

	public ReportCellMultipleKey() {
		super();
	}

	public ReportCellMultipleKey(String concat, String sku, String poc, String rule) {
		super();
		this.concat = concat;
		this.sku = sku;
		this.poc = poc;
		this.rule = rule;
	}

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

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Override
	public String toString() {
		return "ReportCellMultipleKey [concat=" + concat + ", sku=" + sku + ", poc=" + poc + ", rule=" + rule + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concat == null) ? 0 : concat.hashCode());
		result = prime * result + ((poc == null) ? 0 : poc.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportCellMultipleKey other = (ReportCellMultipleKey) obj;
		if (concat == null) {
			if (other.concat != null)
				return false;
		} else if (!concat.equals(other.concat))
			return false;
		if (poc == null) {
			if (other.poc != null)
				return false;
		} else if (!poc.equals(other.poc))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.equals(other.sku))
			return false;
		return true;
	}

	@Override
	public int compareTo(ReportCellMultipleKey o) {
		String result1 = this.getConcat() + this.getSku() + this.getPoc() + this.getRule();
		String result2 = o.getConcat() + o.getSku() + o.getPoc() + o.getRule();
		return result1.compareTo(result2);
	}

}
