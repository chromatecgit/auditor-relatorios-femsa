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

}
