package enums;

public enum ConsolidadaKeyColumns {
	SALESCHANNEL("SALESCHANNEL"),
	FOI_POSSIVEL_REALIZAR_A_PESQUISA("FOI POSSÍVEL REALIZAR A PESQUISA"),
	NADA_EXPOSTO("NADA EXPOSTO"),
	COLETOU_ALGUM_PREÇO("COLETOU ALGUM PREÇO"),
	TIPO_OCORRENCIA("TIPO DE OCORRÊNCIA");
	
	private String columnName;
	
	ConsolidadaKeyColumns(final String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnName() {
		return this.columnName;
	}
}
