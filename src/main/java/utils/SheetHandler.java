package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** 
 * See org.xml.sax.helpers.DefaultHandler javadocs 
 */
public class SheetHandler extends DefaultHandler {
	private SharedStringsTable sst;
	private String lastContents;
	private boolean nextIsString;
	private List<String> cellValueAddressList;
	private String cellValueAddress;
	
	public SheetHandler(SharedStringsTable sst) {
		this.sst = sst;
		this.cellValueAddressList = new ArrayList<>();
	}
	
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// c => cell
		if(name.equals("c")) {
			// Print the cell reference
			//System.out.print(attributes.getValue("r") + " - ");
			this.cellValueAddress = attributes.getValue("r");
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
			this.cellValueAddress += lastContents;
			cellValueAddressList.add(cellValueAddress);
			cellValueAddress = "";
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		lastContents += new String(ch, start, length);
	}

	public List<String> getCellValueAddressList() {
		return cellValueAddressList;
	}
	
	
}
