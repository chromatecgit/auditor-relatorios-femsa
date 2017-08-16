package model;

public class NumeroLinhasResult {
	private int rowQnty;
	private String tabName;
	private String fileName;

	public int getRowQnty() {
		return rowQnty;
	}

	public void setRowQnty(int rowQnty) {
		this.rowQnty = rowQnty;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "\nNumeroLinhasResult [rowQnty=" + rowQnty + ", tabName=" + tabName + ", fileName=" + fileName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rowQnty;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		NumeroLinhasResult other = (NumeroLinhasResult) obj;
		if (rowQnty == other.rowQnty)
			return true;
		return false;
	}

}
