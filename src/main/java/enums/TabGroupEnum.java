package enums;

public enum TabGroupEnum {
	ALL(new TabsEnum[] {
			TabsEnum.AGUA,
			TabsEnum.CERVEJA,
			TabsEnum.CHA,
			TabsEnum.CSD,
			TabsEnum.ENERGETICO,
			TabsEnum.ISOTONICO,
			TabsEnum.LACTEO,
			TabsEnum.OTHER,
			TabsEnum.REFRESCO,
			TabsEnum.SOJA,
			TabsEnum.SUCO}),
	NCB(new TabsEnum[] {
				TabsEnum.AGUA, 
				TabsEnum.CHA, 
				TabsEnum.ISOTONICO, 
				TabsEnum.REFRESCO, 
				TabsEnum.SUCO}),
	SUCOS(new TabsEnum[] {
				TabsEnum.REFRESCO,
				TabsEnum.SUCO
	});
	
	private TabsEnum[] tabs;
	
	TabGroupEnum(TabsEnum[] tabs) {
		this.tabs = tabs;
	}

	public TabsEnum[] getTabs() {
		return tabs;
	}
	
	public boolean containsTab(final String tabName, TabGroupEnum group) {
		switch(group) {
			case NCB:
				for (TabsEnum tName : group.getTabs()) {
					if (tName.getTabName().equals(tabName)) {
						return true;
					}
				}
				break;
			case SUCOS: {
				for (TabsEnum tName : group.getTabs()) {
					if (tName.getTabName().equals(tabName)) {
						return true;
					}
				}
			}
			default:
				break;
		}
		return false;
	}
	
}
