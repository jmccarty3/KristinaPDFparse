package kristina.pdf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class Main {
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String toLoad = args[0];
		Processor processer = new Processor(toLoad);
		 try {
			processer.doWork();
		} catch (IOException e) {
			System.out.print("Aw, hell. Shit went bad");
			e.printStackTrace();
		}	 
		 

	}

}
