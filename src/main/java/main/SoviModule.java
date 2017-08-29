package main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import config.GlobBuilder;
import config.PathBuilder;
import config.ProjectConfiguration;
import enums.ConsolidadoSoviFiltersEnum;
import enums.FileClassEnum;
import enums.ProcessStageEnum;
import exceptions.HaltException;
import model.PathBuilderMapValue;
import model.ReportCell;
import model.ReportCellKey;
import model.ReportTab;
import utils.ConsolidadosFilter;
import utils.FileManager;
import utils.MyLogPrinter;

public class SoviModule {
	
	private String[] fileNames;
	private String[] filters = {"CATEGORIA"};
	private int errorNumber = 0;
	private PathBuilderMapValue consolidadaValue = new PathBuilderMapValue();

	public SoviModule(final String[] fileNames) {
		this.fileNames = fileNames;
	}
	
	public void execute() {
		
		final PathBuilder pathBuilder = new PathBuilder();

		pathBuilder.buildFilePaths(GlobBuilder.buildGlobPatternWith(Arrays.asList(fileNames)),
				new Path[] { ProjectConfiguration.newFilesPath });
		
		final Map<String, PathBuilderMapValue> pathsMap = pathBuilder.getPathMaps();
		
		ReportTab verticalTab = new ReportTab();
		ReportTab horizontalTab = new ReportTab();
		
		for (String fileName : pathsMap.keySet()) {
			if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.VERTICAL.getCode()) {
				verticalTab = FileManager.fetchVerticalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, filters);
				MyLogPrinter.printObject(verticalTab, "verticalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.HORIZONTAL.getCode()) {
				horizontalTab = FileManager.fetchHorizontalDocument(fileName, pathsMap.get(fileName), ProcessStageEnum.FULL, true);
				MyLogPrinter.printObject(horizontalTab, "horizontalTab");
			} else if (pathsMap.get(fileName).getFileClass().getCode() == FileClassEnum.CONSOLIDADA.getCode()) {
				consolidadaValue = pathsMap.get(fileName);
			}
		}
		
