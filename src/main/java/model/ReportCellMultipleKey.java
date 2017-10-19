package model;

public class ReportCellMultipleKey extends ReportCellKey {
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
		if (!(obj instanceof ReportCellMultipleKey))
			return false;
		ReportCellMultipleKey other = (ReportCellMultipleKey) obj;
		if (thirdKey == null) {
			if (other.thirdKey != null)
				return false;
		} else if (!thirdKey.equals(other.thirdKey))
			return false;
		return true;
	}

	@Override
	public int compareTo(final ReportCellKey otherKey) {
		ReportCellMultipleKey multipleKey = (ReportCellMultipleKey) otherKey;
		String result1 = this.getFirstKey() + this.getSecondKey() + this.thirdKey;
		String result2 = multipleKey.getFirstKey() + multipleKey.getSecondKey() + multipleKey.getThirdKey();

		return result1.compareTo(result2);
	}
}
