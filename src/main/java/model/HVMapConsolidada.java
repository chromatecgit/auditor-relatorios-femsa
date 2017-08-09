package model;

import java.util.ArrayList;
import java.util.List;

public class HVMapConsolidada {
	private String id;
	private List<HVMapInfosGroup> infosGroup;

	public HVMapConsolidada() {
		super();
		this.infosGroup = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<HVMapInfosGroup> getInfosGroup() {
		return infosGroup;
	}

	public void setInfosGroup(List<HVMapInfosGroup> infosGroup) {
		this.infosGroup = infosGroup;
	}

	@Override
	public String toString() {
		return "\n\tHVMapConsolidada [id=" + id + ", infosGroup=" + infosGroup + "]";
	}
	
	public HVMapInfosGroup findHVMapInfosGroupByFilterName(String columnName) {
		return this.infosGroup.stream().filter(infos -> infos.getFilter().equals(columnName)).findFirst().orElse(null);
	}
}
