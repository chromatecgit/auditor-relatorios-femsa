package model;

import java.util.List;

public class HorizontalSoviTab {
	private String tabName;
	private List<HorizontalSoviID> horizontalSoviIDs;

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List<HorizontalSoviID> getIds() {
		return horizontalSoviIDs;
	}

	public void setIds(List<HorizontalSoviID> ids) {
		this.horizontalSoviIDs = ids;
	}

	@Override
	public String toString() {
		return "HorizontalSoviTab [tabName=" + tabName + ", ids=" + horizontalSoviIDs + "]";
	}
	
	public HorizontalSoviID findHorizontalSoviIDbyID(final String id) {
		return horizontalSoviIDs.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
	}

}
