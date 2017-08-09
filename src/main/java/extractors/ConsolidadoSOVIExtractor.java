package extractors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.InputSource;

import enums.ConsolidadoKeyColumns;
import enums.ConsolidadoSoviFiltersEnum;
import enums.KeyColumnsEnum;
import model.ConsolidadaSoviID;
import model.ConsolidadaSoviRow;
import model.ConsolidadaSoviTab;
import model.ConsolidadosFilter;
import model.DiffMap;
import model.HVMap;
import model.HVMapConsolidada;
import model.HVMapGroup;
import model.HVMapGroupConsolidada;
import model.HVMapInfos;
import model.HVMapInfosGroup;
import model.HorizontalSoviID;
import model.HorizontalSoviRow;
import model.HorizontalSoviTab;
import model.VerticalSoviID;
import model.VerticalSoviRow;
import model.VerticalSoviTab;
import utils.MyLogPrinter;

public class ConsolidadoSOVIExtractor {

	private Map<String, Integer> verticalKeys;
	private Map<String, Integer> horizontalKeys;
	private Map<String, Integer> consolidadaKeys;
	private XSSFWorkbook verticalWB;
	private XSSFWorkbook horizontalWB;
	private XSSFWorkbook consolidadaWB;
	private HVMapGroup verticalMap;

	public ConsolidadoSOVIExtractor() {
		this.verticalKeys = new HashMap<>();
		this.horizontalKeys = new HashMap<>();
		this.consolidadaKeys = new HashMap<>();
	}

