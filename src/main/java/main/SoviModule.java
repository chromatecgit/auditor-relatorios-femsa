package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ConsolidadoSoviFiltersEnum;
import enums.FileClassEnum;
import enums.FilesPerModuleEnum;
import enums.ProcessStageEnum;
import enums.TabEnum;
import exceptions.HaltException;
import exceptions.WarningException;
import interfaces.Module;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportDocument;
import model.ReportSymmetryResult;
import model.ReportTab;
import model.SoviConsolidadaCell;
import utils.ConsolidadosFilter;
import utils.FileManager;
import utils.MyLogPrinter;
import utils.ReportDocumentUtils;

public class SoviModule implements Module {
	
	private final String[] fileNames = FilesPerModuleEnum.SOVI.getExcelFileNames();
	private String[] filters = { "CATEGORIA" };
	private PathBuilderMapValue consolidadaValue = new PathBuilderMapValue();
	private final String volumeRegexPattern = "\\d+(\\.\\d+)?M?L";
	private final Pattern compiledPattern = Pattern.compile(volumeRegexPattern);
	private final Map<ReportCellKey, ReportCell> asymmetricValues = new TreeMap<>();

	public SoviModule() {
	}
	
	@Override
	public void execute() {

		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(
				GlobBuilder.buildGlobPatternWith(
				Arrays.asList(fileNames)), ProjectConfiguration.newFilesPath );

		final Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();

		ReportDocument verticalDocument = new ReportDocument();
		ReportTab horizontalTab = new ReportTab();

		for (String fileName : pathsMap.keySet()) {
			if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.VERTICAL.getCode()) {
				verticalDocument = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName),
						ProcessStageEnum.FULL, filters);
				MyLogPrinter.printObject(verticalDocument, "SoviModule_verticalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.HORIZONTAL.getCode()) {
				horizontalTab = FileManager.fetchHorizontalDocument(fileName, pathsMap.get(fileName),
						ProcessStageEnum.FULL, true);
				MyLogPrinter.printObject(horizontalTab, "SoviModule_horizontalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.CONSOLIDADA_SOVI.getCode()) {
				this.consolidadaValue = pathsMap.get(fileName);
			}
		}

		try {
			this.applyBusinessRule(verticalDocument, horizontalTab);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}

	private void applyBusinessRule(final ReportDocument verticalDocument, final ReportTab horizontalTab)
			throws HaltException {
		ReportTab mergedVerticalTab = ReportDocumentUtils.merge(verticalDocument);
		try {
			this.checkSymmetry(mergedVerticalTab, horizontalTab);
		} catch (WarningException we) {
			we.printStackTrace();
		} finally {
			ReportTab newHorizontalTab = this.makeSymmetrical(horizontalTab, asymmetricValues);
			Map<ReportCellKey, ReportCell> mergedVerticalCells = mergedVerticalTab.getCells();
			System.out.println("mergedVerticalCells: " + mergedVerticalCells.size());
			newHorizontalTab.getCells().forEach((key, hCell) -> {
				ReportCell vCell = mergedVerticalCells.get(key);
				if (!hCell.getValue().equals(vCell.getValue())) {
					MyLogPrinter.addToBuiltMessage(
							"[Horizontal]=" + key + " valores=" + hCell + "\n\t[Vertical]=" + key + " valores=" + vCell);
				}
			});
			MyLogPrinter.printBuiltMessage("SoviModule_diff");
			this.compareSoviVerticalToConsolidada(verticalDocument);
		}

	}
	
	private void checkSymmetry(final ReportTab verticalTab, final ReportTab horizontalTab) {
		final Map<ReportCellKey, ReportCell> vCells = new TreeMap<>();
		vCells.putAll(verticalTab.getCells());
		System.out.println("vCells: " + vCells.size());
		final Map<ReportCellKey, ReportCell> hCells = new TreeMap<>();
		hCells.putAll(horizontalTab.getCells());
		System.out.println("hCells: " + hCells.size());

		hCells.forEach((key, cell) -> {
			if (vCells.remove(key) == null) {
				asymmetricValues.put(key, cell);
			}
		});

		if (!vCells.isEmpty() || !asymmetricValues.isEmpty()) {
			MyLogPrinter.printCollection(
					this.formatAsymmetricValues(asymmetricValues), "SoviModule_horizontal_only");
			MyLogPrinter.printObject(
					vCells, "SoviModule_vertical_only");
			asymmetricValues.putAll(vCells);
			throw new WarningException(
					"Existem registros em não conformidade. Favor conferir log do arquivo \"SoviModule_asymmetricValues\"");
		}

	}

