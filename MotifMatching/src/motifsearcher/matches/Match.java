package motifsearcher.matches;
/**
 * This class represents a match allowing overlap.
 * 
 * @author Ali T. Abdallah
 * @since jdk1.8
 * @version 08.05.2020
 */

public class Match {

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	int start;
	int length;
	String sequence;
	
	public Match(String sequence, int start, int length) {
		this.start=start-4;
		this.length=length;
		this.sequence=sequence;
	}
	
	public boolean equals(Object other) {
		return sequence.equals(((Match)other).sequence) && identical((Match)other);
	}
	
	
	public boolean identical(Match other) {
		return start==other.start && length==other.length;
	}
	
	public boolean ovelap(Match other) {
		return Math.max(start,other.start) <= Math.min(start+length,other.start+other.length);
	}
	
	public String toString() {
		return "["+start+":"+(start+length-1)+":"+sequence+"]";
	}
	
	public long count_critical_Gs() {
		String tokens[] = sequence.split("(TCCC)|(CCCC)",-1);
		if(tokens.length==4) { 
			return tokens[1].chars().filter(ch -> ch == 'G').count() + 
					tokens[3].chars().filter(ch -> ch == 'G').count();
		} else 
		return tokens[1].chars().filter(ch -> ch == 'G').count();
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		String match_info = "5utr,4,1 "
				+ "BM:4:[311:319:CCCCCCCC]:[513:522:TCCCATCCC]:[518:533:TCCCACCCTTGCCCC]:[529:540:CCCCGCCTCCC]:"
				+ "[529:546:CCCCGCCTCCCTGCCCC]:[536:546:TCCCTGCCCC]:[529:547:CCCCGCCTCCCTGCCCCC]:536:547:TCCCTGCCCCC]:"
				+ "[623:636:CCCCGGCGGCCCC] "
				+ "RM:1:[513:540:TCCCATCCCACCCTTGCCCCGCCTCCC]:[513:546:TCCCATCCCACCCTTGCCCCGCCTCCCTGCCCC]:"
				+ "[518:546:TCCCACCCTTGCCCCGCCTCCCTGCCCC]:[513:547:TCCCATCCCACCCTTGCCCCGCCTCCCTGCCCCC]:"
				+ "[518:547:TCCCACCCTTGCCCCGCCTCCCTGCCCCC]";
		
		String test = "TCCCATCCC";
		@SuppressWarnings("unused")
		String tokens[] = test.split("(TCCC)|(CCCC)",-1);
		
		Match m = new Match(test, 0, test.length());
		System.out.println(m.count_critical_Gs());
		
	}
	
	
	
}