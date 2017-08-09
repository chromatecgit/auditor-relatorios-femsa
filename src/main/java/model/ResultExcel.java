package model;

import enums.SearchTypeEnum;

public class ResultExcel {

	private String tabName;
	private String id;
	private SearchTypeEnum searchType;

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SearchTypeEnum getSearchType() {
		return searchType;
	}

	public void setSearchType(SearchTypeEnum searchType) {
		this.searchType = searchType;
	}

	@Override
	public String toString() {
		return "ResultExcel [tabName=" + tabName + ", id=" + id + ", searchType=" + searchType + "]";
	}
	
	

}
