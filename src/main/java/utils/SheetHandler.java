package utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import enums.ProcessStageEnum;
import interfaces.ReportTabBuilder;
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
	private boolean recordValue;
	

	public SheetHandler(final SharedStringsTable sst, final ReportTabReadyListener listener, final ProcessStageEnum processStageEnum) {
		this.sst = sst;
		this.listener = listener;
		this.cell = new ReportCell();
		this.processStageEnum = processStageEnum;
		this.recordValue = true;
	}
	

	@Override
	public void startDocument() throws SAXException {
		System.out.println("Inicio do parse");
	}

	@Override
	public void endDocument() throws SAXException {
		this.listener.onArrivalOf(this.builder);
		this.builder = null;
		System.out.println("Fim do parse");
	}

	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		switch (processStageEnum) {
			case DIMENSIONS :
				this.getDimensions(name, attributes);
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
			}
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
			if (NumberUtils.isCreatable(lastContents) && lastContents.contains(".")) {
				BigDecimal t = new BigDecimal(lastContents).setScale(2, BigDecimal.ROUND_HALF_UP);
				lastContents = t.toString();
			}
			this.cell.setValue(lastContents);
			this.builder.addCell(this.cell);
			this.cell = new ReportCell();
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		lastContents += new String(ch, start, length);
	}

}
