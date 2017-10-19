package model;

import enums.IndentationEnum;

public class ReportCellKey implements Comparable<ReportCellKey> {

	protected String firstKey;
	protected String secondKey;

	public ReportCellKey() {
		super();
	}

	public ReportCellKey(String firstKey, String secondKey) {
		super();
		this.firstKey = firstKey;
		this.secondKey = secondKey;
	}

	public String getFirstKey() {
		return firstKey;
	}

	public void setFirstKey(String firstKey) {
		this.firstKey = firstKey;
	}

	public String getSecondKey() {
		return secondKey;
	}

	public void setSecondKey(String secondKey) {
		this.secondKey = secondKey;
	}

	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstKey == null) ? 0 : firstKey.hashCode());
		result = prime * result + ((secondKey == null) ? 0 : secondKey.hashCode());
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
		ReportCellKey other = (ReportCellKey) obj;
		if (firstKey == null) {
			if (other.firstKey != null)
				return false;
		} else if (!firstKey.equals(other.firstKey))
			return false;
		if (secondKey == null) {
			if (other.secondKey != null)
				return false;
		} else if (!secondKey.equals(other.secondKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "KEY [" + firstKey + ", " + secondKey + "]";
	}

	@Override
	public int compareTo(final ReportCellKey otherKey) {
		String result1 = this.getFirstKey() + this.getSecondKey();
		String result2 = otherKey.getFirstKey() + otherKey.getSecondKey();

		return result1.compareTo(result2);
	}

}
