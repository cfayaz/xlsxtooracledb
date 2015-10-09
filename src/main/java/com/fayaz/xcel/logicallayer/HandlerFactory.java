package com.fayaz.xcel.logicallayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerFactory {
	
	static Logger logger = LoggerFactory.getLogger(HandlerFactory.class);
	
//	public enum Extension {
//		XLSX("xlsx"),CSV("csv");
//		
//		private String ext;
//		
//		private  Extension(String ext){
//			this.ext = ext;
//		}
//		
//		@Override
//		public String toString(){
//			return this.ext;
//		}
//	}
	
	public static synchronized BaseHandler getHandler(String ext,String file){
		
		if(ext.equals("xlsx"))
			return new XLSXHandler(file);
		else if (ext.equals("csv"))
			return new CSVHandler(file);	
		else{
			logger.error("Invalid Extension!!!");
			return null;
		}
			
		
	}
}
