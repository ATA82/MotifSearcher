import java.io.File;

import java.io.IOException;

import motifsearcher.MultiSequencesMultiRegionsMultiMotifsMatcher;

public class Main {

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("unused")
		String test_args[][] =	{	{		
											"-i",	"/home/aabdalla/Desktop/devries/input/cds.fasta,"+ 	
													"/home/aabdalla/Desktop/devries/input/3utr.fasta,"+
													"/home/aabdalla/Desktop/devries/input/5utr.fasta",
											"-n",	"cds,3utr,5utr",
											"-o",	"/home/aabdalla/Desktop/devries/output/SummarizedMatchTable_mRNA.tsv",
											"-m",	"[TC]CCC.*[TC]CCC{19},[TC]CCC.*[TC]CCC.*[TC]CCC.*[TC]CCC{38}",
											"-c",   "ensembl_gene_id\tensembl_transcript_id\tCDS_Interaction\tCDS_Regulatory\t"
													+ "3UTR_Interaction\t3UTR_Regulatory\t5UTR_Interaction\t5UTR_Regulatory\t"
													+ "CDS_MATCH_INFORMATION (BM: Binding Motif, RM: Regulatory Motif)\t"
													+ "3UTR_MATCH_INFORMATION (BM: Binding Motif, RM: Regulatory Motif)\t"
													+ "5UTR_MATCH_INFORMATION (BM: Binding Motif, RM: Regulatory Motif)"
									},
							 		{		
											"-i",	"/home/aabdalla/Desktop/devries/input/ncrna.fasta",
											"-n",	"ncrna",
											"-o",	"/home/aabdalla/Desktop/devries/output/SummarizedMatchTable_ncrna.tsv",
											"-m",	"[TC]CCC.*[TC]CCC{19},[TC]CCC.*[TC]CCC.*[TC]CCC.*[TC]CCC{38}",
											"-c",   "ensembl_gene_id\tensembl_transcript_id\tNCRNA_Interaction\tNCRNA_Regulatory\t"
													+ "NCRNA_MATCH_INFORMATION (BM: Binding Motif, RM: Regulatory Motif)"
							 		}
								};
		
		String test_args2[][] =	{	{		
			"-i",	"/home/aabdalla/Desktop/devries/input/cds.fasta,"+ 	
					"/home/aabdalla/Desktop/devries/input/3utr.fasta,"+
					"/home/aabdalla/Desktop/devries/input/5utr.fasta",
			"-n",	"cds,3utr,5utr",
			"-o",	"/home/aabdalla/Desktop/devries/output/SummarizedMatchTable_GFiltered_mRNA.tsv",
			"-m",	"[TC]CCC[^G]*[TC]CCC{19},[TC]CCC[^G]*[TC]CCC.*[TC]CCC[^G]*[TC]CCC{38}",
			"-c",   "ensembl_gene_id\tensembl_transcript_id\tCDS_Interaction\tCDS_Regulatory\t"
					+ "3UTR_Interaction\t3UTR_Regulatory\t5UTR_Interaction\t5UTR_Regulatory\t"
					+ "CDS_MATCH_INFORMATION (BM: Binding Motif, RM: Regulatory Motif)\t"
					+ "3UTR_MATCH_INFORMATION (BM: Binding Motif, RM: Regulatory Motif)\t"
					+ "5UTR_MATCH_INFORMATION (BM: Binding Motif, RM: Regulatory Motif)"
	},
		{		
			"-i",	"/home/aabdalla/Desktop/devries/input/ncrna.fasta",
			"-n",	"ncrna",
			"-o",	"/home/aabdalla/Desktop/devries/output/SummarizedMatchTable_GFiltered_ncrna.tsv",
			"-m",	"[TC]CCC[^G]*[TC]CCC{19},[TC]CCC[^G]*[TC]CCC.*[TC]CCC[^G]*[TC]CCC{38}",
			"-c",   "ensembl_gene_id\tensembl_transcript_id\tNCRNA_Interaction\tNCRNA_Regulatory\t"
					+ "NCRNA_MATCH_INFORMATION (BM: Binding Motif, RM: Regulatory Motif)"
		}
};
		
		// No_Critical_Gs_BM\tNo_Critical_Gs_RM\tNrof_non_critical_Gs_BM\tNrof_non_critical_Gs_RM\tNoGs_Info_BM\tNoGs_Info_RM\t
		
		int number_of_runs = 1;
		if(args.length==0) {
			number_of_runs = 2;
		}
		
