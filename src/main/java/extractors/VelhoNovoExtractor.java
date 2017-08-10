package extractors;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import enums.MessageHierarchyEnum;
import utils.MyLogPrinter;

public class VelhoNovoExtractor {

	private int diffNumber = 0;
	private Map<Integer, String> oldColumnKeys;
	private Map<Integer, String> newColumnKeys;

	private String oldName = "";
	private String newName = "";

	public VelhoNovoExtractor() {
		this.oldColumnKeys = new HashMap<>();
		this.newColumnKeys = new HashMap<>();
	}

	private void filterKeys(XSSFWorkbook wb, boolean isNew) {
		Sheet sheet = wb.getSheetAt(wb.getFirstVisibleTab());
		Row row = sheet.getRow(sheet.getFirstRowNum());
		if (isNew) {
			for (Cell cell : row) {
				newColumnKeys.put(cell.getColumnIndex(), this.getCellValueAsString(cell));
			}
		} else {
			for (Cell cell : row) {
				oldColumnKeys.put(cell.getColumnIndex(), this.getCellValueAsString(cell));
			}
		}
	}

	public void getResults(List<Path> refPaths, List<Path> targetPaths) {
		try {
			Collections.sort(refPaths);
			Collections.sort(targetPaths);
			if (refPaths.size() == targetPaths.size()) {

				for (int i = 0; i < refPaths.size(); i++) {

					OPCPackage pkgOld = OPCPackage.open(refPaths.get(i).toFile());
					XSSFWorkbook wbOld = new XSSFWorkbook(pkgOld);
					oldName = refPaths.get(i).getName(refPaths.get(i).getNameCount() - 1).toString();
					this.filterKeys(wbOld, false);
					OPCPackage pkgNew = OPCPackage.open(targetPaths.get(i).toFile());
					XSSFWorkbook wbNew = new XSSFWorkbook(pkgNew);
					newName = targetPaths.get(i).getName(targetPaths.get(i).getNameCount() - 1).toString();
					this.filterKeys(wbOld, true);
					for (Sheet oldSheet : wbOld) {
						for (Sheet newSheet : wbNew) {
							if (!this.findDifferencesInColumns()) {
								if (oldSheet.getSheetName().equals(newSheet.getSheetName())) {
									this.sheetComparator(oldSheet, newSheet);
								}
							}
						}
					}
					wbOld.close();
					wbNew.close();
				}
				MyLogPrinter.printBuiltMessage("AuditorVelhoNovo");
				if (diffNumber == 0) {
					System.out.println("Os documentos não possuem diferenças");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sheetComparator(Sheet oldSheet, Sheet newSheet) {
		for (Row oldRow : oldSheet) {
			for (Row newRow : newSheet) {
				if (rowKey(oldRow).equals(rowKey(newRow))) {
					this.rowComparator(oldRow, newRow);
				}
			}
		}
	}

	private String rowKey(Row row) {
		return this.getCellValueAsString(row.getCell(row.getFirstCellNum()));
	}

	private void rowComparator(Row oldRow, Row newRow) {
		
		for (Integer oldIndex : oldColumnKeys.keySet()) {
			Cell oldCell = oldRow.getCell(oldIndex);
			Cell newCell = newRow.getCell(oldIndex);
			this.compareCells(oldCell, newCell, oldColumnKeys.get(oldIndex));
		}

	}

	private void compareCells(Cell oldCell, Cell newCell, String field) {
		if (!this.getCellValueAsString(oldCell).equals(this.getCellValueAsString(newCell))) {
			diffNumber++;
			MyLogPrinter.addBuiltMessageTitle(field, "");
			MyLogPrinter.addToBuiltMessageWithLevel(MessageHierarchyEnum.LEVEL_2, oldName);
			MyLogPrinter.addToBuiltMessageWithLevel(MessageHierarchyEnum.LEVEL_3,
					"Célula antiga " + oldCell.getAddress() + " = " + this.getCellValueAsString(oldCell));
			MyLogPrinter.addToBuiltMessageWithLevel(MessageHierarchyEnum.LEVEL_2, newName);
			MyLogPrinter.addToBuiltMessageWithLevel(MessageHierarchyEnum.LEVEL_3,
					"Célula nova " + newCell.getAddress() + "= " + this.getCellValueAsString(newCell));
		}
	}

	private boolean findDifferencesInColumns() {
		Set<String> values = new HashSet<>();
		boolean isNew = false;
		for (String value : oldColumnKeys.values()) {
			values.add(value);
		}

		for (String value : newColumnKeys.values()) {
			if (values.add(value)) {
				isNew = true;
				System.out.println("Nova coluna: " + value);
			}
		}
		return isNew;
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
