package extractors;

import java.io.InputStream;

import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import enums.ProcessStageEnum;
import interfaces.ReportTabBuilder;
import listener.ReportTabReadyListener;
import model.ReportTab;
import model.TabNamesMap;
import utils.ReportDocumentBuilder;
import utils.SheetHandler;

public class ExcelExtractor implements ReportTabReadyListener {
	
	private ReportTabBuilder builder;
	private String tabName;
	private String fileName;
	private ReportTab tab;

	public ExcelExtractor(final String fileName, final ReportTabBuilder builder) {
		this.builder = builder;
		this.builder.addDocumentName(fileName);
		this.fileName = fileName;
	}

	public void process(XSSFReader reader, TabNamesMap tabMap, ProcessStageEnum processStageEnum) {
		try {
			
			SharedStringsTable sst = reader.getSharedStringsTable();
			XMLReader parser = this.fetchSheetParser(sst, processStageEnum);
							
			InputStream sheet = reader.getSheet(tabMap.getId());
			InputSource sheetSource = new InputSource(sheet);
			tabName = tabMap.getName();
			
			this.builder.addTabName(tabName);
			
			System.out.println("FILE_NAME: " + fileName);
			System.out.println("TAB_NAME: " + tabName);
			parser.parse(sheetSource);
			sheet.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private XMLReader fetchSheetParser(final SharedStringsTable sst, final ProcessStageEnum processStageEnum) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new SheetHandler(sst, this, processStageEnum, this.builder);
		parser.setContentHandler(handler);
		return parser;
	}

	@Override
	public void onArrivalOf(final ReportTabBuilder tabBuilder) {
		this.tab = tabBuilder.build();
	}
	
	public ReportTab getProcessedTab() {
		return this.tab;
	}

}
