package main;

import java.nio.file.Path;
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
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.FileManager;
import utils.MyLogPrinter;

public class ProdutividadeModule {
	private String[] fileNames;
	private Set<String> ids;

	public ProdutividadeModule(final String[] fileNames) {
		this.fileNames = fileNames;
		this.ids = new TreeSet<>();
	}

	public void execute() {

		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] { ProjectConfiguration.newFilesPath });

		final Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();

		ReportTab produtividadeTab = new ReportTab();

		for (String fileName : pathsMap.keySet()) {
			produtividadeTab = FileManager.fetchProdutividadeDocument(fileName, pathsMap.get(fileName),
					ProcessStageEnum.FULL);
			MyLogPrinter.printObject(produtividadeTab, "ProdutividadeModule_produtividadeTab");
		}

		try {
			this.applyBusinessRule(produtividadeTab);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}

	private void applyBusinessRule(final ReportTab produtividadeTab) throws HaltException {

		Map<ReportCellKey, ReportCell> produtividadeCells = new TreeMap<>();
		produtividadeCells.putAll(produtividadeTab.getCells());

		this.ids.addAll(this.extractIndexColumns(produtividadeCells));

		this.applySoviZeroRules(
				produtividadeCells,
				ConsolidadaKeyColumns.SALESCHANNEL.getColumnName(),
				ConsolidadaKeyColumns.NUM_DE_IMAGENS_TOMADAS.getColumnName(),
				ConsolidadaKeyColumns.NUM_DE_IMAGENS_EM_EVIDENCIA.getColumnName(),
				ConsolidadaKeyColumns.NUM_DE_IMAGENS_EM_OCORRENCIA.getColumnName(),
				ConsolidadaKeyColumns.SOVI.getColumnName());

	}

	private void applySoviZeroRules(final Map<ReportCellKey, ReportCell> produtividadeCells,
									final String saleschannel, final String tomadasColumn,
									final String evidenciaColumn, final String ocorrenciaColumn, final String soviColumn) {
		
		this.ids.stream().forEach( id -> {
			
			ReportCell soviCell = produtividadeCells.get(new ReportCellKey(id, soviColumn));
			ReportCell ocorrenciaCell = produtividadeCells.get(new ReportCellKey(id, ocorrenciaColumn));
			ReportCell tomadasCell = produtividadeCells.get(new ReportCellKey(id, tomadasColumn));
			ReportCell evidenciaCell = produtividadeCells.get(new ReportCellKey(id, evidenciaColumn));
			ReportCell saleschannelCell = produtividadeCells.get(new ReportCellKey(id, saleschannel));
			
			Integer ocorrenciaValue = Integer.valueOf(ocorrenciaCell.getValue());
			Integer tomadasValue = Integer.valueOf(tomadasCell.getValue());
			Integer evidenciaValue = Integer.valueOf(evidenciaCell.getValue());
			
			if (soviCell.getValue().startsWith("0")) {
				
				MyLogPrinter.addToBuiltMessage(id);
				
				if (ocorrenciaValue == 0 && evidenciaValue == 0) {
					MyLogPrinter.addToBuiltMessage("\tSOVI zerado para nenhuma ocorrencia e evidencia");
				}
				
				if (tomadasValue >= 3) {
					MyLogPrinter.addToBuiltMessage("\tSOVI zerado para " + tomadasValue + " fotos tomadas");
				}
				
				if (saleschannelCell.getValue().startsWith("AS") || saleschannelCell.getValue().startsWith("ATAC")) {
					MyLogPrinter.addToBuiltMessage("\t" + saleschannelCell.getValue() + " com SOVI zerado");
				}
				
			}
			
			
		});
		MyLogPrinter.printBuiltMessage("ProdutividadeModule_sovi_zerado");
	}

	private Set<String> extractIndexColumns(final Map<ReportCellKey, ReportCell> produtividadeCells) {
		return produtividadeCells.keySet().stream().map(k -> k.getConcat()).collect(Collectors.toSet());
	}
}
