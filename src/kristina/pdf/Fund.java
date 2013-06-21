package kristina.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class Fund {
	private String fundName;
	private String fundId;
	private HashMap<String, String> fourCodes;
	private HashMap<String, String> fiveCodes;
	private List<String> allCodes;
	private String lastRead;
	private List<String> everything;

	public Fund(String id, String name)
	{
		this.fundId = id;
		this.fundName = name;
		this.fourCodes = new HashMap<String, String>();
		this.fiveCodes = new HashMap<String,String>();
		allCodes = new ArrayList<String>();
	    everything = new ArrayList<String>();
		
	}
	
	public void parseText(String[] toParse){
		String fourPattern = "\\d{4}[-]\\d{2}\\s+[\\S\\s]*";
		String fivePattern = "\\d{5}[-]\\d{4}\\s+[\\S\\s]*";
		
		for(String line : toParse){
			String stripped = strip(line);
			
			if(isLabel(stripped) == false){
				if(stripped.matches(fourPattern)){
					processFour(stripped);
				}
				else if(stripped.matches(fivePattern)){
					processFive(stripped);
				}
				else if(stripped.isEmpty() == false)
					everything.add(stripped);
			}
			
		}
		
	}
	
	private void processFive(String stripped) {
				
		String[] pieces = stripped.split("\\s+", 2);
		
		System.out.println(this + " got five didget code: " + pieces[0]);
		System.out.println("Value: " + pieces[1]);
		
		fiveCodes.put(pieces[0], pieces[1]);
		
		allCodes.add(pieces[0] + "," +pieces[1]);
		everything.add("," + pieces[0] + "," + pieces[1]);
		
	}

	private void processFour(String stripped) {
		String[] pieces = stripped.split("\\s+", 2);
		
		System.out.println(this + " got for didget code: " + pieces[0]);
		System.out.println("Value: " + pieces[1]);
		
		fourCodes.put(pieces[0], pieces[1]);
		
		allCodes.add(pieces[0] + "," +pieces[1]);
		
		everything.add("," + pieces[0] + "," + pieces[1]);
	}

	@Override
	public String toString()
	{
		return fundId + "-" + fundName;
	}
	
	private String strip(String toStrip)
	{
		String stripped;
		String headerLine = "____+";
		String header= "ACCOUNT\\s#\\s*.*";
		String expende = "EXPENDITURES";
		String revenue = "REVENUES";
		String footer = "\\*\\*\\*.*";
		
		stripped = toStrip.trim();
		
		if(stripped.matches(headerLine))
			stripped = "";
		else if(stripped.matches(header))
			stripped = "";
		else if(stripped.matches(expende))
			stripped = "";
		else if(stripped.matches(revenue))
			stripped = "";
		else if(stripped.matches(footer))
			stripped = "";
		
		return stripped;
	}
	
	private Boolean isLabel(String toCheck)
	{
		
		if(toCheck.matches("====+"))
		{
			everything.add(",");
			return true;
		}
		
		return false;
				
	}
	
	public void toFile() throws IOException{
		FileWriter writer = new FileWriter(toString() +".csv", false);
		BufferedWriter output = new BufferedWriter(writer);
		
		for(Map.Entry<String, String> entry : fourCodes.entrySet()){
			output.write(entry.getKey() + "," + entry.getValue());
			output.newLine();
		}
		
		output.newLine();
		output.newLine();
		output.newLine();
		
		for(Map.Entry<String, String> entry : fiveCodes.entrySet()){
			output.write(entry.getKey() + "," + entry.getValue());
			output.newLine();
		}
		
		output.close();
	}
	
	public void allToFile() throws IOException
	{
		FileWriter writer = new FileWriter("all-" +toString() +".csv", false);
		BufferedWriter output = new BufferedWriter(writer);
		
		for(String entry : allCodes){
			output.write(entry);
			output.newLine();
		}
		
		output.close();
	}
	
	public void everythingToFile() throws IOException
	{
		FileWriter writer = new FileWriter("headers-" +toString() +".csv", false);
		BufferedWriter output = new BufferedWriter(writer);
		
		for(String entry : everything){
			output.write(entry);
			output.newLine();
		}
		
		output.close();
	}

}
