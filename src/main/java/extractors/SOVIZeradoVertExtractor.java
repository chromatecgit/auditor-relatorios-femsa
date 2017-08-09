package extractors;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import enums.SearchTypeEnum;
import model.ResultExcel;

public class SOVIZeradoVertExtractor {
	
	private String verificationType;
	private List<ResultExcel> rowsWithIssues;
	private int soviVertColumnAddress;
	private int idColumnAddress;
	
	public SOVIZeradoVertExtractor() {
		this.rowsWithIssues = new ArrayList<>();
	}
	
	private void extractFromTarget(File refFile) {
		try {
			
			OPCPackage pkg = OPCPackage.open(refFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			this.filterKeyColumns(wb);

			for (Sheet sheet : wb) {
				String sheetName = sheet.getSheetName();
				System.out.println("\t\tExtraindo da aba:" + sheetName);
		        for (Row row : sheet) {
		        	if (row.getRowNum() != 0) {
			            for (Cell cell : row) {
			            	if (cell.getAddress().getColumn() == soviVertColumnAddress) {
				            	switch (cell.getCellTypeEnum()) {
					            	case STRING:
					            		cellValueTriage(row, cell.getStringCellValue());
					            		break;
					            	case NUMERIC:
					            		cellValueTriage(row, String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()).longValue()));
					            		break;
					            	case BLANK:
					            		break;
									default:
										break;
				            	}
			            	}
			            }
		        	}
		        }
		    }
			if (rowsWithIssues.isEmpty()) {
				System.out.println("\tDOCUMENTO OK!");
			} else {
				System.out.println("\t" + rowsWithIssues);
			}
//			wb.sheetIterator()
			wb.close();
			this.rowsWithIssues = new ArrayList<>();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void filterKeyColumns(XSSFWorkbook wb) {
		for (Sheet sheet : wb) {
	        for (Row row : sheet) {
	        	if (row.getRowNum() == 0) {
		            for (Cell cell : row) {
		            	if (cell.getCellTypeEnum().equals(CellType.STRING)
		            			&& cell.getStringCellValue().toLowerCase().contains("matricula")) {
		            		idColumnAddress = cell.getAddress().getColumn();
		            	} else if (cell.getCellTypeEnum().equals(CellType.STRING)
		            			&& cell.getStringCellValue().toLowerCase().contains("sovi")) {
		            		soviVertColumnAddress = cell.getAddress().getColumn();
		            	}
		            }
	        	} else {
	        		break;
	        	}
	        }
	        break;
	    }
	}
	
	private boolean cellValueTriage(Row row, String cellValue) {
		System.out.println("\t\t\tVALOR: " + cellValue);
		if (Integer.valueOf(cellValue) == 0) {
			Cell cell = row.getCell(idColumnAddress);
			if (cell != null) {
				ResultExcel result = new ResultExcel();
				result.setId(this.getCellValueAsString(cell));
				result.setSearchType(SearchTypeEnum.SOVI_ZERADO_VERT);
				result.setTabName(row.getSheet().getSheetName());
				this.rowsWithIssues.add(result);
			}
		}

		return false;
	}
	
	private String getCellValueAsString(Cell cell) {
		switch(cell.getCellTypeEnum()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				return String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()).longValue());
			default:
				return "";
		}
	}
	
	public void getResults(List<Path> paths, String verificationType) {
		this.verificationType = verificationType;
		for (Path path : paths) {
			System.out.println("Processando: ");
			System.out.println("\t" + path.getName(path.getNameCount() - 1));
			this.extractFromTarget(path.toFile());
		}
	}
}
