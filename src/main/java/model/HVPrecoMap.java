package model;

import java.util.ArrayList;
import java.util.List;

public class HVPrecoMap {
	private List<HVPrecoEntry> entries;
	
	public HVPrecoMap() {
		this.entries = new ArrayList<>();
	}

	public List<HVPrecoEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<HVPrecoEntry> entries) {
		this.entries = entries;
	}

}
