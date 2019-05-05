import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Article {
    /**
     * Contains the details of an Article. An article has a docID, unnormalized tokens and normalized tokens
     */
    private String docID ;
    private String[] tokens;
    private List<String> normalizedTokens;

    public Article() {}
    public Article(String docID, String[] tokens) {
        this.docID = docID;
        this.tokens = tokens;
    }

    public Article(String docID, String[] tokens, List<String> normalizedTokens) {
        this.docID = docID;
        this.tokens = tokens;
        this.normalizedTokens = normalizedTokens;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    /**
     * call different compression technique methods.
     * Such as removing stopwords of 30 words and 150 words
     * Removing numbers, capital letters and special characters
     * @param tokens
     * @return a list of normalized tokens
     */
    public List<String> normalize(String[] tokens) {

        this.normalizedTokens = new ArrayList<>();
        for (String token: tokens) {
            //removes digits
            token = token.replaceAll("[0-9]+", "");
            //removes captilizations
            token = token.toLowerCase();
            //remove special characters
            token = token.replaceAll("[^a-zA-Z0-9]", "");
            token = remove30StopWords(token);
            token = remove150StopWords(token);
            this.normalizedTokens.add(token);
        }
        //remove empty tokens
        this.normalizedTokens.removeAll(Collections.singleton(null));
        this.normalizedTokens.removeAll(Collections.singleton(""));
        return this.normalizedTokens;
    }

    public List<String> getNormalizedTokens() {
        return normalizedTokens;
    }

    public void setNormalizedTokens(List<String> normalizedTokens) {
        this.normalizedTokens = normalizedTokens;
    }

    public static String remove150StopWords(String token) {
        String updatedToken = token;
        String[] stopwords = {"a", "about", "above", "after", "again", "all", "am", "an", "and", "any",
                "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both",
                "but", "by", "cannot", "could", "did", "didn't", "do", "does", "doesn't", "doing",
                "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't",
                "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "herself",
                "him", "himself", "his", "how", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is",
                "it", "it's", "its", "itself", "me", "more", "most", "my", "myself", "no",
                "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves",
                "out", "over", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so",
                "some", "such", "than", "that", "the", "their", "theirs", "them", "themselves", "then",
                "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those",
                "through", "to", "" +
                "too", "under", "until", "up", "very", "was", "we", "we'd", "we're",
                "were", "what", "when", "when's", "where", "which", "while",
                "who", "whom", "why", "why's", "with", "would", "wouldn't", "you", "you'd", "you'll",
                "you're", "your", "yourself", "yourselves"};

        for (String word: stopwords) {
            updatedToken = token.replaceAll(word, "");
        }

        return updatedToken;
    }

    public static String remove30StopWords(String token) {
        String updatedToken = token;
        String[] stopwords = { "a", "after", "am", "an", "are", "at", "be", "because", "been", "could",
                "did", "do", "each", "few", "for", "go", "give", "get", "he", "have", "her", "him", "i",
                "if", "is", "it", "into", "keep", "last", "less"
                };
        for (String word: stopwords) {
            updatedToken = token.replaceAll(word, "");
        }
        return updatedToken;
    }
}
