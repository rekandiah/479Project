import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SPIMI {
    //key: term, value: list of doc ids
    private Block current_block;
    private int max_block_size;
    private Map<Integer,Integer> blockPostingsList;
    private Iterator<Article> tokenStream;

    public Iterator<Article> getTokenStream() {
        return tokenStream;
    }

    public void setTokenStream(Iterator<Article> tokenStream) {
        this.tokenStream = tokenStream;
    }

    public SPIMI(int max_block_size) {
        this.max_block_size = max_block_size;
    }

    public int getMax_block_size() {
        return max_block_size;
    }

    public void setMax_block_size(int max_block_size) {
        this.max_block_size = max_block_size;
    }

    public Block getCurrent_block() {
        return current_block;
    }

    public void setCurrent_block(Block current_block) {
        this.current_block = current_block;
    }

    public Map<Integer, Integer> getBlockPostingsList() {
        return blockPostingsList;
    }

    public void setBlockPostingsList(Map<Integer, Integer> blockList) {
        this.blockPostingsList = blockList;
    }

    /**
     * This is the spimi algoritm where create the blocks containing the dictionary for the max number of doc ids
     * @param tokenStream is the iterator containing a collection of articles
     * @throws IOException
     */
    public void SPIMIInvert(Iterator<Article> tokenStream) throws IOException {
        Block block;
        Map<String, Map<Integer, Integer>> dictionary;
        Map<Integer, Integer> postingsList;
        setTokenStream(tokenStream);
        int term_frequency = 0;
        int block_number = 0;
        try {
            //while there still exists a next element in the collection
            while (tokenStream.hasNext()) {
                Article article = tokenStream.next();
                //checks if it has reached maximum block size. If the docID is one more than the block max size then a new block is created
                if (Integer.parseInt(article.getDocID()) % this.max_block_size == 1) {
                    block = new Block();
                    dictionary = new HashMap<>();
                    block.setBlock_number(block_number);
                    setCurrent_block(block);
                    block.setDictionary(dictionary);
                    //goes through all the terms in an articles and checks if it is in the dictionary if not it adds it.
                    //Also maps the postings list to the term
                    for (String current_term : article.getNormalizedTokens()) {
                        postingsList = new HashMap<>();
                        if (block.getDictionary().get(current_term) == null) {
                            postingsList.put(Integer.parseInt(article.getDocID()), 1);
                            block.getDictionary().put(current_term, postingsList);
                        } else {
                            ++term_frequency;
                            block.getDictionary().get(current_term).replace(Integer.parseInt(article.getDocID()), term_frequency);
                            postingsList = block.getDictionary().get(current_term);
                        }
                        postingsList.put(Integer.parseInt(article.getDocID()), term_frequency);

                       // postingsList = postingsList.stream().distinct().collect(Collectors.toList());
                        dictionary.replace(current_term, postingsList);
                    }
                    block_number++;
                    //if we're still in the same block
                } else {
                    block = getCurrent_block();
                    dictionary = block.getDictionary();
                    for (String current_term : article.getNormalizedTokens()) {
                        if (dictionary.get(current_term) == null) {
                            postingsList = new HashMap<>();
                            postingsList.put(Integer.parseInt(article.getDocID()), 1);
                            dictionary.put(current_term, postingsList);
                        } else {
                            postingsList = dictionary.get(current_term);
                            term_frequency = 0;
                            if(postingsList.get(Integer.parseInt(article.getDocID()))== null) {
                                term_frequency = 1;
                            }
                            else {
                                term_frequency = postingsList.get(Integer.parseInt(article.getDocID()));
                                term_frequency++;
                            }
                            postingsList.put(Integer.parseInt(article.getDocID()), term_frequency);
                            if(!postingsList.containsKey(Integer.parseInt(article.getDocID()))) {
                                postingsList.put(Integer.parseInt(article.getDocID()), 1);
                                dictionary.put(current_term, postingsList);
                            }
                            postingsList = dictionary.get(current_term);
                        }
                        postingsList.put(Integer.parseInt(article.getDocID()), term_frequency);

                        //remove duplicates
                       // postingsList = postingsList.stream().distinct().collect(Collectors.toList());
                        dictionary.replace(current_term, postingsList);
                    }
                }
                block.setTermFrequency(term_frequency);
                //update our dictionary
                block.setDictionary(dictionary);
                //write block to file in the disk directory
                writeBlockToDisk(block);
            }
        } catch (NoSuchElementException e) {
            e.getMessage();
        }
    }

    /**
     * Sorts the dictionary and then goes through each key and adds the key/ value pairs to a list called lines
     * after going through all the keys(docIDs) then the lines are written to the file.
     * @param block
     * @throws IOException
     */

    public void writeBlockToDisk(Block block) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        String relativePath = "disk" + fileSeparator + block.getBlock_number() + "block.txt";
        Path file = Paths.get(relativePath);
        List<String> keys = new ArrayList<String>(block.getDictionary().keySet());
        Collections.sort(keys);

        List<String> lines = new ArrayList<String>();
        for (String key : keys) {
            //Collections.sort(block.getDictionary().get(key)); //sorting the postings list
            String index = key + " : " + block.getDictionary().get(key).toString();
            lines.add(index);

        }
        try {
            Files.write(file, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeBigDictionaryToDisk(Map<String, Map<Integer, Integer>> dictionary) {
        String fileSeparator = System.getProperty("file.separator");
        String relativePath = "disk" + fileSeparator + " dictionary.txt";
        Path file = Paths.get(relativePath);
        List<String> keys = new ArrayList<String>(dictionary.keySet());
        Collections.sort(keys);

        List<String> lines = new ArrayList<String>();
        for (String key : keys) {
            //Collections.sort(block.getDictionary().get(key)); //sorting the postings list
            String index = key + " : " + dictionary.get(key).toString();
            lines.add(index);

        }
        try {
            Files.write(file, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Merges all the blocks by going through each block file.
     * we can create a big dictionary that contains the unique term and a postinglist with term containing docids from all the blocks
     * @return
     */
    public Map<String,Map<Integer, Integer>> readBigDictionary() {
        Map<String, Map<Integer, Integer>> bigDictionary = new LinkedHashMap<>();

        File dir = new File("disk");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            this.blockPostingsList = new HashMap<>();
            for (File blockFile : directoryListing) {
                try (Stream<String> stream = Files.lines(Paths.get(blockFile.toURI()))) {
                    stream.forEach(line -> {
                        String term = line.split(" :")[0];
                        this.blockPostingsList = getPostingsFromLine(line);
                        if(bigDictionary.containsKey(term)) {
                            bigDictionary.replace(term, blockPostingsList);
                        } else {
                            bigDictionary.put(term, blockPostingsList);
                        }
                    } );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
           System.out.println( "NOT A DIRECTORY");
        }
        return bigDictionary;
    }

    public int getNumOfDocs(Iterator<Article> tokenStream) {
        int count = 0;
        while(tokenStream.hasNext()) {
            count++;
        }
        return count;
    }

    public int getNumOfTokensInEachDoc(Article article) {
        return article.getNormalizedTokens().size();
    }

    public double getAvgLengthOfDoc(Iterator<Article> tokenStream) {
        double total = 0;
        int count = 0;
        double average = 0.0;
        while(tokenStream.hasNext()) {
            Article article = tokenStream.next();
            total = total + article.getNormalizedTokens().size();
            count++;
        }
        average = total/count;
        return average;
    }

    public int getTermFrequency(String term, Integer docID) {
        return readBigDictionary().get(term).get(docID);
    }

    public int getDocumentFrequency(String term) {
        return readBigDictionary().get(term).keySet().size();
    }

    public int calculateBM25Score(Integer docId, String term) {
        final double b = 0.75;
        final double k =  1.3;
        double N = getNumOfDocs(this.tokenStream);
        double average = getAvgLengthOfDoc(this.tokenStream);
        int df = getDocumentFrequency(term);
        int score = 0;
        List<Article> articles = new ArrayList<>();
        while(this.tokenStream.hasNext()) {
            articles.add(tokenStream.next());
        }

        for(Article article : articles) {
                int length = getNumOfTokensInEachDoc(article);
                double log = Math.log(N / df);
                double numerator = (k + 1) * getTermFrequency(term, docId);
                double denominator = k * ((1 - b) + b * (length / average)) + getTermFrequency(term, docId);
                score += (int) (log * (numerator / denominator));
        }

        return score;
    }

    //This parses through each line of a block file and removes the square brackets and gets the list of docIds
    private static Map<Integer, Integer> getPostingsFromLine(String line){
        Map<Integer, Integer> postingsList = new HashMap<>();

        line = line.replace('}', Character.MIN_VALUE); //equivalent of replacing with empty char
        String [] lineComponents = line.split(" : \\{"); //now we have the postings as string delimited with ','
        String [] postings = lineComponents[1].split(",");

        for(String posting : postings){
            String[] temp = posting.split("=");
            postingsList.put(Integer.parseInt(temp[0].trim()), Integer.parseInt(temp[1].trim()));
        }
        return postingsList;
    }


}

