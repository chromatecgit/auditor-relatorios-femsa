package enums;

public enum ConcorrenciaEnum {
	SIM(true), NAO(false);

	private boolean isConcorrencia;

	ConcorrenciaEnum(boolean isConcorrencia) {
		this.isConcorrencia = isConcorrencia;
	}

	public boolean isConcorrencia() {
		return isConcorrencia;
	}

}
