package extractors;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import enums.ProcessStageEnum;
import listener.ReportTabReadyListener;
import model.ReportDocument;
import model.ReportTab;
import model.TabNamesMap;
import utils.ReportDocumentBuilder;
import utils.SheetHandler;

public class ExcelExtractor implements ReportTabReadyListener {
	
	private ReportDocumentBuilder builder;
	private String tabName;

	public ExcelExtractor(String fileName) {
		this.builder = new ReportDocumentBuilder();
		this.builder.addDocumentName(fileName);
	}

	public void process(Path path, ProcessStageEnum processStageEnum) {
		try {
			OPCPackage pkg = OPCPackage.open(path.toFile());
			XSSFReader reader = new XSSFReader(pkg);
			SharedStringsTable sst = reader.getSharedStringsTable();

			XMLReader parser = fetchSheetParser(sst, processStageEnum);

			// To look up the Sheet Name / Sheet Order / rID,
			// you need to process the core Workbook stream.
			// Normally it's of the form rId# or rSheet#
			WorkbookExtractor we = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = we.extractSheetNamesFrom(reader.getWorkbookData());
			for (TabNamesMap tabData : tabNamesMapList) {
				InputStream sheet = reader.getSheet(tabData.getId());
				InputSource sheetSource = new InputSource(sheet);
				tabName = tabData.getName();
				System.out.println("\tTAB_NAME: " + tabName);
				parser.parse(sheetSource);
				sheet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void processAllSheets(String filename) {
//		try {
//			OPCPackage pkg = OPCPackage.open(filename);
//			XSSFReader r = new XSSFReader(pkg);
//			SharedStringsTable sst = r.getSharedStringsTable();
//
//			XMLReader parser = fetchSheetParser(sst);
//
//			Iterator<InputStream> sheets = r.getSheetsData();
//			while (sheets.hasNext()) {
//				System.out.println("Processing new sheet:\n");
//				InputStream sheet = sheets.next();
//				InputSource sheetSource = new InputSource(sheet);
//				parser.parse(sheetSource);
//				sheet.close();
//				System.out.println("");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private XMLReader fetchSheetParser(final SharedStringsTable sst, final ProcessStageEnum processStageEnum) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new SheetHandler(sst, this, processStageEnum);
		parser.setContentHandler(handler);
		return parser;
	}

	@Override
	public void onArrivalOf(final ReportTab tab) {
		this.builder.addReportTab(tab, tabName);
	}
	
	public ReportDocument getDocument() {
		return this.builder.build();
	}

}
