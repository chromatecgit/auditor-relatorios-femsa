package extractors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.NumeroLinhasResult;
import utils.MyLogPrinter;

public class NumeroLinhasExtractor {

	private List<NumeroLinhasResult> checkNumberOfRows(File refFile, String fileName) {
		try {

			OPCPackage pkg = OPCPackage.open(refFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			List<NumeroLinhasResult> results = new ArrayList<>();
			for (Sheet sheet : wb) {
				NumeroLinhasResult linhasResult = new NumeroLinhasResult();
				linhasResult.setRowQnty(sheet.getPhysicalNumberOfRows());
				linhasResult.setTabName(sheet.getSheetName());
				linhasResult.setFileName(fileName);
				results.add(linhasResult);
			}
			
			wb.close();
			return results;

		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void getResults(List<Path> paths) {
		List<NumeroLinhasResult> rows = new ArrayList<>();
		for (Path path : paths) {
			String fileName = path.getName(path.getNameCount() - 1).toString();
			System.out.println("Processando: ");
			System.out.println("\t" + fileName);
			rows.addAll(this.checkNumberOfRows(path.toFile(), fileName));
		}
		
		MyLogPrinter.printCollection(rows, "AuditorNumeroLinhas");
	}
}
