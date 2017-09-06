package enums;

public enum ConsolidadaKeyColumns {
	COD_UNICO("COD_UNICO"),
	FOTO_DESCARTADA("Foto descartada"),
	HORARIO_DE_INICIO_DA_VISITA("Horário de Início da Visita"),
	HORARIO_DE_TERMINO_DA_VISITA("Horário de Término da Visita"),
	LATITUDE("LATITUDE"),
	LONGITUDE("LONGITUDE"),
	NOME("NOME"),
	NUM_DE_IMAGENS_EM_EVIDENCIA("Num de Imagens em Evidência"),
	NUM_DE_IMAGENS_EM_OCORRENCIA("Num de Imagens em Ocorrência"),
	NUM_DE_IMAGENS_TOMADAS("Num de Imagens tomadas"),
	P_FOI_POSSIVEL_REALIZAR_A_VISITA("P. Foi possível realizar a visita"),
	P_NADA_EXPOSTO("P. Nada Exposto"),
	SALESCHANNEL("SALESCHANNEL"),
	SOVI("SOVI"),
	STATUS("Status"),
	TEMPO_DE_TOMADA_DE_IMAGENS("Tempo de tomada de imagens");
	
	private String columnName;
	
	ConsolidadaKeyColumns(final String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnName() {
		return this.columnName;
	}
}
