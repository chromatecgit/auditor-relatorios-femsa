package enums;

public enum ConsolidadoKeyColumns {
	SOVI_H(new String[] {KeyColumnsEnum.MATRICULA.getFieldName(), KeyColumnsEnum.SOVI.getFieldName()}),
	SOVI_V(new String[] {KeyColumnsEnum.MATRICULA.getFieldName(), KeyColumnsEnum.SOVI.getFieldName(), KeyColumnsEnum.SKU.getFieldName(), KeyColumnsEnum.POC.getFieldName()}),
	CONSOLIDADA(new String[] {KeyColumnsEnum.MATRICULA.getFieldName(), KeyColumnsEnum.SOVI.getFieldName()});
	
	private String[] columnNames;
	
	ConsolidadoKeyColumns(final String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public String[] getColumnNames() {
		return this.columnNames;
	}
}
