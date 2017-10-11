package main;

import java.util.Arrays;
import java.util.List;

import config.ProjectConfiguration;

public class Auditor {
	
	public static void main(String args[]) {
		List<String> argsList = Arrays.asList(args);
		
		ProjectConfiguration.prepareEnvironment();
		
		if (!argsList.isEmpty()) {
			argsList.stream().forEach(a -> {
				if (a.equals("help")) {
					System.out.println("Preco, Sovi, Produtividade, Consolidada, Entregas, SoviPreco");
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
			
			SoviPrecoModule soviPrecoModule = new SoviPrecoModule();
			soviPrecoModule.execute();
			
		}
		
	}
	
}
