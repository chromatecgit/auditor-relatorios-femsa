package enums;

public enum TabEnum {
	AGUA("Agua"),
	CERVEJA("Cerveja"),
	CHA("Cha"),
	CSD("CSD"),
	ENERGETICO("Energetico"),
	ISOTONICO("Isotonico"),
	LACTEO("Lacteo"),
	OTHER("Other"),
	REFRESCO("Refresco"),
	SOJA("Soja"),
	SUCO("Suco");
	
	private String tabName;
	
	TabEnum(final String tabName) {
		this.tabName = tabName;
	}

	public String getTabName() {
		return tabName;
	}
	
}
