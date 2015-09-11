package com.fayaz.xcel.logicallayer;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WorkBookParser extends DefaultHandler{
	
	private HashMap<String,String> sheetNameRelIdMap = new HashMap<String,String>();	

	public WorkBookParser(HashMap<String, String> sheetNameRelIdMap2) {
		this.sheetNameRelIdMap = sheetNameRelIdMap2;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(qName.equals("sheet")){			
			sheetNameRelIdMap.put(attributes.getValue("name"), attributes.getValue("r:id"));			
		}
	}
	
}
