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

	public SheetHandler(final SharedStringsTable sst, final ReportTabReadyListener listener, final ProcessStageEnum processStageEnum) {
		this.sst = sst;
		this.listener = listener;
		this.cell = new ReportCell();
		this.processStageEnum = processStageEnum;
	}

	@Override
	public void startDocument() throws SAXException {
		builder = new ReportTabBuilder();
		System.out.println("Inicio do parse");
	}

	@Override
	public void endDocument() throws SAXException {
		listener.onArrivalOf(builder.build());
		builder = null;
		System.out.println("Fim do parse");
	}
	/* MIN e MAX compreendem quais endereços uma coluna esta ocupando
			System.out.println("MIN: " + attributes.getValue("min"));
			Na ultima celula vazia a compreensao sera o MAX indice da ultima
			coluna preenchida + 1 ate o final da planilha, uns 16384
			Para pegar apenas as colunas preenchidas, usar MIN = MAX */
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if (name.equals("col")) {
			if (attributes.getValue("min").equals(attributes.getValue("max"))) {
				builder.addNumberOfColumns(Integer.parseInt(attributes.getValue("min")));
			}
		}
		if (name.equals("row")) {
			builder.addNumberOfRows(Integer.parseInt(attributes.getValue("r")));
		}
		if (processStageEnum == ProcessStageEnum.FULL) {
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
		// Clear contents cache
		lastContents = "";
	}

	public void endElement(String uri, String localName, String name) throws SAXException {
		if (nextIsString) {
			int idx = Integer.parseInt(lastContents);
			lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			nextIsString = false;
		}

		if (name.equals("v") && processStageEnum == ProcessStageEnum.FULL) {
			this.cell.setValue(lastContents);
			builder.addCell(this.cell);
			this.cell = new ReportCell();
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		lastContents += new String(ch, start, length);
	}

}
