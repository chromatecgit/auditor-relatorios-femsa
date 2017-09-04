package model;

import java.util.ArrayList;
import java.util.List;

public class ReportSymmetryResult {
	private String key;
	private List<String> descriptions;

	public ReportSymmetryResult() {
		super();
		this.descriptions = new ArrayList<>();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	@Override
	public String toString() {
		return "ReportSymmetryResult [key=" + key + ", descriptions=" + descriptions + "]";
	}

}
