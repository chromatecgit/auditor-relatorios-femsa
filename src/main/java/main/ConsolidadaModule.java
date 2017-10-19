package main;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ConsolidadaKeyColumns;
import enums.FilesPerModuleEnum;
import exceptions.HaltException;
import interfaces.Module;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;

public class ConsolidadaModule implements Module {
	private final String[] fileNames = FilesPerModuleEnum.CONSOLIDADA.getExcelFileNames();
	private Set<String> ids;

	public ConsolidadaModule() {
		this.ids = new TreeSet<>();
	}
	
	@Override
	public void execute() {

		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)), ProjectConfiguration.newFilesPath );

		final Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();

		ReportTab consolidadaTab = new ReportTab();

		for (String fileName : pathsMap.keySet()) {
			consolidadaTab = FileManager.fetchConsolidadaDocument(fileName, pathsMap.get(fileName));
			MyLogPrinter.printObject(consolidadaTab, "ConsolidadaModule_consolidadaTab");
		}

		try {
			this.applyBusinessRule(consolidadaTab);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}

	private void applyBusinessRule(final ReportTab consolidadaTab) throws HaltException {

		Map<ReportCellKey, ReportCell> consolidadaCells = new TreeMap<>();
		consolidadaCells.putAll(consolidadaTab.getCells());

		this.ids.addAll(this.extractIndexColumns(consolidadaCells));

		// ver se existe nada exposto ou nao tem coleta de preco em AS ou Atacado
		this.applySaleschannelRules(consolidadaCells, ConsolidadaKeyColumns.SALESCHANNEL.getColumnName(),
				ConsolidadaKeyColumns.FOI_POSSIVEL_REALIZAR_A_PESQUISA.getColumnName(),
				ConsolidadaKeyColumns.NADA_EXPOSTO.getColumnName(),
				ConsolidadaKeyColumns.COLETOU_ALGUM_PREÇO.getColumnName(),
				ConsolidadaKeyColumns.TIPO_OCORRENCIA.getColumnName());

		this.applyInformationConsistencyRules(consolidadaCells,
				ConsolidadaKeyColumns.FOI_POSSIVEL_REALIZAR_A_PESQUISA.getColumnName(),
				ConsolidadaKeyColumns.NADA_EXPOSTO.getColumnName(),
				ConsolidadaKeyColumns.COLETOU_ALGUM_PREÇO.getColumnName(),
				ConsolidadaKeyColumns.TIPO_OCORRENCIA.getColumnName());

	}

	private void applySaleschannelRules(final Map<ReportCellKey, ReportCell> consolidadaCells,
			final String saleschannel, final String possivelRealizarPesquisa, final String nadaExposto,
			final String coletouAlgumPreco, final String tipoOcorrencia) {
		
		this.ids.stream().forEach(id -> {

			ReportCell possivelRealizarPesquisaCell = consolidadaCells
					.get(new ReportCellKey(id, possivelRealizarPesquisa));
			ReportCell nadaExpostoCell = consolidadaCells.get(new ReportCellKey(id, nadaExposto));
			ReportCell coletouAlgumPrecoCell = consolidadaCells.get(new ReportCellKey(id, coletouAlgumPreco));
			ReportCell saleschannelCell = consolidadaCells.get(new ReportCellKey(id, saleschannel));
			ReportCell tipoOcorrenciaCell = consolidadaCells.get(new ReportCellKey(id, tipoOcorrencia));

			Integer possivelRealizarPesquisaValue = Integer.valueOf(possivelRealizarPesquisaCell.getValue());
			Integer nadaExpostoValue = Integer.valueOf(nadaExpostoCell.getValue());
			Integer coletouAlgumPrecoValue = Integer.valueOf(coletouAlgumPrecoCell.getValue());

			if ((saleschannelCell.getValue().startsWith("AS") || saleschannelCell.getValue().startsWith("ATAC"))
					&& possivelRealizarPesquisaValue == 1 && tipoOcorrenciaCell.getValue().isEmpty()) {
				
				MyLogPrinter.addToBuiltMessage(id);

				if (nadaExpostoValue == 1) {
					MyLogPrinter.addToBuiltMessage(
							"\tCanal " + saleschannelCell.getValue() + " com Nada Exposto");
				}
				
				if (coletouAlgumPrecoValue == 0) {
					MyLogPrinter.addToBuiltMessage(
							"\tCanal " + saleschannelCell.getValue() + " sem Coleta de Preco");
				}

			}

		});
		
		MyLogPrinter.printBuiltMessage("ConsolidadaModule_saleschannel_rules");

	}

	private void applyInformationConsistencyRules(final Map<ReportCellKey, ReportCell> consolidadaCells,
			final String possivelRealizarPesquisa, final String nadaExposto,
			final String coletouAlgumPreco, final String tipoOcorrencia) {

		this.ids.stream().forEach(id -> {

			ReportCell possivelRealizarPesquisaCell = consolidadaCells
					.get(new ReportCellKey(id, possivelRealizarPesquisa));
			ReportCell nadaExpostoCell = consolidadaCells.get(new ReportCellKey(id, nadaExposto));
			ReportCell coletouAlgumPrecoCell = consolidadaCells.get(new ReportCellKey(id, coletouAlgumPreco));
			ReportCell tipoOcorrenciaCell = consolidadaCells.get(new ReportCellKey(id, tipoOcorrencia));

			Integer possivelRealizarPesquisaValue = Integer.valueOf(possivelRealizarPesquisaCell.getValue());
			Integer nadaExpostoValue = Integer.valueOf(nadaExpostoCell.getValue());
			Integer coletouAlgumPrecoValue = Integer.valueOf(coletouAlgumPrecoCell.getValue());

			if (tipoOcorrenciaCell != null && !tipoOcorrenciaCell.getValue().isEmpty()) {
					
				if (possivelRealizarPesquisaValue == 1) {
					MyLogPrinter.addToBuiltMessage(id + " : Pesquisa com ocorrencia, porem com indicador de positivo para sua realizacao");
				}
				
				if (coletouAlgumPrecoValue == 1) {
					MyLogPrinter.addToBuiltMessage(id + " : Pesquisa com ocorrencia, porem com indicador de positivo para Coleta de Preco");
				}
				
				if (nadaExpostoValue == 1) {
					MyLogPrinter.addToBuiltMessage(id + " : Pesquisa com ocorrencia, porem com indicador de Nada Exposto");
				}

			}

		});
		MyLogPrinter.printBuiltMessage("ConsolidadaModule_information_inconsistencies");
	}

	private Set<String> extractIndexColumns(final Map<ReportCellKey, ReportCell> produtividadeCells) {
		return produtividadeCells.keySet().stream().map(k -> k.getConcat()).collect(Collectors.toSet());
	}
}
