package model;

import java.util.ArrayList;
import java.util.List;

public class HVMapGroupConsolidada {
	private List<HVMapConsolidada> hvMaps;

	public HVMapGroupConsolidada() {
		this.hvMaps = new ArrayList<>();
	}

	public List<HVMapConsolidada> getHvMaps() {
		return hvMaps;
	}

	public void setHvMaps(List<HVMapConsolidada> hvMaps) {
		this.hvMaps = hvMaps;
	}

	@Override
	public String toString() {
		return "HVMapGroupConsolidada [hvMaps=" + hvMaps + "]";
	}
	
	public HVMapConsolidada findHVMapGroupConsolidadaByID(final String id) {
		return this.hvMaps.stream().filter(e-> e.getId().equals(id)).findFirst().orElse(null);
	}
	
	public boolean hasHVMapGroupConsolidadaByID(final String id) {
		return this.hvMaps.stream().filter(e-> e.getId().equals(id)).findFirst().isPresent();
	}


}
