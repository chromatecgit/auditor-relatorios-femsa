package model;

import enums.IndentationEnum;

public class ReportCellKey implements Comparable<ReportCellKey> {

	private String concat;
	private String columnName;

	public String getConcat() {
		return concat;
	}

	public void setConcat(String concat) {
		this.concat = concat;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_2;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "ReportCellKey [concat=" + concat + ", columnName=" + columnName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concat == null) ? 0 : concat.hashCode());
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
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
		if (concat == null) {
			if (other.concat != null)
				return false;
		} else if (!concat.equals(other.concat))
			return false;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		return true;
	}

	@Override
	public int compareTo(ReportCellKey otherKey) {
		String result1 = this.getConcat() + this.getColumnName();
		String result2 = otherKey.getConcat() + otherKey.getColumnName();
		
		return result1.compareTo(result2);
	}

}
