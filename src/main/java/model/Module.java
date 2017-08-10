package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import listener.ReportDocumentReadyListener;
import utils.FileRepository;
import utils.SheetHandler;

public class Module implements ReportDocumentReadyListener 
{
	protected List<ReportDocument> documents = new ArrayList<>();
	
	public void processOneSheet(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		// To look up the Sheet Name / Sheet Order / rID,
		// you need to process the core Workbook stream.
		// Normally it's of the form rId# or rSheet#
		InputStream firstSheet = r.getSheet("rId1");
		InputSource sheetSource = new InputSource(firstSheet);
		parser.parse(sheetSource);
		firstSheet.close();
	}

	public void processAllSheets(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		Iterator<InputStream> sheets = r.getSheetsData();
		while (sheets.hasNext()) {
			System.out.println("Processing new sheet:\n");
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			System.out.println("");
		}
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new SheetHandler(sst, this);
		parser.setContentHandler(handler);
		return parser;
	}
	
	protected List<ReportDocument> fetchDocuments(String[] files) {
		return this.files.add(this.getFilesFromRepository(files));
	}
	
	private ReportDocument getFileFromRepository(String key) {
		AuditorFile obtainFileMapWith = FileRepository.obtainFileMapWith(key);
		return ;
	}
	
	private ReportDocument getFilesFromRepository(String[] keys) {
		AuditorFile obtainFileMapWith = FileRepository.obtainFileMapWith(key);
		return ;
	}

	@Override
	public void onArrivalOf(ReportDocument document) {
		this.document = document;
	}
}
