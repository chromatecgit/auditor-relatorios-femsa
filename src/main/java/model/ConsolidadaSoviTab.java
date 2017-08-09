package model;

import java.util.List;

public class ConsolidadaSoviTab {
	private String tabName;
	private List<ConsolidadaSoviID> consolidadaSoviIDs;

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List<ConsolidadaSoviID> getConsolidadaSoviIDs() {
		return consolidadaSoviIDs;
	}

	public void setConsolidadaSoviIDs(List<ConsolidadaSoviID> consolidadaSoviIDs) {
		this.consolidadaSoviIDs = consolidadaSoviIDs;
	}

	@Override
	public String toString() {
		return "ConsolidadaSoviTab [tabName=" + tabName + ", consolidadaSoviIDs=" + consolidadaSoviIDs + "]";
	}

	public ConsolidadaSoviID findHorizontalSoviIDbyID(final String id) {
		return consolidadaSoviIDs.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
	}

}
