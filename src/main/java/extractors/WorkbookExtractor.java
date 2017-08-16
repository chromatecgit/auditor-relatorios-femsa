package extractors;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import model.TabNamesMap;

public class WorkbookExtractor {
	public List<TabNamesMap> extractSheetNamesFrom(InputStream workbook) {
		List<TabNamesMap> maps = new LinkedList<>();
		try {
			InputSource workbookSource = new InputSource(workbook);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xml = builder.parse(workbookSource);
			xml.getDocumentElement().normalize();
			NodeList elements = xml.getElementsByTagName("sheet");
			for (int i = 0; i < elements.getLength(); i++) {
				Node node = elements.item(i);
				NamedNodeMap attributes = node.getAttributes();
				TabNamesMap map = new TabNamesMap();
				map.setId(attributes.getNamedItem("r:id").toString().replace("r:id=", "").replace("\"", ""));
				map.setName(attributes.getNamedItem("name").toString().replace("name=", "").replace("\"", ""));
				maps.add(map);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return maps;
	}
}
