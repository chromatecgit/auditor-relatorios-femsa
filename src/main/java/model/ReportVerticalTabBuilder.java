package model;

import java.util.LinkedList;
import java.util.List;

import interfaces.ReportTabBuilder;

public class ReportVerticalTabBuilder implements ReportTabBuilder {
	
	
	private ReportTab tab;
	private List<ReportCell> tableHeaders;
	private String tag;
	
	public ReportVerticalTabBuilder(final String tag) {
		this.tab = new ReportTab();
		this.tableHeaders = new LinkedList<>();
		this.tag = tag;
	}

	@Override
	public ReportTab build() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCell(ReportCell cell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAndReset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNumberOfRows(int rows) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNumberOfColumns(int columns) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDocumentName(String fileName) {
		// TODO Auto-generated method stub
		
	}

}
