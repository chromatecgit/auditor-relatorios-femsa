package enums;

public enum CompanyEnum {
	ALL ("*"),
	FEMSA("FEMSA"),
	OUTROS ("OUTROS");
	
	private String cia;
	
	CompanyEnum(final String cia) {
		this.cia = cia;
	}
	
	public String getCia() {
		return this.cia;
	}
}