	private ReportTab makeSymmetrical(final ReportTab horizontalTab, final Map<ReportCellKey, ReportCell> asymmetricValues) {
		ReportTab newTab = horizontalTab;
		asymmetricValues.forEach((k, v) -> {
			ReportCell cell = newTab.getCells().remove(k);
			if (cell == null) {
				newTab.getCells().put(k, v);
			}
		});
		MyLogPrinter.printObject(newTab, "SoviModule_newTab");
		return newTab;
	}

	private boolean compareSoviVerticalToConsolidada(final ReportDocument verticalDocument) {
		try {
			ReportTab consolidadaTab = FileManager.fetchConsolidadaSoviDocument("CONSOLIDADA_SOVI", consolidadaValue,
					ProcessStageEnum.FULL);
			List<String> outkeys = new ArrayList<>();
			Map<ReportCellKey, SoviConsolidadaCell> verticalTabMapped = this.classifyByConsolidadaRules(verticalDocument);
			consolidadaTab.getCells().forEach( (k, v) -> {
				SoviConsolidadaCell result = verticalTabMapped.get(k);
				if (result != null) {
					if (!Integer.valueOf(v.getValue()).equals(result.getValue())) {
						MyLogPrinter.addToBuiltMessage(
								"REGRA:" + result.getAddress() + " [Concat CONSOLIDADA SOVI]=" + k.getConcat() + " valor=" + v.getValue() + "/\n\t\t[Concat SOVI VERTICAL]=" + k.getConcat() + " valor=" + result.getValue());
					}
				} else {
					outkeys.add("Registro " + k + " nao encontrado");
				}
			});
			MyLogPrinter.printObject(verticalTabMapped, "SoviModule_verticalToConsolidadaTab");
			MyLogPrinter.printObject(consolidadaTab, "SoviModule_consolidadaTab");
			MyLogPrinter.printCollection(outkeys, "SoviModule_outkeys_consolidada");
			MyLogPrinter.printBuiltMessage("SoviModule_diff_consolidada");
		} catch (WarningException we) {
			we.printStackTrace();
		}
		
		return false;

	}

	private Map<ReportCellKey, SoviConsolidadaCell> classifyByConsolidadaRules(final ReportDocument verticalDocument) {

		Map<ReportCellKey, SoviConsolidadaCell> filterSkuMap = new TreeMap<>();
		
		for (ConsolidadoSoviFiltersEnum e : ConsolidadoSoviFiltersEnum.values()) {
			ConsolidadosFilter consolidadosFilter = e.getConsolidadosFilter();

			List<ReportTab> tabsToCalculate = new ArrayList<>();
			for (TabEnum tabEnum : consolidadosFilter.getSheets()) {
				tabsToCalculate.add(verticalDocument.getTabs().get(tabEnum.getTabName()));
			}
			filterSkuMap.putAll(
					this.calculateUsingFilter(tabsToCalculate, consolidadosFilter, e));
//			MyLogPrinter.printObject(filterSkuMap, "SoviModule_filterSkuMap_" + e.name());
//			filterSkuMap.clear();
		}
		return filterSkuMap;
	}

