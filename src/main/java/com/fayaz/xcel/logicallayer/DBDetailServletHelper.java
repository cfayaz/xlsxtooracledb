package com.fayaz.xcel.logicallayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DBDetailServletHelper {
	
	static Logger logger = LoggerFactory.getLogger(DBDetailServletHelper.class);
	private static final String UPLOAD_DIRECTORY = "D:\\eclipse";
	private static final String CONN_FILE ="conn.xml";
	private static final String ROOT_NODE = "connections";
	private static final String CONN_NODE = "connection";
	private static final String CONN_IDENTIFIER = "name";
	private static final String CONN_TYPE = "type";
	
	private String message;//To transfer messages to UI
	


	public List<String> getConnectionNames(){
		List<String> connList = new ArrayList<String>();	
		
		try{
			Node root = getDocRootNode();
			if(root!=null){
				NodeList nList = root.getChildNodes();
				for(int i=0;i<nList.getLength();i++){
					Node node = nList.item(i);
					if(node.getNodeType()==Node.ELEMENT_NODE){
						Element element = (Element)node;						
						connList.add(element.getAttribute(CONN_IDENTIFIER));
					}						
				}
			}else{
				logger.error("Root Of Connections Not Found");
			}
			
			
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		
		return connList;
	}
	
	public HashMap<String,String> getConnDetails(String connId){
		HashMap<String,String> connMap = new HashMap<String, String>();
		Node root = getDocRootNode();
		if(root!=null){
			NodeList nList = root.getChildNodes();
			for(int i = 0;i<nList.getLength();i++){
				Node node = nList.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE){
					Element element = (Element)node;
					if(element.getAttribute(CONN_IDENTIFIER).equals(connId)){
						if(element.getAttribute(CONN_TYPE).equals("ORCL")){
							
							connMap = getDetailsFromOracleConn(element.getChildNodes());
						}
						//Add for other db types here.. TODO
					}
				}
			}
			
		}
		return connMap;
	}
	
	private HashMap<String, String> getDetailsFromOracleConn(NodeList nList) {
		HashMap<String,String> connMap = new HashMap<String, String>();
		for(int i=0;i<nList.getLength();i++){
			Node node = nList.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node;
				connMap.put(element.getNodeName(), element.getTextContent());				
			}			
		}
		return connMap;
	}

	public Document returnNormalizedDoc(){
		File f = getConnectionStoreFile();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = dBuilder.parse(f);
			doc.normalize();
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}		
		return doc;
	}
	
	public Node getDocRootNode(){
		Document doc = returnNormalizedDoc();
		Node root = doc.getElementsByTagName(ROOT_NODE).item(0);
		return root;
	}
	
	public File getConnectionStoreFile(){
		File file = new File(UPLOAD_DIRECTORY+File.separator+CONN_FILE);
		return file;
	}
	
	public boolean saveConnection(HashMap<String,String> connMap,String DBType){
		Document doc = returnNormalizedDoc();
		Node root = getDocRootNode();
		Element conn = doc.createElement(CONN_NODE);
		if(checkIdentifierAlreadyExists(connMap.get(CONN_IDENTIFIER))){
			//throw Exception TODO
			return false;
			
		}
		conn.setAttribute(CONN_IDENTIFIER, connMap.get(CONN_IDENTIFIER));
		conn.setAttribute(CONN_TYPE, DBType);
		connMap.remove(CONN_IDENTIFIER);
		
		if(DBType.equals("ORCL")){
			List<Element> connDetailElements = prepareElementListForORCL(connMap,doc);
			Iterator<Element> iter = connDetailElements.iterator();
			while(iter.hasNext()){
				conn.appendChild(iter.next());
			}
			root.appendChild(conn);
		}
		DOMSource source = new DOMSource(doc);        
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        StreamResult result = new StreamResult(getConnectionStoreFile());
	        transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private List<Element> prepareElementListForORCL(
			HashMap<String, String> connMap,Document doc) {
		List<Element> connDetailElements = new ArrayList<Element>();
		Iterator<Map.Entry<String, String>> iter = connMap.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, String> pair = iter.next();
			Element element = doc.createElement(pair.getKey());
			element.setTextContent(pair.getValue());
			connDetailElements.add(element);					
		}
		return connDetailElements;
	}	
	
	private boolean checkIdentifierAlreadyExists(String connId) {
		Node root = getDocRootNode();
		if(root!=null){
			NodeList nList = root.getChildNodes();			
			for(int i = 0;i<nList.getLength();i++){
				Node node = nList.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE){
					Element element = (Element)node;
					if(element.getAttribute(CONN_IDENTIFIER).equals(connId)){
						return false;
					}
				}
			}			
		}
		return true;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
