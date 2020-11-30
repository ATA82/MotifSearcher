package motifsearcher;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import motifsearcher.matches.Match;
import motifsearcher.matches.NonOverlappingMatch;

/**
 * This class implements a - so called - atomic motif matcher to find all instances of a specific motif in a specific
 * genomic sequence. Here, we compute overlapping instances as well as non-overlapping ones.
 * The Matcher expects a sequence id, a sequence, a motif and optionally a size limit for the motif. Also, it is 
 * possible to provide a shift information for the coordinates/positions of the motif.
 * 
 * 
 * @author Ali T. Abdallah
 * @since jdk1.8
 * @version Friday - 08.05.2020
 *
 */

public class AtomicMotifMatcher {

	static boolean DEBUG = true;
	
	String id;
	String transcript;
	String motif;
	
	int motif_size_limit = -1;
	int nr_no_overlap_matches;
	int shift;
	
	boolean available = true;
	
	Vector<Match> matches;
 	Vector<NonOverlappingMatch> nomatches = new Vector<NonOverlappingMatch>();
 	Vector<Match> extended_matches;
	
	public AtomicMotifMatcher(String id, String transcript, String motif, int limit) {
		this.id=id;
		this.transcript = transcript;
		this.motif = motif;
		this.motif_size_limit = limit;
		matches = new Vector<Match>();
		extended_matches = new Vector<Match>();
	 	nomatches = new Vector<NonOverlappingMatch>();
	 	shift=0;
	}
	
	public AtomicMotifMatcher(String id, String transcript, String motif, int limit, int shift) {
		this.id=id;
		this.shift=shift;
		this.transcript = transcript;
		this.motif = motif;
		this.motif_size_limit = limit;
		matches = new Vector<Match>();
	 	nomatches = new Vector<NonOverlappingMatch>();
	 	extended_matches = new Vector<Match>();
	}
	
	public void run() {
		 Pattern pattern = Pattern.compile(motif);
	     Matcher matcher = pattern.matcher(transcript);
	     
	     if(transcript.contains("not available")) {
	    	 nr_no_overlap_matches = -1;
	     }
	     else {
	     for(int k=0; k < transcript.length(); k++) {
	    	 pattern = Pattern.compile(motif);
	    	 matcher = pattern.matcher(transcript.substring(k+0, Math.min(k+motif_size_limit, transcript.length())));
	    	 int search_start = 0;
	    	 while(matcher.find(search_start)) { // set start index for "find"
		        int start = shift+matcher.start();
		        int end = shift+matcher.end();
		        
		        /* Important code part:
		         * we have two match types with different equivalence classes.
		         * Two matches of type Match are considered equal, if their sequences and positions are the same.
		         * Two matches of type NonOverlappingMatch are considered equal, if they overlap each other.
		         * This is implemented by overriding the equals function in each class.
		         * In the case of the no match vector the representative of the equivalence class is - based on this
		         * for loop - always the first match.
		         */
		        Match match 				= new Match(matcher.group(), k+start, end-start+1);
		        NonOverlappingMatch nomatch = new NonOverlappingMatch(matcher.group(), k+start, end-start+1);
		        if(!matches.contains(match)) 	 matches	.add(match);
		        if(!nomatches.contains(nomatch)) nomatches	.add(nomatch);
		        
		        search_start = matcher.start() + 1; // update start index to start from beginning of last match + 1
		     }
		}
	     
	     nr_no_overlap_matches = nomatches.size();
	    }
	}
	
	public String toString() {
		String report = nr_no_overlap_matches+"";
		for(int i = 0; i < matches.size(); i++) {
			report += ":"+matches.get(i);
		}
		report += "";
		return report;
	}
	
	public String toString(int upstream, int downstream) {
		String report = nr_no_overlap_matches+"";
		for(int i = 0; i < matches.size(); i++) {
			report += ":"+matches.get(i);
		}
		report += "";
		return report;
	}
	
	public static void main(String[] args) {
		String transcript = "ATGGAGTCGGCCGACTTCTACGAGGCGGAGCCGCGGCCCCCGATGAGCAGCCACCTGCA"
							+ "GAGCCCCCCGCACGCGCCCAGCAGCGCCGCCTTCGGCTTTCCCCGGGGCGCGGGCCCCGCGCAGCCT"
							+ "CCCGCCCCACCTGCCGCCCCGGAGCCGCTGGGCGGCATCTGCGAGCACGAGACGTCCATCGACATCA"
							+ "GCGCCTACATCGACCCGGCCGCCTTCAACGACGAGTTCCTGGCCGACCTGTTCCAGCACAGCCGGCA"
							+ "GCAGGAGAAGGCCAAGGCGGCCGTGGGCCCCACGGGCGGCGGCGGCGGCGGCGACTTTGACTACCCG"
							+ "GGCGCGCCCGCGGGCCCCGGCGGCGCCGTCATGCCCGGGGGAGCGCACGGGCCCCCGCCCGGCTACG"
							+ "GCTGCGCGGCCGCCGGCTACCTGGACGGCAGGCTGGAGCCCCTGTACGAGCGCGTCGGGGCGCCGGC"
							+ "GCTGCGGCCGCTGGTGATCAAGCAGGAGCCCCGCGAGGAGGATGAAGCCAAGCAGCTGGCGCTGGCC"
							+ "GGCCTCTTCCCTTACCAGCCGCCGCCGCCGCCGCCGCCCTCGCACCCGCACCCGCACCCGCCGCCCG"
							+ "CGCACCTGGCCGCCCCGCACCTGCAGTTCCAGATCGCGCACTGCGGCCAGACCACCATGCACCTGCA"
							+ "GCCCGGTCACCCCACGCCGCCGCCCACGCCCGTGCCCAGCCCGCACCCCGCGCCCGCGCTCGGTGCC"
							+ "GCCGGCCTGCCGGGCCCTGGCAGCGCGCTCAAGGGGCTGGGCGCCGCGCACCCCGACCTCCGCGCGA"
							+ "GTGGCGGCAGCGGCGCGGGCAAGGCCAAGAAGTCGGTGGACAAGAACAGCAACGAGTACCGGGTGCG"
							+ "GCGCGAGCGCAACAACATCGCGGTGCGCAAGAGCCGCGACAAGGCCAAGCAGCGCAACGTGGAGACG"
							+ "CAGCAGAAGGTGCTGGAGCTGACCAGTGACAATGACCGCCTGCGCAAGCGGGTGGAACAGCTGAGCC"
							+ "GCGAACTGGACACGCTGCGGGGCATCTTCCGCCAGCTGCCAGAGAGCTCCTTGGTCAAGGCCATGGG"
							+ "CAACTGCGCGTGA";
		
		AtomicMotifMatcher mm = new AtomicMotifMatcher("CEBPA", transcript, "[TC]CCC.*CCC[TC]", 19);
		mm.run();
		System.out.println(mm);
		
		mm = new AtomicMotifMatcher("CEBPA", transcript, "[TC]CCC.*[TC]CCC.*[TC]CCC.*[TC]CCC", 38);
		mm.run();
		System.out.println(mm);
		
		System.exit(0);
	}		
}
