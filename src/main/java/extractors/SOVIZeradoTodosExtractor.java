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

public class SOVIZeradoTodosExtractor {
	
	private List<ResultExcel> rowsWithIssues;
	private int startColumnAddress;
	private int idColumnAddress;
	private double somaSovi;
	
	public SOVIZeradoTodosExtractor() {
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
		        	//Ignorar a linha de titulos
		        	if (row.getRowNum() != 0) {
			            for (Cell cell : row) {
			            	if (cell.getAddress().getColumn() >= startColumnAddress) {
				            	switch (cell.getCellTypeEnum()) {
					            	case NUMERIC:
					            		cellValueTriage(row, cell.getNumericCellValue());
					            		break;
									default:
										break;
				            	}
			            	}
			            }
			            System.out.println("SOMA SOVI PARA "+ row.getCell(idColumnAddress) + "é " + somaSovi);
			            if (somaSovi <= 0) {
			            	ResultExcel result = new ResultExcel();
			            	result.setId(getCellValueAsString(row.getCell(idColumnAddress)));
			            	result.setSearchType(SearchTypeEnum.SOVI_ZERADO_TODOS);
			            	result.setTabName(sheet.getSheetName());
			            	this.rowsWithIssues.add(result);
			            }
			            somaSovi = 0.0;
		        	}
		        }
		    }
			if (rowsWithIssues.isEmpty()) {
				System.out.println("\tDOCUMENTO OK!");
			} else {
				System.out.println("\t" + rowsWithIssues);
			}
			wb.close();
			this.rowsWithIssues = new ArrayList<>();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 
	private void filterKeyColumns(XSSFWorkbook wb) {
		for (Sheet sheet : wb) {
	        for (Row row : sheet) {
	            for (Cell cell : row) {
	            	if (cell.getCellTypeEnum().equals(CellType.STRING)
	            			&& cell.getStringCellValue().toLowerCase().contains("saleschannel")) {
	            		startColumnAddress = cell.getAddress().getColumn() + 1;
	            	}
	            	if (cell.getCellTypeEnum().equals(CellType.STRING)
	            			&& cell.getStringCellValue().toLowerCase().contains("matricula")) {
	            		idColumnAddress = cell.getAddress().getColumn();
	            	}
	            }
	        }
	    }
	}
	
	private boolean cellValueTriage(Row row, double cellValue) {
		//SOVI_ZERADO_TODOS
		somaSovi = somaSovi + cellValue;

		return false;
	}
	
	public void getResults(File file, List<Path> paths, String verificationType) {
		for (Path path : paths) {
			System.out.println("Processando: ");
			System.out.println("\t" + path.getName(path.getNameCount() - 1));
			this.extractFromTarget(path.toFile());
		}
	}
}
