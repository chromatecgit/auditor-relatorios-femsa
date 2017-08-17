package enums;

public enum IndentationEnum {
	LEVEL_1("\n"), LEVEL_2("\n\t"), LEVEL_3("\n\t\t"), LEVEL_4("\n\t\t\t");

	private String indentationEntity;

	private IndentationEnum(final String indentationEntity) {
		this.indentationEntity = indentationEntity;
	}

	public String getIndentationEntity() {
		return indentationEntity;
	}

}
