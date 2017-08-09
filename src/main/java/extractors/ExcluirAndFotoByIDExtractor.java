package extractors;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import enums.SearchTypeEnum;
import model.ResultExcel;

public class ExcluirAndFotoByIDExtractor {
	
	private String verificationType;
	private List<String> idsExcluir;
	private List<String> idsFotos;
	private List<ResultExcel> rowsWithIssues;
	private int fotoColumnAddress;
	private int idColumnAddress;
	
	public ExcluirAndFotoByIDExtractor() {
		this.idsExcluir = new ArrayList<>();
		this.idsFotos = new ArrayList<>();
		this.rowsWithIssues = new ArrayList<>();
	}
	
	private void extractFromReference(File refFile) {
		try {
			
			OPCPackage pkg = OPCPackage.open(refFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			for (Sheet sheet : wb) {
//				String name = sheet.getSheetName();
//				System.out.println("PLANILHA: " + name);
		        for (Row row : sheet) {
		            for (Cell cell : row) {
		            	String cellAddress = cell.getAddress().formatAsString();
//		            	sb.append(" || LINHA: " + cell.getRowIndex());
		            	switch (cell.getCellTypeEnum()) {
			            	case STRING:
//			            		System.out.println("\t\tVALOR TXT: " + cell.getStringCellValue());
			            		columnAddressTriage(cellAddress, cell.getStringCellValue());
			            		break;
			            	case NUMERIC:
//			            		System.out.println("\t\tVALOR NUM: " + BigDecimal.valueOf(cell.getNumericCellValue()).longValue());
			            		columnAddressTriage(cellAddress, String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()).longValue()));
			            		break;
//			            	case BOOLEAN:
//			            		System.out.println("\t\tVALOR BOL: " + cell.getBooleanCellValue());
//			            		break;
			            	case BLANK:
			            		//System.out.println("\t\tVALOR: " + "blank");
			            		break;
							default:
								if (HSSFDateUtil.isCellDateFormatted(cell)) {
//									System.out.println("\t\tVALOR: " + cell.getDateCellValue());
								} else {
									System.out.println("VALOR: nao identificado");
								}
								break;
		            	}
		            }
		        }
		        
		    }
			wb.close();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		            for (Cell cell : row) {
		            	if (cell.getAddress().getColumn() == idColumnAddress) {
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
	            for (Cell cell : row) {
	            	if (cell.getCellTypeEnum().equals(CellType.STRING)
	            			&& cell.getStringCellValue().toLowerCase().contains("matricula")) {
	            		idColumnAddress = cell.getAddress().getColumn();
	            	} else if (cell.getCellTypeEnum().equals(CellType.STRING)
	            			&& cell.getStringCellValue().toLowerCase().contains("foto")) {
	            		fotoColumnAddress = cell.getAddress().getColumn();
	            	}
	            }
	        }
	    }
	}
	
	private boolean cellValueTriage(Row row, String cellValue) {
		for (String idFotos : idsFotos) {
			if (idFotos.equals(cellValue)) {
				Cell cell = row.getCell(fotoColumnAddress);
				if (cell != null && !getCellValueAsLowerCaseString(cell).contains("sim")) {
					ResultExcel result = new ResultExcel();
					result.setId(cellValue);
					result.setSearchType(SearchTypeEnum.FOTO);
					result.setTabName(row.getSheet().getSheetName());
					this.rowsWithIssues.add(result);
				}
			}
		}

		return false;
	}
	
	private String getCellValueAsLowerCaseString(final Cell cell) {
		String result = "";
		switch (cell.getCellTypeEnum()) {
	    	case STRING:
	    		result = cell.getStringCellValue();
	    		break;
	    	case NUMERIC:
	    		result = String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()).longValue());
	    		break;
	    	case BLANK:
	    		result = "";
	    		break;
			default:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					result = String.valueOf(cell.getDateCellValue());
				} else {
					result = "";
				}
				break;
		}
		return result.toLowerCase();
	}
	
	private void columnAddressTriage(final String columnId, final String value) {
		if (columnId.startsWith("A")) {
			this.idsExcluir.add(value);
		} else if (columnId.startsWith("B")) {
			this.idsFotos.add(value);
		}
	}
	
	public void getResults(File file, List<Path> paths, String verificationType) {
		this.verificationType = verificationType;
		this.extractFromReference(file);
		for (Path path : paths) {
			System.out.println("Processando: ");
			System.out.println("\t" + path.getName(path.getNameCount() - 1));
			this.extractFromTarget(path.toFile());
		}
	}
}