		for(int r = 0; r < number_of_runs; r++) {
			if(number_of_runs>1) args = test_args2[r];
			String files[] 	= null;
			String outfile 	= null;
			String outfile_header = null;
			String names[] 	= null;
			String motifs[] = null;
			int[] limits 	= null;
			boolean custom_names = false;
			boolean input_miss = true;
			boolean output_miss = true;
			boolean motif_miss = true;
			boolean help_requested = false;
		
			boolean input_file_dont_exist[] = null;
		
			for(int option=0; option < args.length; option+=2) {
				switch(args[option]) {
					case "-i": 	input_miss = false; 
							files = args[option+1].split(",");
							input_file_dont_exist = new boolean[files.length];
							for(int i = 0; i < files.length; i++) {
								if(!new File(files[i]).exists()) input_file_dont_exist[i]=true;
								else input_file_dont_exist[i]=false;
							}
							break;
					case "-n":	custom_names=true;names = args[option+1].split(","); break;
					case "-o":	output_miss=false; outfile = args[option+1]; break;
					case "-m":	motif_miss=false; String[] tmp = args[option+1].split(",");
									 motifs = new String[tmp.length];
									 limits = new int[tmp.length];
									 for(int i=0; i < tmp.length; i++) {
										 if(tmp[i].contains("{")) {
											 String left = tmp[i].split("\\{")[0];
											 String right = tmp[i].split("\\{")[1];
											 motifs[i] = left;
											 limits[i] = Integer.parseInt(right.substring(0, right.length()-1));
										 } else {
											 motifs[i] = tmp[i];
											 limits[i] = -1;
										 }
									 }
							break;
					case "-c": outfile_header=args[option+1];break;
					case "-h": case "--help": case "--version": case "-v": help_requested = true;
				}
			}
		
			System.out.println(header_message());
			System.out.println(setting_message(files, names, outfile, motifs));
				
			if(help_requested) {
				System.out.println(usage_message());
			}
//			System.exit(0);
			// Check input parameters.
			if(input_miss || output_miss || motif_miss) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int i=1;
				System.err.println("\n\nList of parameters missing:");
				if(input_miss) System.err.println(i+++". Input files are missing.");
				if(output_miss) System.err.println(i+++". Output file is missing.");
				if(motif_miss) System.err.println(i+++". Motif(s) is/are missing.");
				System.exit(1);
			}
			
			String file_dont_exist_err = "";
			for(int i = 0; i < files.length; i++) {
				if(input_file_dont_exist[i]) {
					file_dont_exist_err+=(files[i]+" don't exist\n");
				}
			}
			if(!file_dont_exist_err.isEmpty()) {
				System.err.println(file_dont_exist_err);
			}
			
