package main;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import config.PathCreator;
import config.ProjectConfiguration;
import enums.FilesPerModuleEnum;
import utils.MyFileVisitor;

public class Auditor {
	
	public static void main(String args[]) {
		PathCreator pathCreator = new PathCreator();
		pathCreator.buildFilePaths();
		
		
		NumeroLinhasModule.executar(FilesPerModuleEnum.NUMERO_LINHAS.getExcelFileNameEnums());
	}
	
}
