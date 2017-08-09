package enums;

public enum PropriaEnum {
	SIM(true), NAO(false);

	private boolean isPropria;

	PropriaEnum(boolean isPropria) {
		this.isPropria = isPropria;
	}

	public boolean isPropria() {
		return isPropria;
	}

}
