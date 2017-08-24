package utils;

import java.util.ArrayList;

import model.ReportDocument;
import model.ReportTab;

public class ReportDocumentBuilder {
	private ReportDocument document;
	private int lastVisitedLine;

	public ReportDocumentBuilder() {
		this.document = new ReportDocument();
		//this.document.setTabs(new ArrayList<ReportTab>());
	}
	
	public void setNewFlagTo(boolean isNew) {
		//this.document.setNew(isNew);
	}

	public void addDocumentName(final String name) {
		this.document.setFileName(name);
	}

	public void addReportTab(final ReportTab tab, final String tabName) {
		ReportTab newTab = new ReportTab();
		newTab = tab;
		newTab.setName(tabName);
		//this.document.getTabs().add(newTab);
	}
	
	public ReportDocument build() {
		return this.document;
	}

	public int getLastVisitedLine() {
		return lastVisitedLine;
	}

	public void setLastVisitedLine(int lastVisitedLine) {
		this.lastVisitedLine = lastVisitedLine;
	}

}
