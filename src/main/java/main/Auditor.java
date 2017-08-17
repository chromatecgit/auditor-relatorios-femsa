package main;

import enums.FilesPerModuleEnum;

public class Auditor {
	
	public static void main(String args[]) {
//		NumeroLinhasModule.execute(
//				FilesPerModuleEnum.NUMERO_LINHAS.getExcelFileNames());
		EntregasModule.execute(FilesPerModuleEnum.ENTREGAS.getExcelFileNames());
	}
	
}
