package model;

import java.util.List;

public class ConsolidadaSoviID {
	private String id;
	private List<ConsolidadaSoviRow> consolidadaSoviRows;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ConsolidadaSoviRow> getConsolidadaSoviRows() {
		return consolidadaSoviRows;
	}

	public void setHorizontalSoviRows(List<ConsolidadaSoviRow> consolidadaSoviRows) {
		this.consolidadaSoviRows = consolidadaSoviRows;
	}

	public void setConsolidadaSoviRows(List<ConsolidadaSoviRow> consolidadaSoviRows) {
		this.consolidadaSoviRows = consolidadaSoviRows;
	}

	@Override
	public String toString() {
		return "\n\tConsolidadaSoviID [id=" + id + ", consolidadaSoviRows=" + consolidadaSoviRows + "]";
	}

}
