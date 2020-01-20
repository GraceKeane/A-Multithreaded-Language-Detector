package ie.gmit.sw;

import java.util.concurrent.*;

/**
 * @author Grace Keane
 * @since 1.0
 * @version 1.8
 */

public class Runner {
	
	public static void main(String [] args) {
		BlockingQueue<Language> l = new ArrayBlockingQueue<>(5);
			Parser p = new Parser(l);
			new Thread(p).start();	
	}
}
