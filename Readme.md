![HNRPK protein](Images/Motif_HNRPK.png)

# Insights Into The Mechanisms Of AML Del9q Progression
> Analysis of hnRNP K interacting RNAs in myeloid KG-1a cellsn

HNRNPK is part of the minimally deleted region (MDR) in AML del(9q). To understand the function of hnRNP K in pathogenesis of this AML subtype, we analyzed the function of hnRNP K as an RNA binding protein in myeloid cells. Co-immunoprecipitated RNAs from hnRNP K and non-related control immunoprecipitation as well as input RNA were subjected to next generation sequencing. This analysis revealed an specific interaction of hnRNP K with 1076 RNAs, among them many mRNAs encoding transcription factors involved in myeloid differentiation and AML pathogenesis.


## Installing / Getting started

**Software requirements:**</BR>

**1. Java**</BR>
Java 8

**2. R**</BR>
To run the R markdowns you need, besides the core installation of R 4.0, the following R-Libraries:</BR>
1. biomaRt
2. DT
3. genefilter
4. tidyverse
5. ggplot2
6. ggseqlogo
7. cowplot
8. DESeq2
9. RColorBrewer
10. svglite

## Usage instructions
### 1. Running Downstream analysis

### 2. Running MotifSearcher
The first step is to take the 1076 transcripts and perform Motif matching using our MotifSearcher program. In the following,</BR>
we print the help section of the program, describing its usage.

```shell
            ===========  __  __       _   _  __ _____                     _               
              =====  	|  \/  |     | | (_)/ _/ ____|                   | |              
               =====	| \  / | ___ | |_ _| || (___   ___  __ _ _ __ ___| |__   ___ _ __ 
 	        =====	| |\/| |/ _ \| __| |  _\___ \ / _ \/ _` | '__/ __| '_ \ / _ \ '__|
                 =====	| |  | | (_) | |_| | | ____) |  __/ (_| | | | (__| | | |  __/ |   
 	          ===== |_|  |_|\___/ \__|_|_||_____/ \___|\__,_|_|  \___|_| |_|\___|_|   
            ===========
              v 0.1.0

MotifMatching (0.1.0) 
Copyright (c) 2020 Ali T. Abdallah, All rights reserved.
-------------------------------------------------------------------------------

'MotifMatching' detects and counts all matches of one or multiple motifs in one
or multiple regions of one or multiple gene products.

The commands below should be preceded by 'java -jar':

Usage:
MotifMatching.jar
-i  INPUT_FILE_PATHS
	  [-n names]
-o  OUTFILE
	  -m MOTIFS_DEFINITION
    [options]
MotifMatching -h | --help | --version

Arguments:
i   a list of comma separated absolute paths of fasta files each of which
    containing one or multiple sequences from the same genomic region.\n"+
    The header is expected to follow the fasta format guidelines, " + 
n   a list of comma separated names of these files/regions [optional]
	  otherwise the names of the files are taken as standard names.
o   the absolute path of the output file.
m   A list of regular expressions defining the motifs to search for.
	  Optionally it is possible to add to each motif a size limitation
	  by adding {size} to each motif definition in the list. For example:
	  [CT]CCC*[CT]CCC*{19},[CT]CCC*[CT]CCC*[CT]CCC*[CT]CCC{38}.
	  In this example the algorithm will look for all instances of the first
	  motif having a size less than 19 and all instances having a size less
	  than 38.";
```






## Licensing

"The code in this project is licensed under MIT license."
