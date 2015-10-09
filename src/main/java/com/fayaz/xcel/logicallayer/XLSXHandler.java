package com.fayaz.xcel.logicallayer;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XLSXHandler implements BaseHandler{
	
	static Logger logger = LoggerFactory.getLogger(XLSXHandler.class);
	
	private static final String SAX_PARSER_DRIVER = "org.apache.xerces.parsers.SAXParser";
	
	private List<String> sheetList = new ArrayList<String>();
	private HashMap<String,String> sheetNameRelIdMap = new HashMap<String,String>();
	private XMLReader sheetParser;
	private XMLReader workBookParser;
	private XSSFReader reader;
	private boolean sheetRelIdMapStatus = false;
	private boolean sheetPopulatedFlag =false;
	private String file = "D:\\Backup From Old Lap\\Folders\\xlsxtodb_test_files\\export.xlsx";
	
	public XLSXHandler(String file) {
		this.file = file;		
		try {
			OPCPackage pkg = OPCPackage.open(file);
			reader = new XSSFReader(pkg);
			SharedStringsTable sst = reader.getSharedStringsTable();
			sheetParser = fetchSheetParser(sst);
			workBookParser = fetchWorkBookParser();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (OpenXML4JException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst)
			throws SAXException {
		XMLReader parser = getParserDriver();
		ContentHandler handler = new XLSXSheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}
	
	public XMLReader fetchWorkBookParser()
			throws SAXException {
		XMLReader parser = getParserDriver();
		ContentHandler handler = new WorkBookParser(sheetNameRelIdMap);
		parser.setContentHandler(handler);
		return parser;
	}
	
	public void setSheetNames(){
		XSSFWorkbook workBook;
		try {
			workBook = new XSSFWorkbook(file);
			for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
				sheetList.add(workBook.getSheetName(i));
			}
			workBook.close();
			sheetPopulatedFlag = true;

		} catch (IOException e) {

			logger.error(e.getMessage());
		}
	}
	
	public List<String> getSheetNames() {
		logger.info("Inside getSheetNames");
		logger.info("File to Process :"+file);
		if(!sheetPopulatedFlag){
			if(file!=null)
				setSheetNames();
			else{
				//add code here to raise FileNotSetException TODO
			} 
		}			
		return sheetList;
	}
	

	public void parseAllSheets() {

		try {
			Iterator<InputStream> sheets = reader.getSheetsData();
			while (sheets.hasNext()) {
				InputStream sheet = sheets.next();
				InputSource sheetSource = new InputSource(sheet);
				sheetParser.parse(sheetSource);
				sheet.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}

	}
	
	public void parseSheetWithNames(List<String> nameList){
		Iterator<String> iter = nameList.iterator();
		while(iter.hasNext()){
			parseSheetWithName(iter.next());
		}
	}
	
	public void parseSheetWithName(String name) {

		try {
			if (!sheetRelIdMapStatus) {				
				generateSheetRelIdMap();
			}
			testMapMethod(sheetNameRelIdMap);
			String relId = sheetNameRelIdMap.get(name);
			if(relId == null){
				System.out.println("Sheet with name not exists");
				return;
			}
				
			InputStream i = reader.getSheet(relId);
			InputSource is = new InputSource(i);
			sheetParser.parse(is);
			i.close();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}

	}
	
	public XMLReader getParserDriver() throws SAXException{
		return XMLReaderFactory.createXMLReader(SAX_PARSER_DRIVER);
	}
	
	public void generateSheetRelIdMap() throws InvalidFormatException, IOException, SAXException{
		sheetRelIdMapStatus = true;
		if (reader != null) {
			InputStream i = reader.getWorkbookData();
			InputSource is = new InputSource(i);
			workBookParser.parse(is);
			i.close();
		}else{
			//Add code here for reader check TODO
		}
	}
	
	//test method..delete in production TODO
	public void testMapMethod(HashMap<String,String> map){
		
		System.out.println("Printing Map:");
		Iterator<Map.Entry<String,String>> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, String> pair = iter.next();
			System.out.println(pair.getKey()+" "+pair.getValue());
		}
	}
	
}
