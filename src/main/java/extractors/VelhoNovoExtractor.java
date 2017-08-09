package extractors;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import enums.ConsolidadoKeyColumns;
import enums.ConsolidadoSoviFiltersEnum;
import enums.KeyColumnsEnum;
import enums.PrecoKeyColumns;
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
import model.VerticalPrecoID;
import model.VerticalPrecoRow;
import model.VerticalPrecoTab;
import model.VerticalSoviID;
import model.VerticalSoviRow;
import model.VerticalSoviTab;
import utils.MyLogPrinter;

public class VelhoNovoExtractor {

	private Map<String, Integer> verticalKeys;
	private Map<String, Integer> horizontalKeys;
	private Map<String, Integer> consolidadaKeys;
	private XSSFWorkbook oldWB;
	private XSSFWorkbook newWB;
	private XSSFWorkbook consolidadaWB;
	private HVMapGroup verticalMap;

	public VelhoNovoExtractor() {
		this.verticalKeys = new HashMap<>();
		this.horizontalKeys = new HashMap<>();
		this.consolidadaKeys = new HashMap<>();
	}

	public void getResults(List<Path> refPaths, List<Path> targetPaths) {
		try {
			String fileName = "";
			for (Path path : refPaths) {
				fileName = path.getName(path.getNameCount() - 1).toString();
				// System.out.println("Processando: " + fileName);
				if (fileName.contains("SOVI")) {
					if (fileName.contains("VERT")) {
						this.oldWB = this.filterKeys(path.toFile(), fileName, ConsolidadoKeyColumns.SOVI_V);
					} else {
						this.oldWB = this.filterKeys(path.toFile(), fileName, ConsolidadoKeyColumns.SOVI_H);
					}
				} else if (fileName.contains("PRECO")) {
					if (fileName.contains("VERT")) {
						this.oldWB = this.filterKeys(path.toFile(), fileName, PrecoKeyColumns.PRECO_V);
					} else {
						this.oldWB = this.filterKeys(path.toFile(), fileName, PrecoKeyColumns.PRECO_H);
					}
				}
				
			}
			
			for (Path path : targetPaths) {
				fileName = path.getName(path.getNameCount() - 1).toString();
				// System.out.println("Processando: " + fileName);
				if (fileName.contains("SOVI")) {
					if (fileName.contains("VERT")) {
						this.newWB = this.filterKeys(path.toFile(), fileName, ConsolidadoKeyColumns.SOVI_V);
					} else {
						this.newWB = this.filterKeys(path.toFile(), fileName, ConsolidadoKeyColumns.SOVI_H);
					}
				} else if (fileName.contains("PRECO")) {
					if (fileName.contains("VERT")) {
						this.newWB = this.filterKeys(path.toFile(), fileName, PrecoKeyColumns.PRECO_V);
					} else {
						this.newWB = this.filterKeys(path.toFile(), fileName, PrecoKeyColumns.PRECO_H);
					}
				}
				
			}
			
			if (fileName.contains("VERT")) {
				if (fileName.contains("SOVI")) {
					List<VerticalSoviTab> verticalOldTabs = this.extractFromVertical(this.oldWB);
					HVMapGroup oldGroup = this.mapVerticalTabs(verticalOldTabs).consolidateSoviBySKU();
					List<VerticalSoviTab> verticalNewTabs = this.extractFromVertical(this.newWB);
					HVMapGroup newGroup = this.mapVerticalTabs(verticalNewTabs).consolidateSoviBySKU();
					this.compareOldAndNew(oldGroup, newGroup, "sovi");
				} else {
					List<VerticalPrecoTab> verticalOldTabs = this.extractFromVertical1(this.oldWB);
					HVMapGroup oldGroup = this.mapVerticalTabs1(verticalOldTabs).consolidatePrecoBySKU();
					List<VerticalPrecoTab> verticalNewTabs = this.extractFromVertical1(this.newWB);
					HVMapGroup newGroup = this.mapVerticalTabs1(verticalNewTabs).consolidatePrecoBySKU();
//					MyLogPrinter.printCollection(oldGroup.getHVMaps(), "oldGroup.getHVMaps()");
//					MyLogPrinter.printCollection(newGroup.getHVMaps(), "newGroup.getHVMaps()");
					this.compareOldAndNew(oldGroup, newGroup, "preco");
				}
				
			} else {
				List<HorizontalSoviTab> horizontalOldTabs = this.extractFromHorizontal(this.oldWB);
				List<HorizontalSoviTab> horizontalNewTabs = this.extractFromHorizontal(this.newWB);
				//this.compareOldAndNew(horizontalOldTabs, horizontalNewTabs);
			}

//			if (this.compareSoviHorizontalVertical(verticalTabs, horizontalTabs)) {
//			if(true) {

				// System.out.println("SOVI VERTICAL E HORIZONTAL ESTÃO OK!");
				// System.out.println("Iniciando comparação com a consolidada");
//				Path consolidadaPath = targetPaths.stream().findFirst().orElse(null);
//				String fileName = consolidadaPath.getName(consolidadaPath.getNameCount() - 1).toString();
//				this.consolidadaWB = this.filterKeys(consolidadaPath.toFile(), fileName,
//						ConsolidadoKeyColumns.CONSOLIDADA);
//				List<ConsolidadaSoviTab> consolidadaSoviTabs = this.extractFromConsolidada();
//				MyLogPrinter.printCollection(consolidadaSoviTabs, "ConsolidadoSOVI[consolidadaSoviTabs]");
//				// System.out.println("CONSOLIDADA: " + consolidadaSoviTabs);
//				this.compareSoviVerticalToConsolidada(consolidadaSoviTabs);
//			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void compareOldAndNew(HVMapGroup verticalOldTabs, HVMapGroup verticalNewTabs, String type) {
		List<DiffMap> diffMaps = new ArrayList<>();
		for (HVMap oldTab : verticalOldTabs.getHVMaps()) {
			for (HVMap newTab : verticalNewTabs.getHVMaps()) {
				if (oldTab.getId().equals(newTab.getId())) {
					diffMaps.addAll(this.continueComparing(oldTab, newTab, type));
				}
			}
		}
		System.out.println("DIFF MAPS:" + diffMaps);
	}
	
	private List<DiffMap> continueComparing(HVMap oldTab, HVMap newTab, String type) {
		List<DiffMap> diffMaps = new ArrayList<>();
		boolean isDifferent = true;
		for (HVMapInfos oldID : oldTab.getMapInfos()) {
			for (HVMapInfos newID : newTab.getMapInfos()) {
				if (oldID.getSku().equals(newID.getSku())) {
					if (type.equals("preco")) {
						if (oldID.getPreco().equals(newID.getPreco())) {
							isDifferent = false;
							break;
						} else {
							isDifferent = true;
						}
					} else {
						if (oldID.getSovi().equals(newID.getSovi())) {
							isDifferent = false;
							break;
						} else {
							isDifferent = true;
						}
					}
				}
				
			}
			if (isDifferent) {
				DiffMap diffMap = new DiffMap();
				diffMap.setId(oldTab.getId());
				diffMap.setHorizontalOrigin(oldID.getSku());
				diffMap.setHorizontalPreco(oldID.getPreco());
				diffMaps.add(diffMap);
			}
			
		}
		return diffMaps;
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

	private List<HorizontalSoviTab> extractFromHorizontal(XSSFWorkbook wb) throws IOException {
		List<HorizontalSoviTab> horizontalTabs = new ArrayList<>();

		for (Sheet sheet : wb) {
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

		wb.close();
		wb = null;
		return horizontalTabs;
	}

	private List<VerticalSoviTab> extractFromVertical(XSSFWorkbook wb) throws IOException {
		String lastID = "";
		boolean isNewID = true;
		List<VerticalSoviTab> verticalTabs = new ArrayList<>();

		for (Sheet sheet : wb) {
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

		wb.close();
		wb = null;
		return verticalTabs;
	}
	
	private List<VerticalPrecoTab> extractFromVertical1(XSSFWorkbook wb) throws IOException {
		String lastID = "";
		boolean isNewID = true;
		List<VerticalPrecoTab> verticalTabs = new ArrayList<>();

		for (Sheet sheet : wb) {
			// //System.out.println("[INICIO] Extracao Preco Vertical da Aba: " +
			// sheet.getSheetName());
			// TAB
			VerticalPrecoTab verticalPrecoTab = new VerticalPrecoTab();
			verticalPrecoTab.setTabName(sheet.getSheetName());
			List<VerticalPrecoID> ids = new ArrayList<>();
			verticalPrecoTab.setVerticalPrecoIDs(ids);
			// ID
			VerticalPrecoID verticalPrecoID = null;
			List<VerticalPrecoRow> verticalRows = null;
			for (Row row : sheet) {
				VerticalPrecoRow verticalPrecoRow = new VerticalPrecoRow();
				if (row.getRowNum() != 0) {

					for (String keyColumn : verticalKeys.keySet()) {
						Cell cell = row.getCell(verticalKeys.get(keyColumn));
						if (KeyColumnsEnum.MATRICULA.getFieldName().equalsIgnoreCase(keyColumn)) {
							if (!this.getCellValueAsString(cell).equals(lastID)) {
								verticalPrecoID = new VerticalPrecoID();
								verticalRows = new ArrayList<>();
								verticalPrecoID.setVerticalPrecoRows(verticalRows);
								verticalPrecoID.setId(this.getCellValueAsString(cell));
								lastID = verticalPrecoID.getId();
								isNewID = true;
							} else {
								isNewID = false;
							}
						} else if (KeyColumnsEnum.POC.getFieldName().equalsIgnoreCase(keyColumn)) {
							verticalPrecoRow.setPoc(cell.getStringCellValue());
						} else if (KeyColumnsEnum.SKU.getFieldName().equalsIgnoreCase(keyColumn)) {
							verticalPrecoRow.setSku(cell.getStringCellValue().toUpperCase());
						} else if (KeyColumnsEnum.PRECO.getFieldName().equalsIgnoreCase(keyColumn)) {
							verticalPrecoRow.setPreco((double) cell.getNumericCellValue());
						}
					}
					verticalPrecoID.getVerticalPrecoRows().add(verticalPrecoRow);
					if (isNewID) {
						verticalPrecoTab.getVerticalPrecoIDs().add(verticalPrecoID);
					}
				}
			}
			verticalTabs.add(verticalPrecoTab);
			// //System.out.println("[FINAL] Extracao Preco Vertical da Aba: " +
			// sheet.getSheetName());
		}

		wb.close();
		wb = null;
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
	
	private HVMapGroup mapVerticalTabs1(List<VerticalPrecoTab> verticalTabs) {
		HVMapGroup verticalMapGroup = new HVMapGroup();

		for (VerticalPrecoTab verticalTab : verticalTabs) {
			for (VerticalPrecoID verticalSovi : verticalTab.getVerticalPrecoIDs()) {
				HVMap hvMap = new HVMap();
				hvMap.setId(verticalSovi.getId());

				for (VerticalPrecoRow verticalRow : verticalSovi.getVerticalPrecoRows()) {
					HVMapInfos infos = new HVMapInfos();
					infos.setPoc(verticalRow.getPoc());
					infos.setSku(verticalRow.getSku());
					infos.setPreco(verticalRow.getPreco());
					infos.setTabName(verticalTab.getTabName());
					hvMap.getMapInfos().add(infos);
				}
				Collections.sort(hvMap.getMapInfos());
				verticalMapGroup.addHVMap(hvMap);
			}
		}

		return verticalMapGroup;
	}



	private XSSFWorkbook filterKeys(File file, String fileName, ConsolidadoKeyColumns keyColumns) {
		try {

			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);

			Sheet sheet = wb.getSheetAt(wb.getFirstVisibleTab());
			// //System.out.println("Processando: " + sheet.getSheetName());

			Row row = sheet.getRow(sheet.getFirstRowNum());
			for (Cell cell : row) {
				for (String columnName : keyColumns.getColumnNames()) {
					String cellName = cell.getStringCellValue().toLowerCase();
					if (cellName.toLowerCase().equals(columnName) && keyColumns.equals(ConsolidadoKeyColumns.SOVI_V)) {
						this.verticalKeys.put(cellName, cell.getColumnIndex());
					} else if (cellName.toLowerCase().startsWith(columnName)
							&& keyColumns.equals(ConsolidadoKeyColumns.SOVI_H)) {
						this.horizontalKeys.put(cellName, cell.getColumnIndex());
					} else if (cellName.toLowerCase().startsWith(columnName)
							&& keyColumns.equals(ConsolidadoKeyColumns.CONSOLIDADA)) {
						this.consolidadaKeys.put(cellName, cell.getColumnIndex());
					}
				}
			}
			// //System.out.println("Chaves extraidas com sucesso de " +
			// sheet.getSheetName());
			return wb;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private XSSFWorkbook filterKeys(File file, String fileName, PrecoKeyColumns keyColumns) {
		try {

			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);

			Sheet sheet = wb.getSheetAt(wb.getFirstVisibleTab());
			// //System.out.println("Processando: " + sheet.getSheetName());

			Row row = sheet.getRow(sheet.getFirstRowNum());
			for (Cell cell : row) {
				for (String columnName : keyColumns.getColumnNames()) {
					String cellName = cell.getStringCellValue().toLowerCase();
					if (cellName.toLowerCase().equals(columnName) && keyColumns.equals(PrecoKeyColumns.PRECO_V)) {
						this.verticalKeys.put(cellName, cell.getColumnIndex());
					} else if (cellName.toLowerCase().startsWith(columnName)
							&& keyColumns.equals(PrecoKeyColumns.PRECO_H)) {
						this.horizontalKeys.put(cellName, cell.getColumnIndex());
					}
				}
			}
			// //System.out.println("Chaves extraidas com sucesso de " +
			// sheet.getSheetName());
			return wb;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return null;
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
