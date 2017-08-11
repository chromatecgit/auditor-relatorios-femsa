package model;

import java.util.ArrayList;
import java.util.List;

public class ValoresIguaisResult {

	private List<ValoresIguaisGroup> groups;

	public ValoresIguaisResult() {
		super();
		this.groups = new ArrayList<>();
	}

	public List<ValoresIguaisGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<ValoresIguaisGroup> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "ValoresIguaisResult [groups=" + groups + "]";
	}

}
