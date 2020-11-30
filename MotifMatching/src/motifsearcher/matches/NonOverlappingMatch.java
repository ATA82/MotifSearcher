package motifsearcher.matches;
/**
 * This class represents a match not allowing overlap by overwriting the equals function accordingly.
 * 
 * @author Ali T. Abdallah
 * @since jdk1.8
 * @version 08.05.2020
 */

public class NonOverlappingMatch {
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
	public NonOverlappingMatch(String sequence, int start, int length) {
		this.start=start-4;
		this.length=length;
		this.sequence=sequence;
	}	
	public boolean equals(Object other) {
		return this.overlaps(((NonOverlappingMatch)other));
	}
	public boolean identical(NonOverlappingMatch other) {
		return start==other.start && length==other.length;
	}
	public boolean overlaps(NonOverlappingMatch other) {
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
}