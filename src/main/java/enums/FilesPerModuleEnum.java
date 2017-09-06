package enums;

public enum FilesPerModuleEnum {
	NUMERO_LINHAS (new String[] {
				ExcelFileNameEnum.SOVI.toString(),
				ExcelFileNameEnum.CONSOLIDADA_SOVI.toString(),
				ExcelFileNameEnum.CONSOLIDADA.toString(),
				ExcelFileNameEnum.PRODUTIVIDADE.toString(),
				ExcelFileNameEnum.PRECO.toString()
	}),
	ENTREGAS (new String[] {
				"*"
	}),
	PRECO (new String[] {
				ExcelFileNameEnum.PRECO.toString(),
				ExcelFileNameEnum.PRECO_VERT.toString()
	}),
	SOVI (new String[] {
			ExcelFileNameEnum.SOVI.toString(),
			ExcelFileNameEnum.SOVI_VERT.toString(),
			ExcelFileNameEnum.CONSOLIDADA_SOVI.toString()
	}),
	PRODUTIVIDADE (new String[] {
			ExcelFileNameEnum.PRODUTIVIDADE.toString()
	});
	
	private String[] excelFileNames;
	
	FilesPerModuleEnum(final String[] excelFileNames) {
		this.excelFileNames = excelFileNames;
	}

	public String[] getExcelFileNames() {
		return excelFileNames;
	}
	
}
