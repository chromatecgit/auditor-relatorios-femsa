package model;

import java.util.List;

public class VerticalPrecoTab {
	private String tabName;
	private List<VerticalPrecoID> verticalPrecoIDs;

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List<VerticalPrecoID> getVerticalPrecoIDs() {
		return verticalPrecoIDs;
	}

	public void setVerticalPrecoIDs(List<VerticalPrecoID> verticalPrecoIDs) {
		this.verticalPrecoIDs = verticalPrecoIDs;
	}

	@Override
	public String toString() {
		return "VerticalPrecoTab [tabName=" + tabName + ", verticalPrecoIDs=" + verticalPrecoIDs + "]";
	}

	public VerticalPrecoID findVerticalSoviIDbyID(final String id) {
		return verticalPrecoIDs.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
	}

}
