package model;

import java.util.ArrayList;
import java.util.List;

public class HVPrecoEntry {
	private String id;
	private List<HVPrecoInfos> infos;

	public HVPrecoEntry() {
		super();
		this.infos = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<HVPrecoInfos> getInfos() {
		return infos;
	}

	public void setInfos(List<HVPrecoInfos> infos) {
		this.infos = infos;
	}

	@Override
	public String toString() {
		return "HVPrecoEntry [id=" + id + ", infos=" + infos + "]";
	}

}
