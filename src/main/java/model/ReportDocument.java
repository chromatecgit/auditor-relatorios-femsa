package model;

import java.util.List;

public class ReportDocument {
	private String fileName;
	private List<ReportTab> tabs;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<ReportTab> getTabs() {
		return tabs;
	}

	public void setTabs(List<ReportTab> tabs) {
		this.tabs = tabs;
	}

	@Override
	public String toString() {
		return "ReportDocument [fileName=" + fileName + ", tabs=" + tabs + "]";
	}

}
