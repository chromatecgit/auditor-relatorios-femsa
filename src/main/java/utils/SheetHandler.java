package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import listener.ReportDocumentReadyListener;
import model.ReportDocument;

/** 
 * See org.xml.sax.helpers.DefaultHandler javadocs 
 */
public class SheetHandler extends DefaultHandler {
	private SharedStringsTable sst;
	private String lastContents;
	private boolean nextIsString;
	private ReportDocument reportDocument;
	private ReportDocumentReadyListener listener;
	
	public SheetHandler(SharedStringsTable sst, ReportDocumentReadyListener listener) {
		this.sst = sst;
		this.listener = listener;
	}
	
	@Override
	public void startDocument() throws SAXException {
		//TODO: instanciar o objeto Spreadsheet
		reportDocument = new ReportDocument();
		System.out.println("Inicio do parse");
	}
	
	@Override
	public void endDocument() throws SAXException {
		//TODO: Chamar uma função de callback, passando o objeto resultante do parse
		this.sendDocument(reportDocument);
		System.out.println("Fim do parse");
	}
	
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (name.equals("col")) {
			// MIN e MAX compreendem quais endereços de coluna uma coluna pode ocupar
			System.out.println("MIN: " + attributes.getValue("min"));
			// Na ultima celula vazia a compreensao sera o MAX indice da ultima coluna preenchida + 1 ate o final da planilha, uns 16384
			// Para pegar apenas as colunas preenchidas, usar MIN = MAX
			System.out.println("MAX: " + attributes.getValue("max"));
		}
		if(name.equals("row")) {
			System.out.println(attributes.getValue("r"));
		}
		// c => cell
		if(name.equals("c")) {
			// Print the cell reference
			//System.out.print(attributes.getValue("r") + " - ");
			// Figure out if the value is an index in the SST
			String cellType = attributes.getValue("t");
			if(cellType != null && cellType.equals("s")) {
				nextIsString = true;
			} else {
				nextIsString = false;
			}
		}
		// Clear contents cache
		lastContents = "";
	}
	
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// Process the last contents as required.
		// Do now, as characters() may be called more than once
		if(nextIsString) {
			int idx = Integer.parseInt(lastContents);
			lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			nextIsString = false;
		}

		// v => contents of a cell
		// Output after we've seen the string contents
		if(name.equals("v")) {
			//System.out.println(lastContents);
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		lastContents += new String(ch, start, length);
	}
	
	private void sendDocument(ReportDocument document) {
		this.listener.onArrivalOf(document);
	}
}
	
	
