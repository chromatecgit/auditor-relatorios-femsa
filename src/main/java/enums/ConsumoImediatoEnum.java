package enums;

public enum ConsumoImediatoEnum {
	SIM(true), NAO(false);

	private boolean isCI;

	ConsumoImediatoEnum(boolean isCI) {
		this.isCI = isCI;
	}

	public boolean isCI() {
		return isCI;
	}

}
