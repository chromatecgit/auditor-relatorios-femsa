package model;

import java.util.ArrayList;
import java.util.List;

public class ReportSymmetryResult {
	private List<String> descriptions;
	
	public ReportSymmetryResult() {
		super();
		this.descriptions = new ArrayList<>();
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	@Override
	public String toString() {
		return "ReportSymmetryResult [descriptions=" + descriptions + "]";
	}

}
