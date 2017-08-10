package extractors;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class VelhoNovoExtractor {
	
	private int diffNumber = 0;
	
	private String oldName = "";
	private String newName = "";

	public VelhoNovoExtractor() {
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
					
					OPCPackage pkgNew = OPCPackage.open(targetPaths.get(i).toFile());
					XSSFWorkbook wbNew = new XSSFWorkbook(pkgNew);
					newName = targetPaths.get(i).getName(targetPaths.get(i).getNameCount() - 1).toString();
					
					for (Sheet oldSheet : wbOld) {
						for (Sheet newSheet : wbNew) {
							if (oldSheet.getSheetName().equals(newSheet.getSheetName())) {
								this.sheetComparator(oldSheet, newSheet);
							}
						}
					}
					
				}
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
				if (oldRow.getRowNum() == newRow.getRowNum()) {
					this.rowComparator(oldRow, newRow);
				}
			}
		}
	}
	
	private void rowComparator(Row oldRow, Row newRow) {
//		System.out.println("OldRow:" + oldRow.getRowNum() + "||NewRow " + newRow.getRowNum());
		if (oldRow.getPhysicalNumberOfCells() != newRow.getPhysicalNumberOfCells()) {
//			System.out.println("O número de celulas das duas é diferente");
//			System.out.println("\tDiferenças:");
			this.findDifferenceBetween(oldRow, newRow);
		} else {
//			System.out.println("O número de celulas das duas é igual");
//			System.out.println("\tProcurando por diferenças...");
			this.findDifferenceBetween(oldRow, newRow);
		}
	}
	
	private void findDifferenceBetween(Row oldRow, Row newRow) {
		Set<String> values = new HashSet<>();
		for (Cell cell : oldRow) {
			//System.out.println("\t\tOldRow Value: " + this.getCellValueAsString(cell));
			values.add(this.getCellValueAsString(cell));
		}
		
		for (Cell cell : newRow) {
			//System.out.println("\t\tNewRow Value: " + this.getCellValueAsString(cell));
			String cellValue = this.getCellValueAsString(cell);
			if (values.add(cellValue)) {
				diffNumber++;
				Cell oldCell = oldRow.getCell(cell.getColumnIndex());
				System.out.println("\t\t" + oldName);
				System.out.println("\t\t\tMudanças na célula antiga " + oldCell.getAddress() + " com valor " + this.getCellValueAsString(oldCell));
				System.out.println("\t\t" + newName);
				System.out.println("\t\t\tMudanças na célula " + cell.getAddress() + " com valor " + cellValue);
				System.out.println("\n");
			}
		}
		
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
