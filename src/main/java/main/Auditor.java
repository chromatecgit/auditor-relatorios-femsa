package main;

import enums.FilesPerModuleEnum;

public class Auditor {
	
	public static void main(String args[]) {
//		NumeroLinhasModule.execute(FilesPerModuleEnum.NUMERO_LINHAS.getExcelFileNames());
		PrecoModule preco = new PrecoModule(FilesPerModuleEnum.PRECO.getExcelFileNames());
		preco.execute();
//		SoviModule sovi = new SoviModule(FilesPerModuleEnum.SOVI.getExcelFileNames());
//		sovi.execute();
//		ProdutividadeModule produtividade = new ProdutividadeModule(FilesPerModuleEnum.PRODUTIVIDADE.getExcelFileNames());
//		produtividade.execute();
//		ConsolidadaModule consolidadaModule = new ConsolidadaModule(FilesPerModuleEnum.CONSOLIDADA.getExcelFileNames());
//		consolidadaModule.execute();
//		EntregasModule entregas = new EntregasModule(FilesPerModuleEnum.ENTREGAS.getExcelFileNames());
//		entregas.execute();
	}
	
}
