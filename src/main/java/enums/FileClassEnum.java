package enums;

public enum FileClassEnum {
	HORIZONTAL(1), VERTICAL(2), CONSOLIDADA(3);

	private int code;

	FileClassEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