	public void getResults(List<Path> refPaths, List<Path> targetPaths) {
		try {
			for (Path path : refPaths) {
				String fileName = path.getName(path.getNameCount() - 1).toString();
				// System.out.println("Processando: " + fileName);
				if (fileName.contains("VERT")) {
					// System.out.println("Workbook Vertical");
					this.verticalWB = this.filterKeys(path.toFile(), fileName, ConsolidadoKeyColumns.SOVI_V);
				} else {
					// System.out.println("Workbook Horizontal");
					this.horizontalWB = this.filterKeys(path.toFile(), fileName, ConsolidadoKeyColumns.SOVI_H);
				}
			}

			List<VerticalSoviTab> verticalTabs = this.extractFromVertical();
			MyLogPrinter.printCollection(verticalTabs, "ConsolidadoSOVI[verticalTabs]");
			List<HorizontalSoviTab> horizontalTabs = this.extractFromHorizontal();
			MyLogPrinter.printCollection(horizontalTabs, "ConsolidadoSOVI[horizontalTabs]");

			if (this.compareSoviHorizontalVertical(verticalTabs, horizontalTabs)) {
//			if(true) {
				verticalTabs = null;
				horizontalTabs = null;
				// System.out.println("SOVI VERTICAL E HORIZONTAL ESTÃO OK!");
				// System.out.println("Iniciando comparação com a consolidada");
				Path consolidadaPath = targetPaths.stream().findFirst().orElse(null);
				String fileName = consolidadaPath.getName(consolidadaPath.getNameCount() - 1).toString();
				this.consolidadaWB = this.filterKeys(consolidadaPath.toFile(), fileName,
						ConsolidadoKeyColumns.CONSOLIDADA);
				List<ConsolidadaSoviTab> consolidadaSoviTabs = this.extractFromConsolidada();
				MyLogPrinter.printCollection(consolidadaSoviTabs, "ConsolidadoSOVI[consolidadaSoviTabs]");
				// System.out.println("CONSOLIDADA: " + consolidadaSoviTabs);
				this.compareSoviVerticalToConsolidada(consolidadaSoviTabs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean compareSoviVerticalToConsolidada(List<ConsolidadaSoviTab> consolidadaSoviTabs) {
		List<DiffMap> diffMaps = new ArrayList<>();
		HVMapGroupConsolidada groupConsolidada = this.mapConsolidada(consolidadaSoviTabs);
		//FIXME: Mudar esse jeito de validar!
		for (ConsolidadaSoviTab tab : consolidadaSoviTabs) {
			for (ConsolidadaSoviID id : tab.getConsolidadaSoviIDs()) {
				HVMapConsolidada tempMap = groupConsolidada.findHVMapGroupConsolidadaByID(id.getId());
				for (ConsolidadaSoviRow row : id.getConsolidadaSoviRows()) {
					HVMapInfosGroup tempGroup = tempMap.findHVMapInfosGroupByFilterName(row.getColumnName());
					if (tempGroup.getTotalSovi() == row.getSovi()) {
						break;
					} else {
						DiffMap diffMap = new DiffMap();
						diffMap.setId(id.getId());
						diffMap.setHorizontalOrigin(row.getColumnName());
						diffMap.setHorizontalSovi(row.getSovi());
						diffMap.setVerticalSovi(tempGroup.getTotalSovi());
						diffMaps.add(diffMap);
					}
				}
			}
			
		}
		consolidadaSoviTabs = null;
		MyLogPrinter.printCollection(diffMaps, "DiffMapsConsolidadaSovi");
		return diffMaps.isEmpty() ? true : false;

	}
	
	private HVMapGroupConsolidada mapConsolidada(List<ConsolidadaSoviTab> consolidadaSoviTabs) {
		HVMapGroupConsolidada groupConsolidada = new HVMapGroupConsolidada();
		
		for (ConsolidadaSoviTab tab : consolidadaSoviTabs) {
			for (ConsolidadaSoviID id : tab.getConsolidadaSoviIDs()) {
				HVMapConsolidada hvMapConsolidada = new HVMapConsolidada();
				hvMapConsolidada.setId(id.getId());

				for (ConsolidadaSoviRow row : id.getConsolidadaSoviRows()) {
					HVMapInfosGroup infosGroup = new HVMapInfosGroup();
					infosGroup.setFilter(row.getColumnName());
					infosGroup.setHvMapInfosList(this.applyRule(id.getId(), row.getColumnName()));
					infosGroup.calculateTotalSovi();
					hvMapConsolidada.getInfosGroup().add(infosGroup);
				}
				groupConsolidada.getHvMaps().add(hvMapConsolidada);
			}
		}
		MyLogPrinter.printCollection(groupConsolidada.getHvMaps(), "VerticalXConsolidadaRules");
		return groupConsolidada;
	}

	private List<HVMapInfos> applyRule(String id, String columnName) {
		ConsolidadoSoviFiltersEnum columnEnum = ConsolidadoSoviFiltersEnum.valueOf(columnName);
		ConsolidadosFilter consolidadosFilter = columnEnum.getConsolidadosFilter();

		HVMap hvMapByID = this.verticalMap.findHVMapByID(id);
		List<HVMapInfos> extractedInfos = hvMapByID.applyConsolidadaFilter(consolidadosFilter);

		return extractedInfos;
	}

	private List<ConsolidadaSoviTab> extractFromConsolidada() throws IOException {
		List<ConsolidadaSoviTab> consolidadaSoviTabs = new ArrayList<>();

		for (Sheet sheet : consolidadaWB) {
			// System.out.println("[INICIO] Extracao Sovi Consolidada da Aba: "
			// + sheet.getSheetName());
			ConsolidadaSoviTab consolidadaSoviTab = new ConsolidadaSoviTab();
			List<ConsolidadaSoviID> consolidadaIDs = new ArrayList<>();
			consolidadaSoviTab.setTabName(sheet.getSheetName());
			consolidadaSoviTab.setConsolidadaSoviIDs(consolidadaIDs);
			for (Row row : sheet) {
				if (row.getRowNum() != 0) {
					ConsolidadaSoviID consolidadaID = new ConsolidadaSoviID();
					List<ConsolidadaSoviRow> consolidadaRows = new ArrayList<>();
					consolidadaID.setConsolidadaSoviRows(consolidadaRows);

					for (String keyColumn : consolidadaKeys.keySet()) {
						Cell cell = row.getCell(consolidadaKeys.get(keyColumn));
						if (KeyColumnsEnum.MATRICULA.getFieldName().equalsIgnoreCase(keyColumn)) {
							consolidadaID.setId(this.getCellValueAsString(cell));
						} else {
							if (cell.getNumericCellValue() != 0.0) {
								ConsolidadaSoviRow consolidadaRow = new ConsolidadaSoviRow();
								consolidadaRow.setColumnName(keyColumn.toUpperCase());
								consolidadaRow.setSovi((int) cell.getNumericCellValue());
								consolidadaID.getConsolidadaSoviRows().add(consolidadaRow);
							}
						}
					}
					consolidadaSoviTab.getConsolidadaSoviIDs().add(consolidadaID);
				}
			}
			// System.out.println("[FINAL] Extracao Sovi Consolidada da Aba: "
			// + sheet.getSheetName());
			consolidadaSoviTabs.add(consolidadaSoviTab);
		}
		MyLogPrinter.printCollection(consolidadaSoviTabs, "ConsolidadaSoviTabsResult");
		this.consolidadaWB.close();
		consolidadaWB = null;
		return consolidadaSoviTabs;
	}

	private List<HorizontalSoviTab> extractFromHorizontal() throws IOException {
		List<HorizontalSoviTab> horizontalTabs = new ArrayList<>();

		for (Sheet sheet : horizontalWB) {
			// System.out.println("[INICIO] Extracao Sovi Horizontal da Aba: " +
			// sheet.getSheetName());
			HorizontalSoviTab horizontalSoviTab = new HorizontalSoviTab();
			List<HorizontalSoviID> horizontalIDs = new ArrayList<>();
			horizontalSoviTab.setTabName(sheet.getSheetName());
			horizontalSoviTab.setIds(horizontalIDs);
			for (Row row : sheet) {
				if (row.getRowNum() != 0) {
					HorizontalSoviID horizontalID = new HorizontalSoviID();
					List<HorizontalSoviRow> horizontalRows = new ArrayList<>();
					horizontalID.setHorizontalSoviRows(horizontalRows);

					for (String keyColumn : horizontalKeys.keySet()) {
						Cell cell = row.getCell(horizontalKeys.get(keyColumn));
						if (KeyColumnsEnum.MATRICULA.getFieldName().equalsIgnoreCase(keyColumn)) {
							horizontalID.setId(this.getCellValueAsString(cell));
						} else {
							if (cell.getNumericCellValue() != 0.0) {
								HorizontalSoviRow horizontalRow = new HorizontalSoviRow();
								horizontalRow.setSku(keyColumn.toUpperCase());
								horizontalRow.setSovi((int) cell.getNumericCellValue());
								horizontalID.getHorizontalSoviRows().add(horizontalRow);
							}
						}
					}
					horizontalSoviTab.getIds().add(horizontalID);
				}
			}
			// //System.out.println("[FINAL] Extracao Sovi Horizontal da Aba: "
			// +
			// sheet.getSheetName());
			horizontalTabs.add(horizontalSoviTab);
		}

		horizontalWB.close();
		horizontalWB = null;
		return horizontalTabs;
	}

	private List<VerticalSoviTab> extractFromVertical() throws IOException {
		String lastID = "";
		boolean isNewID = true;
		List<VerticalSoviTab> verticalTabs = new ArrayList<>();

		for (Sheet sheet : verticalWB) {
			// //System.out.println("[INICIO] Extracao Sovi Vertical da Aba: " +
			// sheet.getSheetName());
			// TAB
			VerticalSoviTab verticalSoviTab = new VerticalSoviTab();
			verticalSoviTab.setTabName(sheet.getSheetName());
			List<VerticalSoviID> ids = new ArrayList<>();
			verticalSoviTab.setVerticalSoviIDs(ids);
			// ID
			VerticalSoviID verticalSoviID = null;
			List<VerticalSoviRow> verticalRows = null;
			for (Row row : sheet) {
				VerticalSoviRow verticalSoviRow = new VerticalSoviRow();
				if (row.getRowNum() != 0) {

					for (String keyColumn : verticalKeys.keySet()) {
						Cell cell = row.getCell(verticalKeys.get(keyColumn));
						if (KeyColumnsEnum.MATRICULA.getFieldName().equalsIgnoreCase(keyColumn)) {
							if (!this.getCellValueAsString(cell).equals(lastID)) {
								verticalSoviID = new VerticalSoviID();
								verticalRows = new ArrayList<>();
								verticalSoviID.setVerticalSoviRows(verticalRows);
								verticalSoviID.setId(this.getCellValueAsString(cell));
								lastID = verticalSoviID.getId();
								isNewID = true;
							} else {
								isNewID = false;
							}
						} else if (KeyColumnsEnum.POC.getFieldName().equalsIgnoreCase(keyColumn)) {
							verticalSoviRow.setPoc(cell.getStringCellValue());
						} else if (KeyColumnsEnum.SKU.getFieldName().equalsIgnoreCase(keyColumn)) {
							verticalSoviRow.setSku(cell.getStringCellValue().toUpperCase());
						} else if (KeyColumnsEnum.SOVI.getFieldName().equalsIgnoreCase(keyColumn)) {
							verticalSoviRow.setSovi((int) cell.getNumericCellValue());
						}
					}
					verticalSoviID.getVerticalSoviRows().add(verticalSoviRow);
					if (isNewID) {
						verticalSoviTab.getVerticalSoviIDs().add(verticalSoviID);
					}
				}
			}
			verticalTabs.add(verticalSoviTab);
			// //System.out.println("[FINAL] Extracao Sovi Vertical da Aba: " +
			// sheet.getSheetName());
		}

		this.verticalWB.close();
		this.verticalWB = null;
		return verticalTabs;
	}

	private HVMapGroup mapVerticalTabs(List<VerticalSoviTab> verticalTabs) {
		HVMapGroup verticalMapGroup = new HVMapGroup();

		for (VerticalSoviTab verticalTab : verticalTabs) {
			for (VerticalSoviID verticalSovi : verticalTab.getVerticalSoviIDs()) {
				HVMap hvMap = new HVMap();
				hvMap.setId(verticalSovi.getId());

				for (VerticalSoviRow verticalRow : verticalSovi.getVerticalSoviRows()) {
					HVMapInfos infos = new HVMapInfos();
					infos.setPoc(verticalRow.getPoc());
					infos.setSku(verticalRow.getSku());
					infos.setSovi(verticalRow.getSovi());
					infos.setTabName(verticalTab.getTabName());
					hvMap.getMapInfos().add(infos);
				}
				Collections.sort(hvMap.getMapInfos());
				verticalMapGroup.addHVMap(hvMap);
			}
		}

		return verticalMapGroup;
	}

	private HVMapGroup mapHorizontalTabs(List<HorizontalSoviTab> horizontalTabs) {
		HVMapGroup horizontalMap = new HVMapGroup();
		for (HorizontalSoviTab horizontalTab : horizontalTabs) {
			if (horizontalTab.getTabName().trim().equals("SOVI")) {
				for (HorizontalSoviID horizontalID : horizontalTab.getIds()) {
					HVMap hvMap = new HVMap();
					hvMap.setId(horizontalID.getId());
					for (HorizontalSoviRow horizontalRow : horizontalID.getHorizontalSoviRows()) {
						HVMapInfos infos = new HVMapInfos();
						infos.setPoc("");
						infos.setSku(horizontalRow.getSku());
						infos.setSovi(horizontalRow.getSovi());
						infos.setTabName(horizontalTab.getTabName());
						hvMap.getMapInfos().add(infos);
					}
					Collections.sort(hvMap.getMapInfos());
					horizontalMap.getHVMaps().add(hvMap);
				}
			}
		}
		return horizontalMap;
	}

	private XSSFWorkbook filterKeys(File file, String fileName, ConsolidadoKeyColumns keyColumns) {
		try {
			OPCPackage pkg = OPCPackage.open(file);
			XSSFReader reader = new XSSFReader(pkg);
			SharedStringsTable sst = reader.getSharedStringsTable();

		    //XMLReader parser = fetchSheetParser(sst);

		    Iterator<InputStream> sheets = reader.getSheetsData();
		    while (sheets.hasNext()) {
		        InputStream sheet = sheets.next();
		        InputSource sheetSource = new InputSource(sheet);
		        //parser.parse(sheetSource);
		        sheet.close();
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean compareSoviHorizontalVertical(final List<VerticalSoviTab> verticalTabs,
			final List<HorizontalSoviTab> horizontalTabs) {
		List<DiffMap> diffMaps = new ArrayList<>();

		HVMapGroup horizontalMap = this.mapHorizontalTabs(horizontalTabs);
		Collections.sort(horizontalMap.getHVMaps());

		this.verticalMap = this.mapVerticalTabs(verticalTabs);
		HVMapGroup consolidatedVertical = this.verticalMap.consolidateSoviBySKU();
		Collections.sort(consolidatedVertical.getHVMaps());

		for (HVMap hMap : horizontalMap.getHVMaps()) {
			for (HVMap vMap : consolidatedVertical.getHVMaps()) {
				if (hMap.getId().equals(vMap.getId())) {
					diffMaps.addAll(this.compareSoviAndSKU(hMap, vMap));
				}
			}
		}
		System.out.println("DIFFMAPS: " + diffMaps);
		return diffMaps.isEmpty() ? true : false;
	}

	private List<DiffMap> compareSoviAndSKU(HVMap hMap, HVMap vMap) {
		List<DiffMap> diffMaps = new ArrayList<>();

		if (hMap.getMapInfos().size() != vMap.getMapInfos().size()) {
			if (hMap.getMapInfos().size() - vMap.getMapInfos().size() < 0) {
				MyLogPrinter.addToBuiltMessage("SKU a mais em Vertical: ");
				for (String vSku : vMap.getAllSKUs()) {
					List<HVMapInfos> mapBySKU = vMap.findMapsBySKU(vSku);
					if (mapBySKU == null || mapBySKU.isEmpty()) {
						MyLogPrinter.addToBuiltMessage(vSku);
						MyLogPrinter.printBuiltMessage(vMap.getId());
					}
				}
			} else {
				MyLogPrinter.addToBuiltMessage("SKU a mais em Horizontal: ");
				for (String hSku : hMap.getAllSKUs()) {
					List<HVMapInfos> mapBySKU = vMap.findMapsBySKU(hSku);
					if (mapBySKU == null || mapBySKU.isEmpty()) {
						MyLogPrinter.addToBuiltMessage(hSku);
						MyLogPrinter.printBuiltMessage(hMap.getId());
					}
				}
			}

		}
		for (HVMapInfos hInfos : hMap.getMapInfos()) {
			for (HVMapInfos vInfos : vMap.getMapInfos()) {
				if (hInfos.getSku().equalsIgnoreCase(vInfos.getSku())) {
					if (hInfos.getSovi().equals(vInfos.getSovi())) {
						break;
					} else {
						DiffMap diffMap = new DiffMap();
						diffMap.setId(hMap.getId());
						diffMap.setSku(vInfos.getSku());
						diffMap.setHorizontalSovi(hInfos.getSovi());
						diffMap.setVerticalSovi(vInfos.getSovi());
						diffMaps.add(diffMap);
					}
				}
			}
		}
		return diffMaps;
	}

	private String getCellValueAsString(final Cell cell) {
		String result = "";
		switch (cell.getCellTypeEnum()) {
		case STRING:
			result = cell.getStringCellValue();
			break;
		case NUMERIC:
			result = String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()).longValue());
			break;
		case BLANK:
			result = "-";
			break;
		default:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				result = String.valueOf(cell.getDateCellValue());
			} else {
				result = "";
			}
			break;
		}
		return result;
	}
	
}


