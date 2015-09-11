package com.fayaz.xcel.logicallayer;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XLSXSheetHandler extends DefaultHandler {

	private SharedStringsTable sst;
	private String lastContents = "";
	private ArrayList<Object> values;
    private int currentIdx = -1;
    private CellType cellType;
    private int rowCounter = 0;

	//static final Logger logger = Logger.getRootLogger();
	//static final String LOG_PROPERTIES_FILE = "lib/Log4J.properties";
	static private enum CellType {
        non, num, staticText, sharedText
	};

	public XLSXSheetHandler(SharedStringsTable sst) {
		this.sst = sst;
		//initializeLogger();
		values = new ArrayList<Object>();
	}
	
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {

		// c => cell
		if (name.equals("c")) {
			// Figure out if the value is an index in the SST
			String type = attributes.getValue("t");
			//logger.info("type :"+type);
			if (type == null ) {
				cellType = CellType.num;
			} else if (type.equals("s")) {
				cellType = CellType.sharedText;
			} else if (type.equals("str")||type.equals("n")||type.equals("inlineStr")) {
				cellType = CellType.staticText;
			} else {
				cellType = CellType.non;
			}
			currentIdx++;
			values.add(currentIdx, null);
			// Clear contents cache
			lastContents = "";
		}
		if (name.equals("row")) {
			currentIdx = -1;
			rowCounter++;
		}		

	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		
		if (name.equals("row")) {
			Iterator<Object> i = values.iterator();
			String str ="";
			while(i.hasNext()){
				Object o = i.next();
				if(o!=null)
					str+=o.toString()+"|";
				else
					str+="|";
				
			}
//			logger.info("Row : "+str);
//			logger.info("Rows :"+rowCounter);
			values.clear();
			return;
		} else if (name.equals("c")) {
			cellType = CellType.non;
			return;
		} else if (name.equals("v") || name.equals("t")) {

			String val = null;

			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if (cellType == CellType.sharedText) {
				int idx;
				idx = Integer.parseInt(lastContents);
				val = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			} else if (cellType == CellType.staticText
					|| cellType == CellType.num) {
				val = lastContents;
			}

			// v => contents of a cell
			if (val != null) {
				values.remove(currentIdx);
				values.add(currentIdx, val);
			}
		}else if(name.equals("worksheet"))
			rowCounter = 0;
	}

	// Extracting the content of an element
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		/*logger.info("In characters: ");*/
		lastContents += new String(ch, start, length);
	}

//	private void initializeLogger() {
//		Properties logProperties = new Properties();
//
//		try {
//			// load our log4j properties / configuration file
////			logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
////			PropertyConfigurator.configure(logProperties);
////			logger.info("Logging initialized.");
//		} catch (IOException e) {
//			throw new RuntimeException("Unable to load logging property "
//					+ LOG_PROPERTIES_FILE);
//		}
//	}

}
