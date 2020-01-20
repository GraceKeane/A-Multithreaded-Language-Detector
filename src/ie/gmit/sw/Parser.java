//Run from parser class
package ie.gmit.sw;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Grace Keane
 * @since 1.0
 * @version 1.8
 * 
 * The class <i>Parser</i> <b>implements</b> the class <i>Runnable</i>.
 * Parser deals with analyzing the text file selected and then
 * determining the correct language. A K-mer is a contiguous substring of text of size n.
 * K-mers have been applied to a wide variety of computing problems including text similarity
 * measurement, text subject determination and language classification.
 * 
 *
 *see Parser
 *see setDB
 *see Parse
 *see analyseQuery
 */


public class Parser implements Runnable { 
	private BlockingQueue<Language> queue;
	
	/**
	 * 
	 * @param queue represents a data structure 
	 */
	public Parser(BlockingQueue<Language> queue) {
		super();
		this.queue = queue;
	}
	
	private Database db = null;
	private String file;
	private int k;
	
	/**
	 * 
	 * @param file parses the file specified
	 * @param string analyzing each word
	 * @param k separates words into groups using k-mers
	 */
	public Parser(String file, String string, int k) {
		this.file = file;
		this.k = k;
	}
	
	/**
	 * 
	 * @param db language database
	 */
	public void setDb(Database db) {
		this.db = db;
	}
	
	@Override
	public void run() {
		Scanner console = new Scanner(System.in);
		String fileName;
		
		System.out.print("\nEnter your name: " );
		String fname = console.next();
		
		System.out.print("Enter your surname: ");
		String sname = console.next();
		
		System.out.print("Welcome to the programme " + fname + " " + sname);
		System.out.println("\n");
		System.out.println("Please enter 1 to use the small text file (wili-2018-Small-11750-Edited.txt)");
		System.out.println("Please enter 2 to use the large text file (wili-2018-Large-117500-Edited.txt)");
		System.out.println("Enter your number for text file: ");
		int file = console.nextInt();
	
		if(file == 1) {
			System.out.println("You have selected the small text file");
			fileName = ("wili-2018-Small-11750-Edited.txt");
		}
		else if (file == 2){
			System.out.println("You have selected the large text file");
			fileName = ("wili-2018-Large-117500-Edited.txt");
		}
		else {
			System.out.println("Invalid input. Please try again");
			return;
		}
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("You have selected the file " + fileName);
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		System.out.println("Building subject database...please wait...");
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			
			System.out.println("in run()");
			
			//BufferedReader to point at the specified file
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String line = null;
			
			//for each line split it based on the @ symbol
			while((line = br.readLine()) != null){
				String[] record = line.trim().split("@");
				if(record.length != 2) {
					continue;
				}
				System.out.println("\n\nLanguage Snippet: " + record[0] + "\n\nLanguage Name: " +record[1] );
				//split into an array and get element 0 & 1
				parse(record[0], record[1]);
			}
			
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param text text being parsed by the selected file
	 * @param lang detecting language
	 * @param ks k-mer devision of size 5
	 */
	private void parse(String text, String lang, int... ks) { 
		//taking a string language and map to an enum value (must match exactly)
		Language language = Language.valueOf(lang);
		System.out.println("\nIn Parse Method");
		System.out.println("\nLanguage devided into kmers of a specific size");

		//looping over sequence
		for(int i = 0; i <= text.length() - k; i++) {
			//read the sub string from i to i + k and then add to map
			CharSequence kmer = text.substring(i, i + k);
			db.add(kmer, language);
			System.out.println(kmer);
			
		}
		
		System.out.println("\nEnd of language kmer parse");
		
		System.out.println("\nNext Language");


	}

	public static void main(String [] args) throws Throwable{
		
		System.out.println("*****************************************************************");
		System.out.println("*\t\t\t\t\t\t\t\t*");
		System.out.println("*\tGMIT  -  Dept of Computer Scienct & Applied Physics\t*");
		System.out.println("*\t\t\t\t\t\t\t\t*");
		System.out.println("*\t\t\tText Language Detector\t\t\t*");
		System.out.println("*\t\t\t\t\t\t\t\t*");
		System.out.println("*****************************************************************");
		
		Parser p = new Parser("wili-2018-Small-11750-Edited.txt", "wili-2018-Large-117500-Edited.txt", 5);
		
		Database db = new Database();
		p.setDb(db);
		Thread t = new Thread(p);
		System.out.println("\nGenerating a new thread...");
		System.out.println("Loading...");
		t.sleep(1000);
		t.start();
		System.out.println("Starting thread...");
		t.join();
		System.out.println("Joined...");
		db.resize(300);
		System.out.println("Resizing database to 300...");
		
	}

	/**
	 * 
	 * @param s used to extract data (Strings) from the database
	 */
	private void analyseQuery(String s) {
		Language language = Language.valueOf(s);
		
		for(int i = 0; i <= s.length() - k; i++) {
			CharSequence kmer = s.substring(i, i + k);
			db.add(kmer, language);
		
		}
	}
}

