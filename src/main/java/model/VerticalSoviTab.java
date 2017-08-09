package model;

import java.util.List;

public class VerticalSoviTab {
	private String tabName;
	private List<VerticalSoviID> verticalSoviIDs;

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List<VerticalSoviID> getVerticalSoviIDs() {
		return verticalSoviIDs;
	}

	public void setVerticalSoviIDs(List<VerticalSoviID> verticalSoviIDs) {
		this.verticalSoviIDs = verticalSoviIDs;
	}

	@Override
	public String toString() {
		return "VerticalSoviTab [tabName=" + tabName + ", verticalSoviIDs=" + verticalSoviIDs + "]";
	}
	
	public VerticalSoviID findVerticalSoviIDbyID(final String id) {
		return verticalSoviIDs.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
	}
	
}
