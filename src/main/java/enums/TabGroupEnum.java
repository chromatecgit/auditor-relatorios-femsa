package enums;

public enum TabGroupEnum {
	ALL(new TabEnum[] {
			TabEnum.AGUA,
			TabEnum.CERVEJA,
			TabEnum.CHA,
			TabEnum.CSD,
			TabEnum.ENERGETICO,
			TabEnum.ISOTONICO,
			TabEnum.LACTEO,
			TabEnum.OTHER,
			TabEnum.REFRESCO,
			TabEnum.SOJA,
			TabEnum.SUCO}),
	NCB(new TabEnum[] {
				TabEnum.AGUA, 
				TabEnum.CHA, 
				TabEnum.ISOTONICO, 
				TabEnum.REFRESCO, 
				TabEnum.SUCO}),
	SUCOS(new TabEnum[] {
				TabEnum.REFRESCO,
				TabEnum.SUCO
	});
	
	private TabEnum[] tabs;
	
	TabGroupEnum(TabEnum[] tabs) {
		this.tabs = tabs;
	}

	public TabEnum[] getTabs() {
		return tabs;
	}
	
	public boolean containsTab(final String tabName, TabGroupEnum group) {
		switch(group) {
			case NCB:
				for (TabEnum tName : group.getTabs()) {
					if (tName.getTabName().equals(tabName)) {
						return true;
					}
				}
				break;
			case SUCOS: {
				for (TabEnum tName : group.getTabs()) {
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
