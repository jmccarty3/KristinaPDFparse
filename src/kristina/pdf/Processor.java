package kristina.pdf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class Processor {
	private String pdfFile;
	HashMap<String, Fund> funds;
	
	public Processor(String fileName)
	{
		pdfFile = fileName;
		funds = new HashMap<String, Fund>();
		System.out.println("Going to process: " + fileName);
	}
	
	public void doWork() throws IOException
	{
		System.out.println("Loading File " + pdfFile);
		
		PdfReader reader = new PdfReader(pdfFile);
		PdfReaderContentParser contentParser = new PdfReaderContentParser(reader);
		TextExtractionStrategy strategy;
		
		//System.out.println("First Page: " + contentParser.processContent(1, new SimpleTextExtractionStrategy()).getResultantText());
		
		for(int page=1; page <= reader.getNumberOfPages(); page++){
			strategy = contentParser.processContent(page, new SimpleTextExtractionStrategy());
			String text = strategy.getResultantText();
			
			String[] splitText = text.split("\n");
			
			if(splitText.length >=5)
			{
				String headerLine = splitText[4].trim();
				System.out.println("First Line after header:" + headerLine);
				String idCode = headerLine.substring(0, 3);
				String fundId = headerLine.substring(4);
				
				Fund currentFund; 
				if(!funds.containsKey(idCode)){
					currentFund = new Fund(idCode, fundId);
					funds.put(idCode, currentFund);
				}
				else
					currentFund = funds.get(idCode);
				
				currentFund.parseText(Arrays.copyOfRange(splitText, 5, splitText.length));				
			}
			else
				System.out.println("Nothing after header on page: " + page);
			
		}
		
		System.out.println("Number of funds: " + funds.size());
		
		for (Fund fund : funds.values()) {
			System.out.println("Fund: " + fund);
			fund.toFile();
			//fund.allToFile();
			fund.everythingToFile();
		}
		
		System.out.println("Done");
		
		System.out.print("Closing File");
		reader.close();
		
	}
	
}
