package enums;

public enum ConsolidadaKeyColumns {
	COD_UNICO("COD_UNICO"),
	FOTO_DESCARTADA("Foto descartada"),
	HORARIO_DE_INICIO_DA_VISITA("Hor�rio de In�cio da Visita"),
	HORARIO_DE_TERMINO_DA_VISITA("Hor�rio de T�rmino da Visita"),
	LATITUDE("LATITUDE"),
	LONGITUDE("LONGITUDE"),
	NOME("NOME"),
	NUM_DE_IMAGENS_EM_EVIDENCIA("Num de Imagens em Evid�ncia"),
	NUM_DE_IMAGENS_EM_OCORRENCIA("Num de Imagens em Ocorr�ncia"),
	NUM_DE_IMAGENS_TOMADAS("Num de Imagens tomadas"),
	P_FOI_POSSIVEL_REALIZAR_A_VISITA("P. Foi poss�vel realizar a visita"),
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
