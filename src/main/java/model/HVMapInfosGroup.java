package model;

import java.util.ArrayList;
import java.util.List;

public class HVMapInfosGroup {
	private String filter;
	private int totalSovi;
	private List<HVMapInfos> hvMapInfosList;

	public HVMapInfosGroup() {
		super();
		this.hvMapInfosList = new ArrayList<>();
	}

	public int getTotalSovi() {
		return totalSovi;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public List<HVMapInfos> getHvMapInfosList() {
		return hvMapInfosList;
	}

	public void setHvMapInfosList(List<HVMapInfos> hvMapInfosList) {
		this.hvMapInfosList = hvMapInfosList;
	}

	@Override
	public String toString() {
		return "\n\t\tHVMapInfosGroup [filter=" + filter + ", totalSovi=" + totalSovi + ", hvMapInfosList=" + hvMapInfosList
				+ "]";
	}
	
	public void calculateTotalSovi() {
		for (HVMapInfos infos : hvMapInfosList) {
			this.totalSovi += infos.getSovi();
		}
	}

}