	private Map<ReportCellKey, SoviConsolidadaCell> calculateUsingFilter(final List<ReportTab> tabsToCalculate,
			final ConsolidadosFilter consolidadosFilter, final ConsolidadoSoviFiltersEnum e) {

		final Map<ReportCellKey, SoviConsolidadaCell> verticalCellMaps = new TreeMap<>();

		tabsToCalculate.stream().forEach(t -> {
			t.getCells().forEach((key, cell) -> {
				if (key.getColumnName().startsWith(consolidadosFilter.getConsolidadoType().name())) {
					for (String poc : cell.getPocInfos().keySet()) {
						if (this.belongsToRule(key.getColumnName(), poc, consolidadosFilter)) {
							ReportCellKey newKey = new ReportCellKey(key.getConcat(), e.name());
							SoviConsolidadaCell newCell = new SoviConsolidadaCell(key.getColumnName(), Integer.valueOf(cell.getPocInfos().get(poc)));
							newCell.getAppendix().getPocs().add(poc);
							newCell.getAppendix().getSkus().add(key.getColumnName());
							verticalCellMaps.merge(newKey, newCell, (ov, nv) -> {
								SoviConsolidadaCell s = new SoviConsolidadaCell(e.name(),  nv.getValue() + ov.getValue());
								s.getAppendix().getPocs().addAll(nv.getAppendix().getPocs());
								s.getAppendix().getPocs().addAll(ov.getAppendix().getPocs());
								
								s.getAppendix().getSkus().addAll(nv.getAppendix().getSkus());
								s.getAppendix().getSkus().addAll(ov.getAppendix().getSkus());
								return s;
							});
						}
					}
				}
			});
		});

		return verticalCellMaps;
	}

//	private Map<ReportCellDebugKey, Integer> calculateUsingFilterDebug(final List<ReportTab> tabsToCalculate,
//			final ConsolidadosFilter consolidadosFilter, final ConsolidadoSoviFiltersEnum e) {
//
//		final Map<ReportCellDebugKey, Integer> verticalCellMaps = new TreeMap<>();
//
//		tabsToCalculate.stream().forEach(t -> {
//			t.getCells().forEach((key, cell) -> {
//				if (key.getColumnName().startsWith(consolidadosFilter.getConsolidadoType().name())) {
//					for (String poc : cell.getPocInfos().keySet()) {
//						if (this.belongsToRule(key.getColumnName(), poc, consolidadosFilter)) {
//							ReportCellDebugKey newKey = new ReportCellDebugKey(key.getConcat(), key.getColumnName(),
//									poc, e.name());
//
//							verticalCellMaps.put(newKey, Integer.valueOf(cell.getValue()));
//						}
//					}
//				}
//			});
//		});
//
//		return verticalCellMaps;
//	}

	private boolean belongsToRule(final String sku, final String poc, final ConsolidadosFilter rule) {
		// TODO: Talvez retirar o asterisco e colocar vazia, para melhorar performance
		String pocName = rule.getPoc().getCompleteName();
		if (!pocName.equals("*") && !poc.contains(pocName)) {
			return false;
		}

		boolean containsSynonym = false;
		for (String synonym : rule.getProduct().getSynonyms()) {
			if (synonym.equals("*")) {
				containsSynonym = true;
				break;
			} else {
				if (!sku.contains(synonym)) {
					continue;
				} else {
					containsSynonym = true;
					break;
				}
			}
		}
		if (!containsSynonym) {
			return false;
		}

		String cia = rule.getCompany().getCia();
		if (!cia.equals("*") && !sku.contains(cia)) {
			return false;
		}

		if (rule.getConsumoImediato().isCI()) {
			Matcher m = compiledPattern.matcher(sku);
			if (m.find()) {
				String result = m.group(0);
				if (result.contains("M")) {
					double number = Double.valueOf(result.replace("ML", ""));
					if (number > 600) {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
			
		}

		if (rule.getPropria().isPropria() && (!poc.contains("Proprio") && !poc.contains("Concorrencia"))) {
			return false;
		}

		return true;
	}

	private Collection<ReportSymmetryResult> formatAsymmetricValues(
			final Map<ReportCellKey, ReportCell> asymmetricValues) {
		Map<String, ReportSymmetryResult> results = new HashMap<>();
		asymmetricValues.entrySet().stream().forEach(e -> {
			ReportSymmetryResult r = new ReportSymmetryResult();
			r.setKey(e.getKey().getConcat());
			r.getDescriptions().add(e.getKey().getColumnName() + "=" + e.getValue().getValue() + " ");
			results.merge(e.getKey().getConcat(), r, (nv, ov) -> {
				ov.getDescriptions().addAll(nv.getDescriptions());
				return ov;
			});
		});
		return results.values();
	}

}
