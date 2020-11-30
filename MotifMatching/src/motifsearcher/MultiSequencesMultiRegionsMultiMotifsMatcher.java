package motifsearcher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

/**
 * This class use the MultiSequenceMultiMotifMatcher to compute matches of multiple genomics products in (optionally) 
 * multiple genomic regions.
 * 
 * For example: if we have three types of sequences: 3utr, 5utr and cds. Then this class will generate a table
 * with one row for each transcript and information about the number matches in these three regions for each of the 
 * desired motifs. Additional columns deliver information about all matching subsequences and their positions.
 * 
 * @author Ali T. Abdallah
 * @since jdk1.8
 * @version 08.05.2020
 */

public class MultiSequencesMultiRegionsMultiMotifsMatcher {
	
	HashMap<Integer, String> id2file;
	HashMap<Integer, String> id2name;
	String outfile;
	String outfile_header;
	int[] limits;
	String[] motifs;
	
	public MultiSequencesMultiRegionsMultiMotifsMatcher(String[] names, String[] files, String[] motifs, int[] limits, String outfile, String outfile_header) {
		id2file= new HashMap<Integer, String>();
		id2name= new HashMap<Integer, String>();
		
		for(int i=0; i < names.length; i++) {
			id2file.put(i, files[i]);
			id2name.put(i, names[i]);
		}
		this.outfile = outfile;
		this.outfile_header = outfile_header;
		this.limits = limits;
		this.motifs = motifs;
	}
	
	public void run() {
		try {
			// Initialize scanners for all files of the different regions.
			Vector<Scanner> sequence_scanners = new Vector<Scanner>();
			for(int i = 0; i < id2file.size(); i++) {
				sequence_scanners.add(new Scanner(new File(id2file.get(i))));
			}
			// HashMap to map each transcript id to a vector of strings containing the sequences from the
			// different genomic regions (stored in different files).
			HashMap<String, Vector<String>> hm = new HashMap<String, Vector<String>>();
			int update=1;
			// Loop over all sequence regions files.
			for(int i = 0; i < id2name.size(); i++) {
				// Select the corresponding scanner.
				Scanner sequence_scanner = sequence_scanners.get(i);
				// Select the corresponding name of the region type.
				String sequence_name = id2name.get(i);
				// Read the first Sequence Id from the fasta file.
				String id = sequence_scanner.nextLine();
				// As long as the file is not fully processed.
				while(sequence_scanner.hasNextLine()) {
					// Generate a tracker of the next id.
					String id_tracker = "";
					// Initialize a sequence variable to collect the sequence of the current genomic sequence id 
					String sequence = "";
				
					// Make sure it is the header line of the sequence.
					// If this is the case, collect the sequence of the current id until you detect the next id.
					if(id.startsWith(">")) {
						while(sequence_scanner.hasNextLine() && !(id_tracker=sequence_scanner.nextLine()).startsWith(">")) {
							sequence += id_tracker;
						}
					} 
					// Otherwise look at the next line to find the header line.
					else if(sequence_scanner.hasNextLine()) {
						id = sequence_scanner.nextLine();
						continue;
					}
					
					// If we don't have an entry in the hashmap for this specific genomic product
					if(hm.get(id) == null) {
						// then create an empty vector and add the collected sequence of the current id to this vector
						// then make an entry for this genomic product mapping its id to the vector.
						Vector<String> sequences = new Vector<String>();
						sequences.add(sequence_name+"_"+sequence);
						hm.put(id, sequences);
					} else {
						// Otherwise, add the current collected sequence to existing entry. 
						hm.get(id).add(sequence_name+"_"+sequence);
					}
					
					// update the current id.
					if(sequence_scanner.hasNextLine())
						id = id_tracker;
				}
			}
			
			// Once finished reading the files and creating the hashmap
			// Intialize a FileWriter.
			new File(outfile).getParentFile().mkdirs();
			FileWriter fw = new FileWriter(outfile);
			if(outfile_header!=null) fw.write(outfile_header+"\n");
			
			System.out.println("Processing Information for "+id2name+":");
			// Go through all keys of all HashMap entries.
			for(String ids: hm.keySet()) {
				// Create two arrays of the sequences of the same genomics product from different regions and 
				// corresponding region names (for the output)
				String sequences[] = new String[hm.get(ids).size()];
				String[] names = new String[sequences.length];
				// Add the HashMap information to the arrays.
				for(int i = 0; i < sequences.length; i++) {
					sequences[i] = hm.get(ids).get(i);
					names[i] = id2name.get(i);
				}
				
				// Now find all matches of each of the motifs in these multiple regions of this specific gene product.
				MultiRegionsMultiMotifsMatcher mms = 
						new MultiRegionsMultiMotifsMatcher(ids, sequences, 0, limits, motifs);
				mms.run();
				
				// If you find some mathces in at least one product.
				
				if(!mms.nomatches()) {
					// write a summary line of this genomic product to the output file.
					fw.write(mms.toString(names).trim()+"\n");	
				}
				if(update%500==0 || update==hm.keySet().size())
					System.out.println(update+" from "+hm.keySet().size() + " genomics products processed");
				update++;
			}
			System.out.println();
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
	}
	

	
}