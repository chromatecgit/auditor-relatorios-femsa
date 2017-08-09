package enums;

public enum PrecoKeyColumns {
	PRECO_H(new String[] {KeyColumnsEnum.MATRICULA.getFieldName(), KeyColumnsEnum.PRECO.getFieldName()}),
	PRECO_V(new String[] {KeyColumnsEnum.MATRICULA.getFieldName(), KeyColumnsEnum.PRECO.getFieldName(), KeyColumnsEnum.SKU.getFieldName()});
	
	private String[] columnNames;
	
	PrecoKeyColumns(final String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public String[] getColumnNames() {
		return this.columnNames;
	}
}
