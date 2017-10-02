package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import config.ProjectConfiguration;

public class Auditor {
	
	public static void main(String args[]) {
		List<String> argsList = Arrays.asList(args);

		try {
			Path logsRootFolder = Paths.get(System.getProperty("user.dir")).getParent().resolve(ProjectConfiguration.logFolderName);
			if (!Files.exists(logsRootFolder)) {
				ProjectConfiguration.newLogFolder = Files.createDirectory(logsRootFolder).toString().concat(File.separator);
			} else {
				ProjectConfiguration.newLogFolder = logsRootFolder.toString().concat(File.separator);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!argsList.isEmpty()) {
			argsList.stream().forEach(a -> {
				if (a.equals("help")) {
					System.out.println("Preco, Sovi, Produtividade, Consolidada, Entregas");
				} else {
					ModuleManager.startModule(a);
				}
			});
		} else {
			
			PrecoModule preco = new PrecoModule();
			preco.execute();
			
			SoviModule sovi = new SoviModule();
			sovi.execute();
			
			ProdutividadeModule produtividade = new ProdutividadeModule();
			produtividade.execute();
			
			ConsolidadaModule consolidadaModule = new ConsolidadaModule();
			consolidadaModule.execute();
			
			EntregasModule entregas = new EntregasModule();
			entregas.execute();
			
		}
		
	}
	
}
