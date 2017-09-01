package model;

import java.util.HashMap;
import java.util.Map;

public class ReportDocument {
	private String fileName;
	private Map<String, ReportTab> tabs;

	public ReportDocument() {
		this.tabs = new HashMap<>();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, ReportTab> getTabs() {
		return tabs;
	}

	public void setTabs(Map<String, ReportTab> tabs) {
		this.tabs = tabs;
	}

	@Override
	public String toString() {
		return "ReportDocument [fileName=" + fileName + ", tabs=" + tabs + "]";
	}

}
