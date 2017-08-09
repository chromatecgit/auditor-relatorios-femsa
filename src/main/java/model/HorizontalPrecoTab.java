package model;

import java.util.List;

public class HorizontalPrecoTab {
	private String tabName;
	private List<HorizontalPrecoID> horizontalPrecoIDs;

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List<HorizontalPrecoID> getHorizontalPrecoIDs() {
		return horizontalPrecoIDs;
	}

	public void setHorizontalPrecoIDs(List<HorizontalPrecoID> horizontalPrecoIDs) {
		this.horizontalPrecoIDs = horizontalPrecoIDs;
	}

	@Override
	public String toString() {
		return "HorizontalPrecoTab [tabName=" + tabName + ", horizontalPrecoIDs=" + horizontalPrecoIDs + "]";
	}

	public HorizontalPrecoID findHorizontalSoviIDbyID(final String id) {
		return horizontalPrecoIDs.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
	}

}
