package enums;

public enum POCTypeEnum {
	ALL("*"),
	GDM("Geladeira"),
	SB("Setor-de-Bebidas"),
	NONE("");
	
	private String completeName;
	
	POCTypeEnum(final String completeName){
		this.completeName = completeName;
	}

	public String getCompleteName() {
		return completeName;
	}
	
}
