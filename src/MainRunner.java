import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainRunner {

    public static void main(String[] args) {

        // going through all the reuter files and getting our list of Articles
        //Article is an object that contains the docId and its tokens
        try {
            List<Article> allArticles = new ArrayList<>();
            for (int i = 0; i < 22; i++) {
                ReuterFile file = new ReuterFile("reut2-0" + String.format("%02d", i) + ".sgm");
                String content = file.readReuterFile();
                file.setContent(content);
                allArticles.addAll(file.readAllArticles());
            }

            Iterator<Article> documentStream = allArticles.iterator();
            SPIMI spimi = new SPIMI(500);
            spimi.SPIMIInvert(documentStream);
            Map<String, Map<Integer, Integer>> dict = spimi.readBigDictionary();
            spimi.writeBigDictionaryToDisk(dict);

          //  runTestQueries(spimi);
            runMyFriendsQueries(spimi);
           // runDEMOQueries(spimi);

        } catch(IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /*public static void runTestQueries(SPIMI spimi) {
        //RUN QUERIES
        Query query = new Query(spimi);
        System.out.println("----Running Test Queries----");
        System.out.println();
        //Jimmy Carter
        String queryTerm1 = "Jimmy AND Carter";
        String result1 = query.returnANDQueryResults(queryTerm1).toString();
        System.out.print(queryTerm1 + ": " + result1);
        System.out.println();
        //Green Party
        String queryTerm2 = "Green AND Party";
        String result2 = query.returnANDQueryResults(queryTerm2).toString();
        System.out.print(queryTerm2 + ": " + result2);
        System.out.println();
        //Innovations in telecommunication
        String queryTerm3 = "Innovations AND in AND telecommunication";
        String result3 = query.returnANDQueryResults(queryTerm3).toString();
        System.out.print(queryTerm3 + ": " + result3);
        System.out.println();
        //environmentalist ecologist
        String queryTerm4 = "environmentalist OR ecologist";
        String result4 = query.returnORQueryResults(queryTerm4).toString();
        System.out.print(queryTerm4 + ": " + result4);
    }*/

    public static void runMyFriendsQueries(SPIMI spimi) {
        //AND Query by Corey Levine
        Query query = new Query(spimi);
        System.out.println();
        System.out.println();
        System.out.println("----Running Friends Queries----");
        /*System.out.println();
        String queryTerm = "ample AND emotional";
        String result = query.returnANDQueryResults(queryTerm).toString();
        System.out.print(queryTerm + ": " + result);*/

        System.out.println();
        //Single Query by Senaga Velupillai
        String queryTerm1 = "drug";
        String result1 = query.returnSingleQueryResults(queryTerm1).toString();
        System.out.print(queryTerm1 + ": " + result1);

        /*System.out.println();
        //OR Query by Mukulika Dey
        String queryTerm2 = "Jupiter OR transparent";
        String result2 = query.returnORQueryResults(queryTerm2).toString();
        System.out.print(queryTerm2 + ": " + result2);*/

    }

    /*public static void runDEMOQueries(SPIMI spimi) {
        System.out.println();
        System.out.println();
        System.out.println("----Running DEMO Queries----");
        System.out.println();
        Query query = new Query(spimi);
        String queryTerm = "forget";
        String result = query.returnSingleQueryResults(queryTerm).toString();
        String queryTerm1 = "forget AND this";
        String result1 = query.returnANDQueryResults(queryTerm1).toString();
        String queryTerm2 = "snails OR prize OR firestorm";
        String result2 = query.returnORQueryResults(queryTerm2).toString();


        System.out.print(queryTerm + ": " + result);
        System.out.print(queryTerm1 + ": " + result1);
        System.out.print(queryTerm2 + ": " + result2);


    }*/
}


