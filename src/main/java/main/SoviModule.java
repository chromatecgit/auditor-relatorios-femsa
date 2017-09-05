package main;

import java.nio.file.Path;
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
import enums.ProcessStageEnum;
import enums.TabEnum;
import exceptions.HaltException;
import exceptions.WarningException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportDocument;
import model.ReportSymmetryResult;
import model.ReportTab;
import utils.ConsolidadosFilter;
import utils.FileManager;
import utils.MyLogPrinter;
import utils.ReportDocumentUtils;

public class SoviModule {

	private String[] fileNames;
	private String[] filters = { "CATEGORIA" };
	private PathBuilderMapValue consolidadaValue = new PathBuilderMapValue();
	private final String volumeRegexPattern = "\\d+(\\.\\d+)?M?L";
	private final Pattern compiledPattern = Pattern.compile(volumeRegexPattern);
	final Map<ReportCellKey, ReportCell> asymmetricValues = new TreeMap<>();

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
				verticalDocument = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName),
						ProcessStageEnum.FULL, filters);
				MyLogPrinter.printObject(verticalDocument, "SoviModule_verticalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.HORIZONTAL.getCode()) {
				horizontalTab = FileManager.fetchHorizontalDocument(fileName, pathsMap.get(fileName),
						ProcessStageEnum.FULL, true);
				MyLogPrinter.printObject(horizontalTab, "SoviModule_horizontalTab");
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
							"[Horizontal]=" + key + " valores=" + hCell + "/[Vertical]=" + key + " valores=" + vCell);
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
			asymmetricValues.putAll(vCells);
			MyLogPrinter.printCollection(
					this.formatAsymmetricValues(asymmetricValues), "SoviModule_asymmetricValues");
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
		ReportTab consolidadaTab = FileManager.fetchConsolidadaDocument("CONSOLIDADA_SOVI", consolidadaValue,
				ProcessStageEnum.FULL);
		this.classifyByConsolidadaRules(verticalDocument);
		MyLogPrinter.printObject(consolidadaTab, "SoviModule_consolidadaTab");

		return false;

	}

	private ReportTab classifyByConsolidadaRules(final ReportDocument verticalDocument) {

		ReportTab parsedTab = new ReportTab();

		Map<ReportCellKey, Integer> filterSkuMap = new TreeMap<>();
		
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
		 MyLogPrinter.printObject(filterSkuMap, "SoviModule_filterSkuMap");
		return null;
	}

	private Map<ReportCellKey, Integer> calculateUsingFilter(final List<ReportTab> tabsToCalculate,
			final ConsolidadosFilter consolidadosFilter, final ConsolidadoSoviFiltersEnum e) {

		final Map<ReportCellKey, Integer> verticalCellMaps = new TreeMap<>();

		tabsToCalculate.stream().forEach(t -> {
			t.getCells().forEach((key, cell) -> {
				if (key.getColumnName().startsWith(consolidadosFilter.getConsolidadoType().name())) {
					for (String poc : cell.getPocInfos().keySet()) {
						if (this.belongsToRule(key.getColumnName(), poc, consolidadosFilter)) {
							ReportCellKey newKey = new ReportCellKey(key.getConcat(), e.name());
							verticalCellMaps.merge(newKey, Integer.valueOf(cell.getPocInfos().get(poc)), (nv, ov) -> {
								return nv + ov;
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
			String[] brokenSku = sku.split(" ");
			for (String piece : brokenSku) {
				Matcher m = compiledPattern.matcher(piece);
				if (m.matches()) {
					String result = m.group(1);
					if (result.contains("M")) {
						double number = Double.valueOf(result.replace("ML", ""));
						if (number > 600) {
							return false;
						}
					} else {
						return false;
					}
					break;
				}
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
