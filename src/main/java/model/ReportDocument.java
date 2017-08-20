package model;

import java.util.List;
import java.util.stream.Collectors;

import enums.IndentationEnum;
import exceptions.WarningException;
import interfaces.Indentable;

public class ReportDocument implements Indentable {
	private boolean isNew;
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

	public IndentationEnum getHierarchy() {
		return IndentationEnum.LEVEL_1;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public String toString() {
		return this.getHierarchy().getIndentationEntity() + "ReportDocument [fileName=" + fileName + ", tabs=" + tabs
				+ "]";
	}

	public List<NumeroLinhasResult> parseToNumeroLinhasResult() {
		return this.tabs.stream().map(t -> {
			NumeroLinhasResult result = new NumeroLinhasResult();
			result.setRowQnty(t.getDimensions().getColumns());
			result.setTabName(t.getName());
			result.setFileName(this.getFileName());
			return result;
		}).collect(Collectors.toList());
	}
	
	public ReportTab findEquivalentTab(final String tabName) throws WarningException {
		return this.tabs.stream().filter(t -> t.getName().equals(tabName)).findFirst().orElseThrow(WarningException::new);
	}
}
