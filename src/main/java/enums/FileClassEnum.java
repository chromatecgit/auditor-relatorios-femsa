package enums;

public enum FileClassEnum {
	HORIZONTAL(1), VERTICAL(2), CONSOLIDADA_SOVI(3), CONSOLIDADA(4), PRODUTIVIDADE(5);

	private int code;

	FileClassEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
