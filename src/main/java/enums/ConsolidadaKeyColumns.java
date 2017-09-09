package enums;

public enum ConsolidadaKeyColumns {
	SALESCHANNEL("SALESCHANNEL"),
	FOI_POSSIVEL_REALIZAR_A_PESQUISA("FOI POSS�VEL REALIZAR A PESQUISA"),
	NADA_EXPOSTO("NADA EXPOSTO"),
	COLETOU_ALGUM_PRE�O("COLETOU ALGUM PRE�O"),
	TIPO_OCORRENCIA("TIPO DE OCORR�NCIA");
	
	private String columnName;
	
	ConsolidadaKeyColumns(final String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnName() {
		return this.columnName;
	}
}
