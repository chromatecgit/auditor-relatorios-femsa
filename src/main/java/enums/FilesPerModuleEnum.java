package enums;

public enum FilesPerModuleEnum {
	NUMERO_LINHAS(new ExcelFileNameEnum[] {
			ExcelFileNameEnum.SOVI,
			ExcelFileNameEnum.CONSOLIDADA_SOVI,
			ExcelFileNameEnum.CONSOLIDADA,
			ExcelFileNameEnum.PRODUTIVIDADE,
			ExcelFileNameEnum.PRECO});
	
	private ExcelFileNameEnum[] excelFileNameEnums;
	
	FilesPerModuleEnum(final ExcelFileNameEnum[] excelFileNameEnums) {
		this.excelFileNameEnums = excelFileNameEnums;
	}

	public ExcelFileNameEnum[] getExcelFileNameEnums() {
		return excelFileNameEnums;
	}
	
}
