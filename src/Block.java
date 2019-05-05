import java.util.List;
import java.util.Map;

public class Block {
    /**
     * This class contains the details of a block that will be outputted to the block text file
     */
    private Map<String, Map<Integer, Integer>> dictionary;
    private int termFrequency = 0;
    private int block_number = 0;

    public Block() {}

    public Block(Map<String, Map<Integer, Integer>> dictionary, int block_number) {
        this.dictionary = dictionary;
        this.block_number = block_number;
    }

    public Map<String, Map<Integer, Integer>> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Map<String, Map<Integer, Integer>> dictionary) {
        this.dictionary = dictionary;
    }

    public int getBlock_number() {
        return block_number;
    }

    public void setBlock_number(int block_number) {
        this.block_number = block_number;
    }

    public int getTermFrequency() {
        return this.termFrequency;
    }

    public void setTermFrequency(int termFrequency) {
        this.termFrequency = termFrequency;
    }

}

