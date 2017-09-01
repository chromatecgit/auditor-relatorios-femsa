package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.bag.TreeBag;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ConsolidadoSoviFiltersEnum;
import enums.ConsolidadoTypeEnum;
import enums.FileClassEnum;
import enums.ProcessStageEnum;
import enums.TabEnum;
import exceptions.HaltException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportDocument;
import model.ReportTab;
import utils.ConsolidadosFilter;
import utils.FileManager;
import utils.MyLogPrinter;
import utils.TabUtils;

public class SoviModule {
	
	private String[] fileNames;
	private String[] filters = {"CATEGORIA"};
	private int errorNumber = 0;
	private PathBuilderMapValue consolidadaValue = new PathBuilderMapValue();
	private final String volumeRegexPattern = "\\d+(\\.\\d+)?M?L";
	private final Pattern compiledPattern = Pattern.compile(volumeRegexPattern);

	public SoviModule(final String[] fileNames) {
		this.fileNames = fileNames;
	}
	
	public void execute() {
		
		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] { ProjectConfiguration.newFilesPath });
		
		final Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();
		
		ReportDocument verticalDocument = new ReportDocument();
		ReportTab horizontalTab = new ReportTab();
		
		for (String fileName : pathsMap.keySet()) {
			if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.VERTICAL.getCode()) {
				verticalDocument = FileManager.fetchVerticalDocument(
						fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, filters);
				MyLogPrinter.printObject(verticalDocument, "verticalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.HORIZONTAL.getCode()) {
				horizontalTab = FileManager.fetchHorizontalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, true);
				MyLogPrinter.printObject(horizontalTab, "horizontalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.CONSOLIDADA.getCode()) {
				this.consolidadaValue = pathsMap.get(fileName);
			}
		}
		
		try {
			this.applyBusinessRule(verticalDocument, horizontalTab);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}
	
	private void applyBusinessRule(final ReportDocument verticalDocument, final ReportTab horizontalTab) throws HaltException {
		// Verificar se as duas possuem o mesmo tamanho antes
		List<ReportCellKey> outKeys = new ArrayList<>();
		TabUtils.merge(verticalDocument).getCells().forEach( (key, vCell) -> {
			ReportCell hCell = horizontalTab.getCells().get(key.getKeyWithEmptyPoc());
			if (hCell != null) {
				if (!vCell.getValue().equals(hCell.getValue())) {
					MyLogPrinter.addToBuiltMessage("[Horizontal]=" + key + " valores=" + hCell + "/[Vertical]=" + key + " valores=" + vCell);
					errorNumber++;
				}
			} else {
				outKeys.add(key);
			}
		});
		MyLogPrinter.printObject(outKeys, "SoviModule_outkeys");
		MyLogPrinter.printBuiltMessage("SoviModule_diff");
//		if (outKeys.isEmpty() && errorNumber == 0) {
			this.compareSoviVerticalToConsolidada(verticalDocument);
//		}

	}

	private boolean compareSoviVerticalToConsolidada(final ReportDocument verticalDocument) {
		ReportTab consolidadaTab = FileManager.fetchConsolidadaDocument("CONSOLIDADA_SOVI", consolidadaValue, ProcessStageEnum.FULL);
		this.classifyByConsolidadaRules(verticalDocument);
		MyLogPrinter.printObject(consolidadaTab, "consolidadaTab");
		
		return false;

	}
	
	private ReportTab classifyByConsolidadaRules(final ReportDocument verticalDocument) {

		ReportTab parsedTab = new ReportTab();
		
		Map<ReportCellKey, List<String>> filterSkuMap = new HashMap<>();
		//Classificar os SKUs
		//ConsolidadoTypeEnum.SOVI
		for (ConsolidadoSoviFiltersEnum e : ConsolidadoSoviFiltersEnum.values()) {
			ConsolidadosFilter consolidadosFilter = e.getConsolidadosFilter();
			
			List<ReportTab> tabsToCalculate = new ArrayList<>();
			//TabEnum.AGUA
			for (TabEnum tabEnum : consolidadosFilter.getSheets()) {
				tabsToCalculate.add(verticalDocument.getTabs().get(tabEnum.getTabName()));
			}
			
			this.calculateUsingFilter(tabsToCalculate, consolidadosFilter, e);
		}
		return null;
	}

	private Map<ReportCellKey, Integer> calculateUsingFilter(final List<ReportTab> tabsToCalculate, final ConsolidadosFilter consolidadosFilter,
			final ConsolidadoSoviFiltersEnum e) {
		
		final List<Integer> soviToSum = new ArrayList<>();
		final String keyForMap = e.name();
		final Map<ReportCellKey, String> verticalCellMaps = new TreeMap<>();
		final ReportTab verticalConverted = new ReportTab();
		
		tabsToCalculate.stream().forEach( t -> {
			t.getCells().forEach( (key, cell) -> {
				boolean passed = false;
				if (key.getColumnName().startsWith(consolidadosFilter.getConsolidadoType().name())) {
					for (String poc : cell.getPocs()) {
						if (this.belongsToRule(key.getColumnName(), poc , consolidadosFilter)) {
							soviToSum.add(Integer.valueOf(cell.getValue()));
						}
					}
				}
//				ReportCellKey newKey = new ReportCellKey(key.getConcat(), e.name());
//				verticalCellMaps.put(newKey, value)
			});
			
			//gravar o concat e a regra da consolidada aqui
			//key e rule
		});
		
		
		
		return null;
	}

	private boolean belongsToRule(final String sku, final String poc, final ConsolidadosFilter rule) {
		//TODO: Talvez retirar o asterisco e colocar vazia, para melhorar performance
		String pocName = rule.getPoc().getCompleteName();
		if (!pocName.equals("*") && !poc.contains(pocName)) {
			return false;
		}
		
		for (String synonym : rule.getProduct().getSynonyms()) {
			if (synonym.equals("*")) {
				break;
			} else {
				if (!sku.contains(synonym)) {
					continue;
				} else {
					break;
				}
			}
		}
		
		String cia = rule.getCompany().getCia();
		if (!cia.equals("*") && !sku.contains(cia)) {
			return false;
		}
		
		if (rule.getConsumoImediato().isCI()) {
			Matcher m = compiledPattern.matcher(sku);
			if (m.matches()) {
				String result = m.group(1);
				if (result.contains("M")) {
					double number = Double.valueOf(result.replace("ML", ""));
					return number <= 600 ? true : false;
				} else {
					return false;
				}
			}
		}
		
		if (rule.getPropria().isPropria() && (!poc.contains("Proprio") || !poc.contains("Concorrencia"))) {
			return false;
		}
		
		//SOVI CHA FEMSA FUZE ICE TEA 300ML PET
		return true;
	}
	

	
}


