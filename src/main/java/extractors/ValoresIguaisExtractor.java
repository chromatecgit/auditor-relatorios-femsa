package extractors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.ValoresIguaisGroup;
import model.ValoresIguaisResult;

public class ValoresIguaisExtractor {

	private String[] notKeys = { "matricula", "concat", "data", "saleschannel", "cod_unico", "usuario" };
	private Map<Integer, String> docKeyMap = new HashMap<>();
	private ValoresIguaisResult result = new ValoresIguaisResult();

	public void getResults(List<Path> paths) {
		try {
			for (Path path : paths) {

				OPCPackage pkg = OPCPackage.open(path.toFile());
				XSSFWorkbook wb = new XSSFWorkbook(pkg);
				filterKeys(wb);

				String fileName = path.getName(path.getNameCount() - 1).toString();
				String keyname = "";
				
				for (Sheet sheet : wb) {
					long refNumber = sheet.getPhysicalNumberOfRows() - 1;
					for (Integer columnIndex : docKeyMap.keySet()) {
						List<String> results = new ArrayList<>();
						for (Row row : sheet) {
							if (row.getCell(columnIndex) != null) {
								if (row.getRowNum() == 0) {
									keyname = this.getCellValueAsString(row.getCell(columnIndex));
								} else {
									results.add(this.getCellValueAsString(row.getCell(columnIndex)));
								}
							}
						}
						this.calculateResults(results, keyname, refNumber);
					}
					System.out.println(result);
				}

			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void calculateResults(List<String> results, String keyname, long refNumber) {
		Collections.sort(results);
		Map<String, Long> collect = results.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		ValoresIguaisGroup valores = new ValoresIguaisGroup(keyname, collect, refNumber);
		result.getGroups().add(valores);
	}

	private void filterKeys(XSSFWorkbook wb) {
		Sheet sheet = wb.getSheetAt(wb.getFirstVisibleTab());
		Row row = sheet.getRow(sheet.getFirstRowNum());
		List<String> listNotKeys = Arrays.asList(notKeys);
		for (Cell cell : row) {
			if (!listNotKeys.contains(this.getCellValueAsString(cell).toLowerCase())) {
				docKeyMap.put(cell.getColumnIndex(), this.getCellValueAsString(cell));
			}
		}
	}

	private String getCellValueAsString(Cell cell) {
		String result = "";
		switch (cell.getCellTypeEnum()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
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
