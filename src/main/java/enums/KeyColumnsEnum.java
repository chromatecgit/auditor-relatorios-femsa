package enums;

public enum KeyColumnsEnum {
	MATRICULA("matricula"), SOVI("sovi"), SKU("produto"), POC("poc"), PRECO("preco");

	private String fieldName;

	KeyColumnsEnum(final String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return this.fieldName;
	}

}
