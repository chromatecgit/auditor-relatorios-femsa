package model;

public class ReportCellTripleKey extends ReportCellKey {
	private String thirdKey;

	public String getThirdKey() {
		return thirdKey;
	}

	public void setThirdKey(String thirdKey) {
		this.thirdKey = thirdKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((thirdKey == null) ? 0 : thirdKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ReportCellTripleKey))
			return false;
		ReportCellTripleKey other = (ReportCellTripleKey) obj;
		if (thirdKey == null) {
			if (other.thirdKey != null)
				return false;
		} else if (!thirdKey.equals(other.thirdKey))
			return false;
		return true;
	}

	@Override
	public int compareTo(final ReportCellKey otherKey) {
		ReportCellTripleKey multipleKey = (ReportCellTripleKey) otherKey;
		String result1 = this.getFirstKey() + this.getSecondKey() + this.thirdKey;
		String result2 = multipleKey.getFirstKey() + multipleKey.getSecondKey() + multipleKey.getThirdKey();

		return result1.compareTo(result2);
	}

	@Override
	public String toString() {
		return "KEY [" + firstKey + ", " + secondKey + ", " + thirdKey
				+ "]";
	}
	
	
}
