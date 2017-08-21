package model;

import java.util.List;

public class HVPrecoMap {
	private String id;
	private List<HVPrecoInfos> infos;

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
		return "HVPrecoMap [id=" + id + ", infos=" + infos + "]";
	}

}
