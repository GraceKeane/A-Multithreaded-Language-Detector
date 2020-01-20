package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Grace Keane
 * @since 1.0
 * @version 1.8
 * 
 * <i>Database</i> <b>stores</b> the information needed to run this program
 * 
 * see add
 * see getLanguageEntries
 * see resize
 * see getTop
 * see getLanguage
 * see getOutOfPlaceDistance
 * see OutOfPlaceMetric
 * see getLanguage
 * see getAbsoluteDistance
 */

public class Database {
	//maps languages to their ngrams and their frequency of occurance
	private Map<Language, Map<Integer, LanguageEntry>> db = new ConcurrentSkipListMap<>();
	
	/**
	 * Add parses some text
	 * Converts strings into hashcodes. Stores the int 
	 * 
	 * @param s readable sequence of char values.
	 * Provides uniform, read-only access to many different kinds of char sequences.
	 * A char value represents a character in the Basic Multilingual Plane 
	 * @param lang readable sequence of language values.
	 */
	public void add(CharSequence s, Language lang) {
		int kmer = s.hashCode();
		//gets a handle on the language map for a particular language and passes it the language(lang)
		Map<Integer, LanguageEntry> langDb = getLanguageEntries(lang);
		
		//setting frequency to 1
		int frequency = 1;
		//if language map already has the k-mer then frequency is incremented
		if (langDb.containsKey(kmer)) {
			frequency += langDb.get(kmer).getFrequency();
		}
		
		//re-inserting into map, override the existing k-mer with a new language entry with a frequency
		langDb.put(kmer, new LanguageEntry(kmer, frequency));
		
	}
	
	/**
	 * Map creates the language if it isn't in the database and returns it if language is found
	 * 
	 * @param lang readable sequence of language values.
	 * @return returns langDB
	 */
	private Map<Integer, LanguageEntry> getLanguageEntries(Language lang){
		Map<Integer, LanguageEntry> langDb = null; 
		if (db.containsKey(lang)) {
			langDb = db.get(lang);
		}else {
			langDb = new TreeMap<Integer, LanguageEntry>();
			//language relates to map
			db.put(lang, langDb);
		}
		return langDb;
	}
	
	/**
	 * cuts down the number to just 300 top entries in the map
	 * 
	 * @param max setting max to 300
	 */
	
	//passing in 300 as max
	public void resize(int max) {
		//calling db.keySet - getting all the languages 
		Set<Language> keys = db.keySet();
		for (Language lang : keys) {
			//for each language it gets the mapping of the integers to their language 
			Map<Integer, LanguageEntry> top = getTop(max, lang);
			//re-inserting into the map
			db.put(lang, top);
		}
	}
	
	/**
	 * keeping the top 300 k-mers in a given map
	 * 
	 * @param max initializing the max to be 300
	 * @param lang gets language
	 * @return returners the temp value
	 */
	public Map<Integer, LanguageEntry> getTop(int max, Language lang) {
		//creating a temporary map
		Map<Integer, LanguageEntry> temp = new ConcurrentSkipListMap<>();
		//returning the set of frequencies & language from the main map
		List<LanguageEntry> les = new ArrayList<>(db.get(lang).values());
		Collections.sort(les);
		
		int rank = 1;
		for (LanguageEntry le : les) {
			le.setRank(rank);
			temp.put(le.getKmer(), le);		
			//once hit 300 then it will break otherwise the rank will be implemented
			if (rank == max) break;
			rank++;
		}
		
		return temp;
	}
	
	/**
	 * 
	 * @param query extract data from the database.
	 * @return the data in the database
	 */
	public Language getLanguage(Map<Integer, LanguageEntry> query) {
		//comparing my query against the database
		TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();
		
		Set<Language> langs = db.keySet();
		for (Language lang : langs) {
			//adding new language to sorted treeSet
			//adding language to sorted treeSet a new OutOfPlaceMetric with language name
			//giving query and map for the language
			oopm.add(new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, db.get(lang))));
		}
		return oopm.first().getLanguage();
	}
	
	/**
	 * 
	 * comparing two queries
	 * 
	 * @param query  extract data from the database
	 * @param subject grouping of related information for a single entity
	 * @return returns the distance
	 */
	private int getOutOfPlaceDistance(Map<Integer, LanguageEntry> query, Map<Integer, LanguageEntry> subject) {
		int distance = 0;
		
		//creating a sorted treeSet based on query values
		Set<LanguageEntry> les = new TreeSet<>(query.values());		
		for (LanguageEntry q : les) {
			//getting language entry in the specific language database
			LanguageEntry s = subject.get(q.getKmer());
			//if doesn't exist it is set to the farthest distance
			if (s == null) {
				distance += subject.size() + 1;
			}else {
				//if data does exist minus the subject rank to the query rank
				distance += s.getRank() - q.getRank();
			}
		}
		return distance;
	}
	
	/*
	 * distance between it and the query
	 * OutOfPlaceMetric implements comparable
	 */
	
	private class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric>{
		private Language lang;
		private int distance;
		
		/**
		 * constructor that takes in lang and distance
		 * 
		 * @param lang language
		 * @param distance between it and the query
		 */
		public OutOfPlaceMetric(Language lang, int distance) {
			super();
			this.lang = lang;
			this.distance = distance;
		}

		/**
		 * gets the language
		 * @return returns the language 
		 */
		public Language getLanguage() {
			return lang;
		}

		/**
		 * if taking one distance away from the other or one rank from another this function will be used
		 * @return returns a positive value for distance
		 */
		public int getAbsoluteDistance() {
			return Math.abs(distance);
		}

		//gives values in ascending order
		@Override
		public int compareTo(OutOfPlaceMetric o) {
			return Integer.compare(this.getAbsoluteDistance(), o.getAbsoluteDistance());
		}

		@Override
		public String toString() {
			return "[lang=" + lang + ", distance=" + getAbsoluteDistance() + "]";
		}
			
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		int langCount = 0;
		int kmerCount = 0;
		Set<Language> keys = db.keySet();
		for (Language lang : keys) {
			langCount++;
			sb.append(lang.name() + "->\n");
			 
			 Collection<LanguageEntry> m = new TreeSet<>(db.get(lang).values());
			 kmerCount += m.size();
			 for (LanguageEntry le : m) {
				 sb.append("\t" + le + "\n");
			 }
		}
		sb.append(kmerCount + " total k-mers in " + langCount + " languages"); 
		return sb.toString();
	}
}