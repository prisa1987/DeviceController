package models;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Insert/Delete a string to file
 * @author Krittapat Wongyaowarak
 */
public class InsertString {



	public static void insertStringInFile(File inFile, int lineno, String lineToBeInserted) throws Exception {
		// temp file
		File outFile = new File("E:\\Hagen-Internship\\workspace\\simpleTest\\src\\com\\example\\simpleTest\\$$$$$$$$.java");
		
		// input
		FileInputStream fis  = new FileInputStream(inFile);
		BufferedReader in = new BufferedReader
		    (new InputStreamReader(fis));
		
		// output         
		FileOutputStream fos = new FileOutputStream(outFile);
		PrintWriter out = new PrintWriter(fos);
		
		String thisLine = "";
		int i =1;
		while ((thisLine = in.readLine()) != null) {
		  if(i == lineno)
			  out.println(lineToBeInserted);
		  
			  out.println(thisLine);
		  i++;
		  }
		out.flush();
		out.close();
		in.close();
		
		inFile.delete();
		outFile.renameTo(inFile);
	}
	
	public static void deleteStringInFile(File inFile, int lineno) throws IOException {
		// temp file
		File outFile = new File(
				"E:\\Hagen-Internship\\workspace\\simpleTest\\src\\com\\example\\simpleTest\\$$$$$$$$.java");

		// input
		FileInputStream fis = new FileInputStream(inFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));

		// output
		FileOutputStream fos = new FileOutputStream(outFile);
		PrintWriter out = new PrintWriter(fos);

		String thisLine = "";
		int i = 1;
		while ((thisLine = in.readLine()) != null) {
			if (i != lineno)
				out.println(thisLine);
			i++;
		}
		out.flush();
		out.close();
		in.close();

		inFile.delete();
		outFile.renameTo(inFile);
	}
}