			// Standard naming if custom names are missing.
			if(!custom_names) {
				for(int i = 0; i < files.length; i++) {
					names[i] = new File(files[i]).getName();
					names[i] = names[i].substring(0,names[i].lastIndexOf("."));
				}
			}
			MultiSequencesMultiRegionsMultiMotifsMatcher cmm = 
					new MultiSequencesMultiRegionsMultiMotifsMatcher(names, files, motifs, limits, outfile, outfile_header);
			cmm.run();
		}
	}
	public static String usage_message() {
		return "MotifMatching (0.1.0)\n" + 
				"Copyright (c) 2020 Ali T. Abdallah, All rights reserved.\n" + 
				"-------------------------------------------------------------------------------\n" + 
				"\n" + 
				"'MotifMatching' detects and counts all matches of one or multiple motifs in one\n" + 
				"or multiple regions of one or multiple gene products.\n" + 
				"\n" + 
				"The commands below should be preceded by 'java -jar':\n" + 
				"\n" + 
				"Usage:\n" + 
				"    MotifMatching.jar\n" + 
				"        -i INPUT_FILE_PATHS\n" + 
				"	[-n names]\n" + 
				"        -o OUTFILE\n" + 
				"	-m MOTIFS_DEFINITION\n" + 
				"        [options]\n" + 
				"    MotifMatching -h | --help | --version\n" + 
				"\n" + 
				"Arguments:\n" + 
				"    i  	a list of comma separated absolute paths of fasta files each of which\n" + 
				"	containing one or multiple sequences from the same genomic region.\n"+
				"   The header is expected to follow the fasta format guidelines, " + 
				"    n   a list of comma separated names of these files/regions [optional]\n" + 
				"	otherwise the names of the files are taken as standard names.\n" + 
				"    o 	the absolute path of the output file.\n" + 
				"    m   A list of regular expressions defining the motifs to search for.\n" + 
				"	Optionally it is possible to add to each motif a size limitation\n" + 
				"	by adding {size} to each motif definition in the list. For example:\n" + 
				"	[CT]CCC*[CT]CCC*{19},[CT]CCC*[CT]CCC*[CT]CCC*[CT]CCC{38}.\n" + 
				"	In this example the algorithm will look for all instances of the first\n" + 
				"	motif having a size less than 19 and all instances having a size less\n" + 
				"	than 38.";
	}
	
	public static String header_message() {
		return "    ===========  __  __       _   _  __ _____                     _               \n" + 
				"      =====  	|  \\/  |     | | (_)/ _/ ____|                   | |              \n" + 
				"       =====	| \\  / | ___ | |_ _| || (___   ___  __ _ _ __ ___| |__   ___ _ __ \n" + 
				" 	=====	| |\\/| |/ _ \\| __| |  _\\___ \\ / _ \\/ _` | '__/ __| '_ \\ / _ \\ '__|\n" + 
				"         =====	| |  | | (_) | |_| | | ____) |  __/ (_| | | | (__| | | |  __/ |   \n" + 
				" 	  ===== |_|  |_|\\___/ \\__|_|_||_____/ \\___|\\__,_|_|  \\___|_| |_|\\___|_|   \n" + 
				"    ===========\n" + 
				"      v 0.1.0";
	}
	
	public static String space(int i) {
		String space= "";
		for(int k = 0; k < i; k++) {
			space+=" ";
		}
		return space;
	}
	
	public static String setting_message(String[] input_files, String names[], String outfile, String[] motifs) {
		String setting= "//====================================== MotifSearcher setting =======================================\\\\\n";
//		System.out.println(setting.length());
		setting 	 += "||"+space(100)+"||\n";
		String input_text = input_files(input_files);
		setting += input_text+"";
		setting 	 += "||"+space(100)+"||\n";
		String input_text_names = input_files_names(names);
		setting += input_text_names+"";
		setting 	 += "||"+space(100)+"||\n";
		String outfile_text = output_file(outfile);
		setting += outfile_text+"";

		setting 	 += "||"+space(100)+"||\n";
		String motifs_text = motifs(motifs);
		setting += motifs_text+"";
		setting 	 += "||"+space(100)+"||\n";

		setting 	 += "\\\\=============================== https://github.com/ATA82/MotifSearcher =============================//";
		return setting;
	
	}

	private static String input_files(String[] input_files) {
		String input_text  = "||"+space(38)+"Input files :";
		String inDir = " InDir  : "+new File(input_files[0]).getParent()+"";
		inDir = inDir+space(50-inDir.length()-1)+"||\n";
		input_text+=inDir;
		for(int input = 0; input < input_files.length; input++) {
			String files = "";
			files += "||"+space(52)+(input+1)+". File: "+new File(input_files[input]).getName()+"";
			files += space(102-files.length())+"||\n";
			input_text +=files;
		}
		return input_text;
	}
	
	private static String output_file(String out_file) {
		String input_text  = "||"+space(37)+"Output files :";
		String inDir = " OutDir  : "+new File(out_file).getParent()+"";
		inDir = inDir+space(50-inDir.length()-1)+"||\n";
		input_text+=inDir;
		String files = "";
		files += "||"+space(52)+"File    : "+new File(out_file).getName()+"";
		files += space(102-files.length())+"||\n";
		input_text +=files;
		return input_text;
	}
	
	private static String motifs(String[] motifs) {
		String input_text  = "||"+space(43)+"Motifs :";
		String inDir = "";
		inDir = inDir+" List of Motifs"+space(35-inDir.length()-1)+"||\n";
		input_text+=inDir;
		for(int input = 0; input < motifs.length; input++) {
			String files = "";
			files += "||"+space(52)+(input+1)+". Motif: "+new File(motifs[input]).getName()+"";
			files += space(102-files.length())+"||\n";
			input_text +=files;
		}
		return input_text;
	}
	
	private static String input_files_names(String[] input_files_names) {
		String input_text  = "||"+space(24)+"Names of genomics regions :";
		String inDir = "";
		inDir = inDir+" List of regions"+space(34-inDir.length()-1)+"||\n";
		input_text+=inDir;
		for(int input = 0; input < input_files_names.length; input++) {
			String files = "";
			files += "||"+space(52)+(input+1)+". Region: "+new File(input_files_names[input]).getName()+"";
			files += space(102-files.length())+"||\n";
			input_text +=files;
		}
		return input_text;
	}

	
}
