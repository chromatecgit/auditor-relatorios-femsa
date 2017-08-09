package model;

import java.util.ArrayList;
import java.util.List;

public class HVMapGroup {
	private List<HVMap> hvMaps;

	public HVMapGroup() {
		this.hvMaps = new ArrayList<>();
	}

	public List<HVMap> getHVMaps() {
		return hvMaps;
	}

	public void setHVMaps(List<HVMap> hvMaps) {
		this.hvMaps = hvMaps;
	}

	@Override
	public String toString() {
		return "\n\tHVMapGroup [hvMaps=" + hvMaps + "]";
	}

	public HVMap findHVMapByID(String id) {
		return this.hvMaps.stream().filter(map -> id.equals(map.getId())).findFirst().orElse(null);
	}

	private boolean containsHVMap(String hvMapID) {
		return this.findHVMapByID(hvMapID) != null ? true : false;
	}

	public void addHVMap(HVMap hvMap) {
		if (this.containsHVMap(hvMap.getId())) {
			HVMap existingHVMap = this.findHVMapByID(hvMap.getId());
			existingHVMap.getMapInfos().addAll(hvMap.getMapInfos());
		} else {
			this.hvMaps.add(hvMap);
		}
	}
	
	public HVMapGroup consolidateSoviBySKU() {
		HVMapGroup newGroup = new HVMapGroup();
		for (HVMap hvMap : this.hvMaps) {
			HVMap newHVMap = new HVMap(hvMap.getId(), hvMap.consolidateSoviBySKU());
			newGroup.addHVMap(newHVMap);
		}
		
		return newGroup;
	}
	
	public HVMapGroup consolidatePrecoBySKU() {
		HVMapGroup newGroup = new HVMapGroup();
		for (HVMap hvMap : this.hvMaps) {
			HVMap newHVMap = new HVMap(hvMap.getId(), hvMap.consolidatePrecoBySKU());
			newGroup.addHVMap(newHVMap);
		}
		
		return newGroup;
	}
	
}
