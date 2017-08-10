package extractors;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.IdVsDate;
import model.MatriculaXDataUnicaKeys;
import model.MatriculaXDataUnicaResult;
import model.NumeroLinhasResult;

public class ValoresIguaisExtractor {

	private List<MatriculaXDataUnicaKeys> matriculaXDataUnicaKeys;
	private List<MatriculaXDataUnicaResult> matriculaXDataUnicaResults;
	private Map<String, Integer> columnKeys;

	private void checkDatesAndIDs(List<Path> paths) {

		for (Path path : paths) {
			String fileName = path.getName(path.getNameCount() - 1).toString();
			System.out.println("Processando: ");
			System.out.println("\t" + fileName);
			this.matriculaXDataUnicaResults.add(this.extractInfo(this.filterKeys(path.toFile(), fileName), fileName));

		}
		this.compareResults(this.matriculaXDataUnicaResults);
		System.out.println(this.matriculaXDataUnicaResults);
	}

	private void compareResults(List<MatriculaXDataUnicaResult> results) {
		System.out.println("COMPARANDO:");
		for (MatriculaXDataUnicaResult ref : results) {
			System.out.println("\tREF: " + ref.getFileName());
			for (MatriculaXDataUnicaResult comp : results) {
				if (!ref.getFileName().equals(comp.getFileName())) {
					System.out.println("\tTARGET: " + comp.getFileName());
					this.compareIdsAndDates(ref.getIdVsDateList(), comp.getIdVsDateList());
				}
			}
		}

	}

	private IdVsDate compareIdsAndDates(List<IdVsDate> idVsDateList, List<IdVsDate> idVsDateList2) {
		boolean isNew = true;
		IdVsDate currentElement = null;
		for (IdVsDate data : idVsDateList) {
			// System.out.println("\t\tComparando: " + data);
			for (IdVsDate data2 : idVsDateList2) {
				// System.out.println("\t\t...com: " + data2);
				if (data.getId().equals(data2.getId()) && data.getDate().equals(data2.getDate())) {
					isNew = false;
					break;
				} else {
					isNew = true;
					currentElement = data;
					continue;
				}
			}

		}
		if (isNew) {
			System.out.println("DIFF: " + currentElement);
			return currentElement;
		}
		return null;
	}

	private MatriculaXDataUnicaResult extractInfo(XSSFWorkbook wb, String fileName) {
		MatriculaXDataUnicaResult result = new MatriculaXDataUnicaResult();
		result.setFileName(fileName);
		for (Sheet sheet : wb) {
			for (Row row : sheet) {
				if (row.getRowNum() != 0) {
					IdVsDate idVsDate = new IdVsDate();
					for (String columnName : this.columnKeys.keySet()) {
						for (Cell cell : row) {
							if (columnName.equalsIgnoreCase("matricula")
									&& cell.getColumnIndex() == this.columnKeys.get(columnName)) {
								idVsDate.setId(this.getCellValueAsString(cell));
							} else if (columnName.equalsIgnoreCase("data")
									&& cell.getColumnIndex() == this.columnKeys.get(columnName)) {
								idVsDate.setDate(this.getCellValueAsString(cell));
							}
						}
					}
					result.getIdVsDateList().add(idVsDate);
				}
			}
		}
		// Collections.sort(result.getIdVsDateList());
		return result;
	}

	private XSSFWorkbook filterKeys(File file, String fileName) {
		try {

			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);

			Sheet sheet = wb.getSheetAt(wb.getFirstVisibleTab());
			Row row = sheet.getRow(sheet.getFirstRowNum());
			for (Cell cell : row) {
				String cellValue = this.getCellValueAsString(cell);
				if (cellValue.equalsIgnoreCase("matricula")) {
					this.columnKeys.put(cellValue, cell.getColumnIndex());
				} else if (cellValue.equalsIgnoreCase("data")) {
					this.columnKeys.put(cellValue, cell.getColumnIndex());
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

	public void getResults(List<Path> paths) {
		List<NumeroLinhasResult> rows = new ArrayList<>();
		this.checkDatesAndIDs(paths);

	}

	private String getCellValueAsString(Cell cell) {
		String result = "";
		switch (cell.getCellTypeEnum()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()).longValue());
		default:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				result = String.valueOf(cell.getDateCellValue());
			} else {
				result = "";
			}
		}
		return result;
	}
}
