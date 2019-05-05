import java.util.*;

public class Query {

/**
     * This class contains the functions that will run single, multikeyword queries(AND, OR) */


    private Map<String, List<Integer>> bigDictionary;
    private SPIMI spimi;

    public Query(SPIMI spimi) {
        this.spimi = spimi;
    }

/**
     *
     * @return a  big dictionary containing the posting list of a term from all the blocks together */


    public Map<String, List<Integer>> getBigDictionary() {
        return bigDictionary;
    }

    public void setBigDictionary(Map<String, List<Integer>> bigDictionary) {
        this.bigDictionary = bigDictionary;
    }

    public SPIMI getSpimi() {
        return spimi;
    }

    public void setSpimi(SPIMI spimi) {
        this.spimi = spimi;
    }

    public Map<Integer, Integer> returnSingleQueryResults(String query) {
        Map<Integer, Integer> postinglist = new HashMap<>();
        postinglist = spimi.readBigDictionary().get(query.toLowerCase());
        int score = 0;
        for (Integer docID: postinglist.keySet()) {
            score = spimi.calculateBM25Score(docID, query.toLowerCase());
            postinglist.replace(docID,score);
        }

        if (postinglist == null) {
            System.out.println("No results for the query " + query);
        }
        //postingList.sort(Comparator.naturalOrder());
        return postinglist;
    }

    /*public Map<Integer, Integer> returnANDQueryResults(String query) {
        List<Integer> postingList = new ArrayList<>();
        String [] queryTerms = query.split(" AND ");
        Map<Integer, Integer> list1 = new HashMap<>();
        Map<Integer, Integer> list2 = new HashMap<>();
        Map<Integer, Integer> list3 = new HashMap<>();

        if(queryTerms.length == 3 ) {
            list1 = spimi.readBigDictionary().get(queryTerms[0].toLowerCase());
            list2 = spimi.readBigDictionary().get(queryTerms[1].toLowerCase());
            list3 = spimi.readBigDictionary().get(queryTerms[2].toLowerCase());
            for(Integer id : list1) {
                if(list2.contains(id)){
                    if(list3.contains(id)) {
                        postingList.add(id);
                    }
                }
            }
        }

        if(queryTerms.length == 2 ) {
            list1 = spimi.readBigDictionary().get(queryTerms[0].toLowerCase());
            list2 = spimi.readBigDictionary().get(queryTerms[1].toLowerCase());
            for(Integer id : list1) {
                if(list2.contains(id)){
                    postingList.add(id);
                }
            }
        }

        if(postingList == null) {
            System.out.println("No results for the query " + query);
        }
        //Sorts the postings list from least to greatest
        postingList.sort(Comparator.naturalOrder());
        return postingList;
    }

    public List<Integer> returnORQueryResults(String query) {
        List<Integer> postingList = new ArrayList<>();
        String [] queryTerms = query.split(" OR ");

        for (String term: queryTerms) {
            if(spimi.readBigDictionary().get(term.toLowerCase()) != null) {
                postingList.addAll(spimi.readBigDictionary().get(term.toLowerCase()));
            }
        }

        if(postingList == null) {
            System.out.println("No results for the query " + query);
        }
        //sorts postings list from least to greatest
        postingList.sort(Comparator.naturalOrder());
        return postingList;
    }*/

}
