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

import enums.KeyColumnsEnum;
import enums.PrecoKeyColumns;
import model.DiffMap;
import model.HVMap;
import model.HVMapGroup;
import model.HVMapInfos;
import model.HorizontalPrecoID;
import model.HorizontalPrecoRow;
import model.HorizontalPrecoTab;
import model.VerticalPrecoID;
import model.VerticalPrecoRow;
import model.VerticalPrecoTab;

public class ConsolidadoPrecoExtractor {

	private Map<String, Integer> verticalKeys;
	private Map<String, Integer> horizontalKeys;
	private XSSFWorkbook verticalWB;
	private XSSFWorkbook horizontalWB;
	private HVMapGroup verticalMap;

	public ConsolidadoPrecoExtractor() {
		this.verticalKeys = new HashMap<>();
		this.horizontalKeys = new HashMap<>();
	}

	public void getResults(List<Path> refPaths) {
		try {
			for (Path path : refPaths) {
				String fileName = path.getName(path.getNameCount() - 1).toString();
				if (fileName.contains("VERT")) {
					this.verticalWB = this.filterKeys(path.toFile(), fileName, PrecoKeyColumns.PRECO_V);
				} else {
					this.horizontalWB = this.filterKeys(path.toFile(), fileName, PrecoKeyColumns.PRECO_H);
				}
			}

			List<VerticalPrecoTab> verticalTabs = this.extractFromVertical();
			List<HorizontalPrecoTab> horizontalTabs = this.extractFromHorizontal();

			this.comparePrecoHorizontalVertical(verticalTabs, horizontalTabs);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private List<HorizontalPrecoTab> extractFromHorizontal() throws IOException {
		List<HorizontalPrecoTab> horizontalTabs = new ArrayList<>();

		for (Sheet sheet : horizontalWB) {
			HorizontalPrecoTab horizontalPrecoTab = new HorizontalPrecoTab();
			List<HorizontalPrecoID> horizontalIDs = new ArrayList<>();
			horizontalPrecoTab.setTabName(sheet.getSheetName());
			horizontalPrecoTab.setHorizontalPrecoIDs(horizontalIDs);
			for (Row row : sheet) {
				if (row.getRowNum() != 0) {
					HorizontalPrecoID horizontalID = new HorizontalPrecoID();
					List<HorizontalPrecoRow> horizontalRows = new ArrayList<>();
					horizontalID.setHorizontalPrecoRows(horizontalRows);

					for (String keyColumn : horizontalKeys.keySet()) {
						Cell cell = row.getCell(horizontalKeys.get(keyColumn));
						if (KeyColumnsEnum.MATRICULA.getFieldName().equalsIgnoreCase(keyColumn)) {
							horizontalID.setId(this.getCellValueAsString(cell));
						} else {
							if (cell.getNumericCellValue() != 0.0) {
								HorizontalPrecoRow horizontalRow = new HorizontalPrecoRow();
								horizontalRow.setSku(keyColumn.toUpperCase());
								horizontalRow.setPreco((double) cell.getNumericCellValue());
								horizontalID.getHorizontalPrecoRows().add(horizontalRow);
							}
						}
					}
					horizontalPrecoTab.getHorizontalPrecoIDs().add(horizontalID);
				}
			}
			horizontalTabs.add(horizontalPrecoTab);
		}

		horizontalWB.close();
		horizontalWB = null;
		return horizontalTabs;
	}

	private List<VerticalPrecoTab> extractFromVertical() throws IOException {
		String lastID = "";
		boolean isNewID = true;
		List<VerticalPrecoTab> verticalTabs = new ArrayList<>();

		for (Sheet sheet : verticalWB) {
			//TAB
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
		}

		this.verticalWB.close();
		this.verticalWB = null;
		return verticalTabs;
	}

	private HVMapGroup mapVerticalTabs(List<VerticalPrecoTab> verticalTabs) {
		HVMapGroup verticalMapGroup = new HVMapGroup();

		for (VerticalPrecoTab verticalTab : verticalTabs) {
			for (VerticalPrecoID verticalPreco : verticalTab.getVerticalPrecoIDs()) {
				HVMap hvMap = new HVMap();
				hvMap.setId(verticalPreco.getId());

				for (VerticalPrecoRow verticalRow : verticalPreco.getVerticalPrecoRows()) {
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

	private HVMapGroup mapHorizontalTabs(List<HorizontalPrecoTab> horizontalTabs) {
		HVMapGroup horizontalMap = new HVMapGroup();
		for (HorizontalPrecoTab horizontalTab : horizontalTabs) {
			if (horizontalTab.getTabName().trim().equals("PRECO")) {
				for (HorizontalPrecoID horizontalID : horizontalTab.getHorizontalPrecoIDs()) {
					HVMap hvMap = new HVMap();
					hvMap.setId(horizontalID.getId());
					for (HorizontalPrecoRow horizontalRow : horizontalID.getHorizontalPrecoRows()) {
						HVMapInfos infos = new HVMapInfos();
						infos.setPoc("");
						infos.setSku(horizontalRow.getSku());
						infos.setPreco(horizontalRow.getPreco());
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

	private XSSFWorkbook filterKeys(File file, String fileName, PrecoKeyColumns keyColumns) {
		try {

			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);

			Sheet sheet = wb.getSheetAt(wb.getFirstVisibleTab());
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
			return wb;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean comparePrecoHorizontalVertical(final List<VerticalPrecoTab> verticalTabs,
			final List<HorizontalPrecoTab> horizontalTabs) {
		List<DiffMap> diffMaps = new ArrayList<>();

		HVMapGroup horizontalMap = this.mapHorizontalTabs(horizontalTabs);
		Collections.sort(horizontalMap.getHVMaps());

		this.verticalMap = this.mapVerticalTabs(verticalTabs);
		HVMapGroup consolidatedVertical = this.verticalMap.consolidatePrecoBySKU();
		Collections.sort(consolidatedVertical.getHVMaps());

		for (HVMap hMap : horizontalMap.getHVMaps()) {
			for (HVMap vMap : consolidatedVertical.getHVMaps()) {
				if (hMap.getId().equals(vMap.getId())) {
					diffMaps.addAll(this.comparePrecoAndSKU(hMap, vMap));
				}
			}
		}
		System.out.println("HMAP:" + horizontalMap);
		System.out.println("VMAP:" + consolidatedVertical);
		System.out.println("DIFFMAPS: " + diffMaps);
		return diffMaps.isEmpty() ? true : false;
	}

	private List<DiffMap> comparePrecoAndSKU(HVMap hMap, HVMap vMap) {
		List<DiffMap> diffMaps = new ArrayList<>();

		if (hMap.getMapInfos().size() != vMap.getMapInfos().size()) {
			if (hMap.getMapInfos().size() - vMap.getMapInfos().size() < 0) {
				for (String vSku : vMap.getAllSKUs()) {
					List<HVMapInfos> findMapsBySKU = vMap.findMapsBySKU(vSku);
					if (findMapsBySKU == null || findMapsBySKU.isEmpty()) {
						System.out.println("\tSKU: " + vSku);
					}
				}
			} else {
				for (String hSku : hMap.getAllSKUs()) {
					List<HVMapInfos> findMapsBySKU = vMap.findMapsBySKU(hSku);
					if (findMapsBySKU == null || findMapsBySKU.isEmpty()) {
						System.out.println("\tSKU: " + hSku);
					}
				}
			}

		}
		for (HVMapInfos hInfos : hMap.getMapInfos()) {
			for (HVMapInfos vInfos : vMap.getMapInfos()) {
				if (hInfos.getSku().equalsIgnoreCase(vInfos.getSku())) {
					if (hInfos.getPreco().equals(vInfos.getPreco())) {
						break;
					} else {
						DiffMap diffMap = new DiffMap();
						diffMap.setId(hMap.getId());
						diffMap.setSku(vInfos.getSku());
						diffMap.setHorizontalPreco(hInfos.getPreco());
						diffMap.setVerticalPreco(vInfos.getPreco());
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
