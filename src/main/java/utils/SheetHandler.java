package utils;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import enums.ProcessStageEnum;
import listener.ReportTabReadyListener;
import model.ReportCell;

/**
 * See org.xml.sax.helpers.DefaultHandler javadocs
 */
public class SheetHandler extends DefaultHandler {

	private SharedStringsTable sst;
	private String lastContents;
	private boolean nextIsString;
	private ReportTabBuilder builder;
	private ReportTabReadyListener listener;
	private ReportCell cell;
	private ProcessStageEnum processStageEnum;
	private int pagingSize;
	private int lastVisitedLine;
	private boolean recordValue;

	public SheetHandler(final SharedStringsTable sst, final ReportTabReadyListener listener, final ProcessStageEnum processStageEnum) {
		this.sst = sst;
		this.listener = listener;
		this.cell = new ReportCell();
		this.processStageEnum = processStageEnum;
		this.recordValue = true;
	}
	
	public SheetHandler(final SharedStringsTable sst, final ReportTabReadyListener listener, final ProcessStageEnum processStageEnum, final int lastVisitedLine) {
		this.sst = sst;
		this.listener = listener;
		this.cell = new ReportCell();
		this.processStageEnum = processStageEnum;
		this.lastVisitedLine = lastVisitedLine;
		this.recordValue = true;
	}

	@Override
	public void startDocument() throws SAXException {
		builder = new ReportTabBuilder();
		this.pagingSize = processStageEnum.getLinesToBeRead();
		System.out.println("Inicio do parse");
	}

	@Override
	public void endDocument() throws SAXException {
		this.builder.setLastLineIndex(lastVisitedLine);
		this.listener.onArrivalOf(this.builder);
		this.builder = null;
		System.out.println("Fim do parse");
	}

	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		switch (processStageEnum) {
			case DIMENSIONS :
				this.getDimensions(name, attributes);
				break;
			case PAGING_100 :
				this.getDimensions(name, attributes);
				this.getCellAddressWithLimit(name, attributes);
				break;
			default :
				this.getDimensions(name, attributes);
				this.getCellAddress(name, attributes);
				break;
		}
	
		lastContents = "";
	}
	
	private void getDimensions(String name, Attributes attributes) {
		if (name.equals("col")) {
			if (attributes.getValue("min").equals(attributes.getValue("max"))) {
				builder.addNumberOfColumns(Integer.parseInt(attributes.getValue("min")));
			}
		}
		if (name.equals("row")) {
			builder.addNumberOfRows(Integer.parseInt(attributes.getValue("r")));
		}
	}
	
	private void getCellAddress(String name, Attributes attributes) {
		if (name.equals("c")) {
			this.cell.setAddress(attributes.getValue("r"));
			String cellType = attributes.getValue("t");
			if (cellType != null && cellType.equals("s")) {
				nextIsString = true;
			} else {
				nextIsString = false;
			}
		}
	}
	
	private void getCellAddressWithLimit(String name, Attributes attributes) {
		if (name.equals("c")) {
			if (this.builder.getLastLineIndex() <= this.pagingSize && this.checkAddress(attributes.getValue("r"))) {
				this.recordValue = true;
				this.cell.setAddress(attributes.getValue("r"));
			} else {
				this.recordValue = false;
			}
			String cellType = attributes.getValue("t");
			if (cellType != null && cellType.equals("s")) {
				nextIsString = true;
			} else {
				nextIsString = false;
			}
		}
	}
	
	private boolean checkAddress(final String address) {
		int addressLine = Integer.valueOf(address.replaceAll("\\D+", ""));
		if (addressLine > lastVisitedLine) {
			return true;
		} else {
			this.lastVisitedLine++;
			return false;
		}
	}

	public void endElement(String uri, String localName, String name) throws SAXException {
		if (nextIsString) {
			int idx = Integer.parseInt(lastContents);
			lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			nextIsString = false;
		}
		if (this.recordValue) {
			this.getCellValue(name);
		}
	}
	
	private void getCellValue(String name) {
		if (name.equals("v")) {
			this.cell.setValue(lastContents);
			this.builder.addCell(this.cell);
			this.cell = new ReportCell();
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		lastContents += new String(ch, start, length);
	}

}
