package ie.gmit.sw;
/**
 * @author Grace Keane
 * @since 1.0
 * @version 1.8
 * 
 * The class <i>LanguageEntry</i> <b>implements</b> the class <i>Comparable</i> which invokes the class <i>LanguageEntry</i>.
 * The Language entry class contains getters and setters
 * 
 * see LanguageEntry
 * see compareTo
 */

public class LanguageEntry implements Comparable<LanguageEntry> {
	/*
	 * int kmer - Calling hashcode & store the cheaper int
	 * int frequency - frequency of occurrence of a kmer in a language
	 * int rank - ranking of a k-mer in terms of frequency in the language
	 */
	private int kmer;
	private int frequency;
	private int rank;

	/**
	 * 
	 * @param kmer separating the lines into 5 k-mers to be able to detect the language
	 * @param frequency rate at which words occur over a particular period of time or in a given sample.
	 */
	public LanguageEntry(int kmer, int frequency) {
		super();
		this.kmer = kmer;
		this.frequency = frequency;
	}

	/**
	 * 
	 * @return all the k-mers of a specific size in the file
	 */
	public int getKmer() {
		return kmer;
	}

	/**
	 * 
	 * @param kmer setting the k-mer to 5
	 * 'THIS' in Java is a reference variable that refers to the current object.
	 */
	public void setKmer(int kmer) {
		this.kmer = kmer;
	}

	/**
	 * 
	 * @return returners the rate at which words occur in a given sample.
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * 
	 * @param frequency returns the number of elements in the collection.
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * 
	 * @return returns the rank/ position
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * 
	 * @param rank returns the rank/ position
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	//comparing one language entry to another (compare one k-mer to another by the frequency, also done in descending order)
	@Override
	public int compareTo(LanguageEntry next) {
		return - Integer.compare(frequency, next.getFrequency());
	}
	
	@Override
	public String toString() {
		return "[" + kmer + "/" + frequency + "/" + rank + "]";
	}
}