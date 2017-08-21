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
import model.TabNamesMap;
import utils.ReportDocumentBuilder;
import utils.ReportTabBuilder;
import utils.SheetHandler;

public class ExcelExtractor implements ReportTabReadyListener {
	
	private ReportDocumentBuilder builder;
	private String tabName;
	private String fileName;
	private OPCPackage pkg;
	private XSSFReader reader;
	private SharedStringsTable sst;
	private XMLReader parser;

	public ExcelExtractor(String fileName, Path path) {
		this.builder = new ReportDocumentBuilder();
		this.builder.addDocumentName(fileName);
		this.fileName = fileName;
	}
	
	

	public void process(Path path, ProcessStageEnum processStageEnum) {
		try {
			SharedStringsTable sst = reader.getSharedStringsTable();
			XMLReader parser = this.fetchSheetParser(sst, processStageEnum);
							
			this.builder.setNewFlagTo(path.toString().contains("new") ? true : false);
			
			WorkbookExtractor we = new WorkbookExtractor();
			List<TabNamesMap> tabNamesMapList = we.extractSheetNamesFrom(reader.getWorkbookData());
			
			for (TabNamesMap tabData : tabNamesMapList) {
				InputStream sheet = reader.getSheet(tabData.getId());
				InputSource sheetSource = new InputSource(sheet);
				tabName = tabData.getName();
				System.out.println("FILE_NAME: " + fileName);
				System.out.println("TAB_NAME: " + tabName);
				parser.parse(sheetSource);
				sheet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processWorkbookData() {
		
	}
	

	private XMLReader fetchSheetParser(final SharedStringsTable sst, final ProcessStageEnum processStageEnum) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new SheetHandler(sst, this, processStageEnum);
		parser.setContentHandler(handler);
		return parser;
	}

	@Override
	public void onArrivalOf(final ReportTabBuilder tabBuilder) {
		this.builder.addReportTab(tabBuilder.build(), tabName);
	}
	
	public ReportDocumentBuilder getDocumentBuilder() {
		return this.builder;
	}

}
