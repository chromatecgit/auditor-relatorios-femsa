package enums;

public enum TabsEnum {
	AGUA("SV_Agua"),
	CERVEJA("SV_Cerv"),
	CHA("SV_Cha"),
	CSD("SV_CSD"),
	ENERGETICO("SV_Energ"),
	ISOTONICO("SV_Iso"),
	LACTEO("SV_Lacteo"),
	OTHER("SV_Other"),
	REFRESCO("SV_Refres"),
	SOJA("SV_Soja"),
	SUCO("SV_Suco");
	
	private String tabName;
	
	TabsEnum(final String tabName) {
		this.tabName = tabName;
	}

	public String getTabName() {
		return tabName;
	}
	
}
