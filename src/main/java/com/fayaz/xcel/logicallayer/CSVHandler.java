package com.fayaz.xcel.logicallayer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler implements BaseHandler{

	private String file = "D:\\Backup From Old Lap\\Folders\\xlsxtodb_test_files\\export.xlsx";
	private BufferedReader fileReader = null;
	private int rowCounter = 0;
	private List<String> values;

	private final String DELIMITER = ",";

	public CSVHandler(String file) {
		this.file = file;
		values = new ArrayList<String>();
		try {
			fileReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void parseAllSheets() {
		try {
			String line = null;

			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				for (String token : tokens) {
					values.add(token);
				}
				rowCounter++;
			}
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fileReader != null)
				try {
					fileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
	}
	
	public int getRowsProcessed(){
		return  rowCounter;
	}
	
	//Following method only returns the name of the file from the path..
	//Method basically ensures uniformity of method signatures for different 
	//extension handlers..
	@Override
	public List<String> getSheetNames() {
		List<String> nameList = new ArrayList<String>();
		nameList.add(file.substring(file.lastIndexOf("\\")+1));
		return nameList;
	}

}