		try {
			this.applyBusinessRule(verticalTab, horizontalTab);
		} catch (HaltException e) {
			e.printStackTrace();
		}
	}
	
	private void applyBusinessRule(final ReportTab verticalTab, final ReportTab horizontalTab) throws HaltException {
		// Verificar se as duas possuem o mesmo tamanho antes
		List<ReportCellKey> outKeys = new ArrayList<>();
		verticalTab.getCells().forEach( (key, vCell) -> {
			ReportCell hCell = horizontalTab.getCells().get(key);
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
		if (outKeys.isEmpty() && errorNumber == 0) {
			this.compareSoviVerticalToConsolidada(verticalTab);
		}

	}

	private boolean compareSoviVerticalToConsolidada(final ReportTab verticalTab) {
		ReportTab consolidadaTab = FileManager.fetchConsolidadaDocument("CONSOLIDADA_SOVI", consolidadaValue, ProcessStageEnum.FULL);
		//MyLogPrinter.printObject(consolidadaTab, "consolidadaTab");
		
		return false;

	}
	
	private ReportTab classifyByConsolidadaRules(final ReportTab verticalTab) {
		
		for (ConsolidadoSoviFiltersEnum e : ConsolidadoSoviFiltersEnum.values()) {
			ConsolidadosFilter consolidadosFilter = e.getConsolidadosFilter();
			
		}
		return verticalTab;
		
		
	}
//	
//	private HVMapGroupConsolidada mapConsolidada(List<ConsolidadaSoviTab> consolidadaSoviTabs) {
//		HVMapGroupConsolidada groupConsolidada = new HVMapGroupConsolidada();
//		
//		for (ConsolidadaSoviTab tab : consolidadaSoviTabs) {
//			for (ConsolidadaSoviID id : tab.getConsolidadaSoviIDs()) {
//				HVMapConsolidada hvMapConsolidada = new HVMapConsolidada();
//				hvMapConsolidada.setId(id.getId());
//
//				for (ConsolidadaSoviRow row : id.getConsolidadaSoviRows()) {
//					HVMapInfosGroup infosGroup = new HVMapInfosGroup();
//					infosGroup.setFilter(row.getColumnName());
//					infosGroup.setHvMapInfosList(this.applyRule(id.getId(), row.getColumnName()));
//					infosGroup.calculateTotalSovi();
//					hvMapConsolidada.getInfosGroup().add(infosGroup);
//				}
//				groupConsolidada.getHvMaps().add(hvMapConsolidada);
//			}
//		}
//		MyLogPrinter.printCollection(groupConsolidada.getHvMaps(), "VerticalXConsolidadaRules");
//		return groupConsolidada;
//	}
//
//	private List<HVMapInfos> applyRule(String id, String columnName) {
//		ConsolidadoSoviFiltersEnum columnEnum = ConsolidadoSoviFiltersEnum.valueOf(columnName);
//		ConsolidadosFilter consolidadosFilter = columnEnum.getConsolidadosFilter();
//
//		HVMap hvMapByID = this.verticalMap.findHVMapByID(id);
//		List<HVMapInfos> extractedInfos = hvMapByID.applyConsolidadaFilter(consolidadosFilter);
//
//		return extractedInfos;
//	}
//
//	private List<ConsolidadaSoviTab> extractFromConsolidada() throws IOException {
//		List<ConsolidadaSoviTab> consolidadaSoviTabs = new ArrayList<>();
//
//		for (Sheet sheet : consolidadaWB) {
//			// System.out.println("[INICIO] Extracao Sovi Consolidada da Aba: "
//			// + sheet.getSheetName());
//			ConsolidadaSoviTab consolidadaSoviTab = new ConsolidadaSoviTab();
//			List<ConsolidadaSoviID> consolidadaIDs = new ArrayList<>();
//			consolidadaSoviTab.setTabName(sheet.getSheetName());
//			consolidadaSoviTab.setConsolidadaSoviIDs(consolidadaIDs);
//			for (Row row : sheet) {
//				if (row.getRowNum() != 0) {
//					ConsolidadaSoviID consolidadaID = new ConsolidadaSoviID();
//					List<ConsolidadaSoviRow> consolidadaRows = new ArrayList<>();
//					consolidadaID.setConsolidadaSoviRows(consolidadaRows);
//
//					for (String keyColumn : consolidadaKeys.keySet()) {
//						Cell cell = row.getCell(consolidadaKeys.get(keyColumn));
//						if (KeyColumnsEnum.MATRICULA.getFieldName().equalsIgnoreCase(keyColumn)) {
//							consolidadaID.setId(this.getCellValueAsString(cell));
//						} else {
//							if (cell.getNumericCellValue() != 0.0) {
//								ConsolidadaSoviRow consolidadaRow = new ConsolidadaSoviRow();
//								consolidadaRow.setColumnName(keyColumn.toUpperCase());
//								consolidadaRow.setSovi((int) cell.getNumericCellValue());
//								consolidadaID.getConsolidadaSoviRows().add(consolidadaRow);
//							}
//						}
//					}
//					consolidadaSoviTab.getConsolidadaSoviIDs().add(consolidadaID);
//				}
//			}
//			// System.out.println("[FINAL] Extracao Sovi Consolidada da Aba: "
//			// + sheet.getSheetName());
//			consolidadaSoviTabs.add(consolidadaSoviTab);
//		}
//		MyLogPrinter.printCollection(consolidadaSoviTabs, "ConsolidadaSoviTabsResult");
//		this.consolidadaWB.close();
//		consolidadaWB = null;
//		return consolidadaSoviTabs;
//	}
//
//	private List<HorizontalSoviTab> extractFromHorizontal() throws IOException {
//		List<HorizontalSoviTab> horizontalTabs = new ArrayList<>();
//
//		for (Sheet sheet : horizontalWB) {
//			// System.out.println("[INICIO] Extracao Sovi Horizontal da Aba: " +
//			// sheet.getSheetName());
//			HorizontalSoviTab horizontalSoviTab = new HorizontalSoviTab();
//			List<HorizontalSoviID> horizontalIDs = new ArrayList<>();
//			horizontalSoviTab.setTabName(sheet.getSheetName());
//			horizontalSoviTab.setIds(horizontalIDs);
//			for (Row row : sheet) {
//				if (row.getRowNum() != 0) {
//					HorizontalSoviID horizontalID = new HorizontalSoviID();
//					List<HorizontalSoviRow> horizontalRows = new ArrayList<>();
//					horizontalID.setHorizontalSoviRows(horizontalRows);
//
//					for (String keyColumn : horizontalKeys.keySet()) {
//						Cell cell = row.getCell(horizontalKeys.get(keyColumn));
//						if (KeyColumnsEnum.MATRICULA.getFieldName().equalsIgnoreCase(keyColumn)) {
//							horizontalID.setId(this.getCellValueAsString(cell));
//						} else {
//							if (cell.getNumericCellValue() != 0.0) {
//								HorizontalSoviRow horizontalRow = new HorizontalSoviRow();
//								horizontalRow.setSku(keyColumn.toUpperCase());
//								horizontalRow.setSovi((int) cell.getNumericCellValue());
//								horizontalID.getHorizontalSoviRows().add(horizontalRow);
//							}
//						}
//					}
//					horizontalSoviTab.getIds().add(horizontalID);
//				}
//			}
//			// //System.out.println("[FINAL] Extracao Sovi Horizontal da Aba: "
//			// +
//			// sheet.getSheetName());
//			horizontalTabs.add(horizontalSoviTab);
//		}
//
//		horizontalWB.close();
//		horizontalWB = null;
//		return horizontalTabs;
//	}
//
//	private List<VerticalSoviTab> extractFromVertical() throws IOException {
//		String lastID = "";
//		boolean isNewID = true;
//		List<VerticalSoviTab> verticalTabs = new ArrayList<>();
//
//		for (Sheet sheet : verticalWB) {
//			// //System.out.println("[INICIO] Extracao Sovi Vertical da Aba: " +
//			// sheet.getSheetName());
//			// TAB
//			VerticalSoviTab verticalSoviTab = new VerticalSoviTab();
//			verticalSoviTab.setTabName(sheet.getSheetName());
//			List<VerticalSoviID> ids = new ArrayList<>();
//			verticalSoviTab.setVerticalSoviIDs(ids);
//			// ID
//			VerticalSoviID verticalSoviID = null;
//			List<VerticalSoviRow> verticalRows = null;
//			for (Row row : sheet) {
//				VerticalSoviRow verticalSoviRow = new VerticalSoviRow();
//				if (row.getRowNum() != 0) {
//
//					for (String keyColumn : verticalKeys.keySet()) {
//						Cell cell = row.getCell(verticalKeys.get(keyColumn));
//						if (KeyColumnsEnum.MATRICULA.getFieldName().equalsIgnoreCase(keyColumn)) {
//							if (!this.getCellValueAsString(cell).equals(lastID)) {
//								verticalSoviID = new VerticalSoviID();
//								verticalRows = new ArrayList<>();
//								verticalSoviID.setVerticalSoviRows(verticalRows);
//								verticalSoviID.setId(this.getCellValueAsString(cell));
//								lastID = verticalSoviID.getId();
//								isNewID = true;
//							} else {
//								isNewID = false;
//							}
//						} else if (KeyColumnsEnum.POC.getFieldName().equalsIgnoreCase(keyColumn)) {
//							verticalSoviRow.setPoc(cell.getStringCellValue());
//						} else if (KeyColumnsEnum.SKU.getFieldName().equalsIgnoreCase(keyColumn)) {
//							verticalSoviRow.setSku(cell.getStringCellValue().toUpperCase());
//						} else if (KeyColumnsEnum.SOVI.getFieldName().equalsIgnoreCase(keyColumn)) {
//							verticalSoviRow.setSovi((int) cell.getNumericCellValue());
//						}
//					}
//					verticalSoviID.getVerticalSoviRows().add(verticalSoviRow);
//					if (isNewID) {
//						verticalSoviTab.getVerticalSoviIDs().add(verticalSoviID);
//					}
//				}
//			}
//			verticalTabs.add(verticalSoviTab);
//			// //System.out.println("[FINAL] Extracao Sovi Vertical da Aba: " +
//			// sheet.getSheetName());
//		}
//
//		this.verticalWB.close();
//		this.verticalWB = null;
//		return verticalTabs;
//	}
//
//	private HVMapGroup mapVerticalTabs(List<VerticalSoviTab> verticalTabs) {
//		HVMapGroup verticalMapGroup = new HVMapGroup();
//
//		for (VerticalSoviTab verticalTab : verticalTabs) {
//			for (VerticalSoviID verticalSovi : verticalTab.getVerticalSoviIDs()) {
//				HVMap hvMap = new HVMap();
//				hvMap.setId(verticalSovi.getId());
//
//				for (VerticalSoviRow verticalRow : verticalSovi.getVerticalSoviRows()) {
//					HVMapInfos infos = new HVMapInfos();
//					infos.setPoc(verticalRow.getPoc());
//					infos.setSku(verticalRow.getSku());
//					infos.setSovi(verticalRow.getSovi());
//					infos.setTabName(verticalTab.getTabName());
//					hvMap.getMapInfos().add(infos);
//				}
//				Collections.sort(hvMap.getMapInfos());
//				verticalMapGroup.addHVMap(hvMap);
//			}
//		}
//
//		return verticalMapGroup;
//	}
//
//	private HVMapGroup mapHorizontalTabs(List<HorizontalSoviTab> horizontalTabs) {
//		HVMapGroup horizontalMap = new HVMapGroup();
//		for (HorizontalSoviTab horizontalTab : horizontalTabs) {
//			if (horizontalTab.getTabName().trim().equals("SOVI")) {
//				for (HorizontalSoviID horizontalID : horizontalTab.getIds()) {
//					HVMap hvMap = new HVMap();
//					hvMap.setId(horizontalID.getId());
//					for (HorizontalSoviRow horizontalRow : horizontalID.getHorizontalSoviRows()) {
//						HVMapInfos infos = new HVMapInfos();
//						infos.setPoc("");
//						infos.setSku(horizontalRow.getSku());
//						infos.setSovi(horizontalRow.getSovi());
//						infos.setTabName(horizontalTab.getTabName());
//						hvMap.getMapInfos().add(infos);
//					}
//					Collections.sort(hvMap.getMapInfos());
//					horizontalMap.getHVMaps().add(hvMap);
//				}
//			}
//		}
//		return horizontalMap;
//	}
//
//	private XSSFWorkbook filterKeys(File file, String fileName, ConsolidadoKeyColumns keyColumns) {
//		try {
//			OPCPackage pkg = OPCPackage.open(file);
//
//			XSSFWorkbook wb = new XSSFWorkbook(pkg);
//
//			Sheet sheet = wb.getSheetAt(wb.getFirstVisibleTab());
//			System.out.println("Processando: " + sheet.getSheetName());
//
//			Row row = sheet.getRow(sheet.getFirstRowNum());
//			for (Cell cell : row) {
//				for (String columnName : keyColumns.getColumnNames()) {
//					String cellName = cell.getStringCellValue().toLowerCase();
//					if (cellName.toLowerCase().equals(columnName) && keyColumns.equals(ConsolidadoKeyColumns.SOVI_V)) {
//						this.verticalKeys.put(cellName, cell.getColumnIndex());
//					} else if (cellName.toLowerCase().startsWith(columnName)
//							&& keyColumns.equals(ConsolidadoKeyColumns.SOVI_H)) {
//						this.horizontalKeys.put(cellName, cell.getColumnIndex());
//					} else if (cellName.toLowerCase().startsWith(columnName)
//							&& keyColumns.equals(ConsolidadoKeyColumns.CONSOLIDADA)) {
//						this.consolidadaKeys.put(cellName, cell.getColumnIndex());
//					}
//				}
//			}
//			System.out.println("Chaves extraidas com sucesso de " +
//			sheet.getSheetName());
//			return wb;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private boolean compareSoviHorizontalVertical(final List<VerticalSoviTab> verticalTabs,
//			final List<HorizontalSoviTab> horizontalTabs) {
//		List<DiffMap> diffMaps = new ArrayList<>();
//
//		HVMapGroup horizontalMap = this.mapHorizontalTabs(horizontalTabs);
//		Collections.sort(horizontalMap.getHVMaps());
//
//		this.verticalMap = this.mapVerticalTabs(verticalTabs);
//		HVMapGroup consolidatedVertical = this.verticalMap.consolidateSoviBySKU();
//		Collections.sort(consolidatedVertical.getHVMaps());
//
//		// System.out.println("HORIZONTAL MAP: " + horizontalMap);
//		// System.out.println("VERTICAL CONSOLIDATED MAP: " +
//		// consolidatedVertical);
//
//		for (HVMap hMap : horizontalMap.getHVMaps()) {
//			for (HVMap vMap : consolidatedVertical.getHVMaps()) {
//				if (hMap.getId().equals(vMap.getId())) {
//					diffMaps.addAll(this.compareSoviAndSKU(hMap, vMap));
//				}
//			}
//		}
//		System.out.println("DIFFMAPS: " + diffMaps);
//		return diffMaps.isEmpty() ? true : false;
//	}
//
//	private List<DiffMap> compareSoviAndSKU(HVMap hMap, HVMap vMap) {
//		List<DiffMap> diffMaps = new ArrayList<>();
//
//		if (hMap.getMapInfos().size() != vMap.getMapInfos().size()) {
//			// System.out.println("HMAP: " + hMap.getMapInfos().size() + " - " +
//			// "VMAP: " + vMap.getMapInfos().size());
//			if (hMap.getMapInfos().size() - vMap.getMapInfos().size() < 0) {
//				// System.out.println("SKU a mais em Vertical");
//				for (String vSku : vMap.getAllSKUs()) {
//					List<HVMapInfos> findMapsBySKU = vMap.findMapsBySKU(vSku);
//					if (findMapsBySKU == null || findMapsBySKU.isEmpty()) {
//						// System.out.println("\tSKU: " + vSku);
//					}
//				}
//			} else {
//				// System.out.println("SKU a mais em Horizontal");
//				for (String hSku : hMap.getAllSKUs()) {
//					List<HVMapInfos> findMapsBySKU = vMap.findMapsBySKU(hSku);
//					if (findMapsBySKU == null || findMapsBySKU.isEmpty()) {
//						// System.out.println("\tSKU: " + hSku);
//					}
//				}
//			}
//
//		}
//		for (HVMapInfos hInfos : hMap.getMapInfos()) {
//			for (HVMapInfos vInfos : vMap.getMapInfos()) {
//				if (hInfos.getSku().equalsIgnoreCase(vInfos.getSku())) {
//					if (hInfos.getSovi().equals(vInfos.getSovi())) {
//						break;
//					} else {
//						DiffMap diffMap = new DiffMap();
//						diffMap.setId(hMap.getId());
//						diffMap.setSku(vInfos.getSku());
//						diffMap.setHorizontalSovi(hInfos.getSovi());
//						diffMap.setVerticalSovi(vInfos.getSovi());
//						diffMaps.add(diffMap);
//					}
//				}
//			}
//		}
//		return diffMaps;
//	}
	
}


