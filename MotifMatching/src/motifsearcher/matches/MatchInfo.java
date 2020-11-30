package motifsearcher.matches;

import java.util.HashMap;

public class MatchInfo {
	String region;
	int nr_no_interaction_motifs;
	int nr_no_regulatory_motifs;
	HashMap<String, String> coord_to_bm_motif_sequence;
	HashMap<String, String> coord_to_rm_motif_sequence;
	
	public MatchInfo(String match_info) {
		
		String infos[] = match_info.split(" ");

		region = infos[0].split(",")[0];
		nr_no_interaction_motifs = Integer.parseInt(infos[0].split(",")[1]);
		nr_no_regulatory_motifs  = Integer.parseInt(infos[0].split(",")[2]);
		
		
		coord_to_bm_motif_sequence = new HashMap<String, String>();
		if(infos[1].length() > 5) {
		String SecondInfo = infos[1].substring(infos[1].indexOf(":", 4));
		String bm_matches[] = SecondInfo.substring(1).split("(]:)|(])");
		for(int i = 0; i < bm_matches.length; i++) {
			String coord = bm_matches[i].substring(1, bm_matches[i].lastIndexOf(":"));
			String ms = bm_matches[i].substring(bm_matches[i].lastIndexOf(":"), bm_matches[i].length());
			coord_to_bm_motif_sequence.put(coord, ms.substring(1));
		}
		}
		coord_to_rm_motif_sequence = new HashMap<String, String>();

		if(infos[2].length() > 5) {
		String ThirdInfo = infos[2].substring(infos[2].indexOf(":", 4));
		String rm_matches[] = ThirdInfo.substring(1).split("(]:)|(])");
		for(int i = 0; i < rm_matches.length; i++) {
			String coord = rm_matches[i].substring(1, rm_matches[i].lastIndexOf(":"));
			String ms = rm_matches[i].substring(rm_matches[i].lastIndexOf(":"), rm_matches[i].length());
			coord_to_rm_motif_sequence.put(coord, ms.substring(1));
		}
		}
		
	}

	int non_critical_bm_matches = 0;
	int non_critical_rm_matches = 0;
	
	public String G_Infos() {
		String G_Infos= "";
		for(String bm_match_key: coord_to_bm_motif_sequence.keySet()) {
			int start = Integer.parseInt(bm_match_key.split(":")[0]);
			int end = Integer.parseInt(bm_match_key.split(":")[1]);
			String sequence = coord_to_bm_motif_sequence.get(bm_match_key);
			long nr_critical_Gs = new Match(sequence, start,end-start+1).count_critical_Gs();
			G_Infos += sequence+":"+nr_critical_Gs+",";
			if(nr_critical_Gs == 0) non_critical_bm_matches++;
		}

		if(G_Infos.length()==0) non_critical_bm_matches = -1;
		G_Infos = (G_Infos.length()==0)?"NA\t":G_Infos.substring(0,G_Infos.length()-1)+"\t";
		
		String add_G_Infos = "";
		for(String rm_match_key: coord_to_rm_motif_sequence.keySet()) {
			int start = Integer.parseInt(rm_match_key.split(":")[0]);
			int end = Integer.parseInt(rm_match_key.split(":")[1]);
			String sequence = coord_to_rm_motif_sequence.get(rm_match_key);
			long nr_critical_Gs = new Match(sequence, start,end-start+1).count_critical_Gs();
			add_G_Infos += sequence+":"+nr_critical_Gs+",";
			if(nr_critical_Gs == 0) non_critical_rm_matches++;
		}

		if(add_G_Infos.length()==0) non_critical_rm_matches = -1;
		G_Infos += (add_G_Infos.length()==0)?"NA\t":add_G_Infos.substring(0,add_G_Infos.length()-1)+"\t";
		return ((non_critical_bm_matches>0)?"Yes":"-")+ "\t" +
				((non_critical_rm_matches>0)?"Yes":"-")+"\t"+
					non_critical_bm_matches+"\t"+non_critical_rm_matches+"\t"+ G_Infos;
	}
	

	public static void main(String[] args) {
		String match_info = "5utr,4,1 "
				+ "BM:0 "
				+ "RM:1:[513:540:TCCCATCCCACCCTTGCCCCGCCTCCC]:[513:546:TCCCATCCCACCCTTGCCCCGCCTCCCTGCCCC]:"
				+ "[518:546:TCCCACCCTTGCCCCGCCTCCCTGCCCC]:[513:547:TCCCATCCCACCCTTGCCCCGCCTCCCTGCCCCC]:"
				+ "[518:547:TCCCACCCTTGCCCCGCCTCCCTGCCCCC]";
		
		MatchInfo mi = new MatchInfo(match_info);
		System.out.println(mi.G_Infos());
	}
	
	
}
