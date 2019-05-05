import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*** This class contains the content of each reuter file. The reuter file is split into articles where each article contains an
 * docid and an array of tokens
 * ***/
public class ReuterFile {

    private String filename;
    private String content;
    private List<Article> articles;

    public ReuterFile(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * when we call this methods the content instance is set
     * @return String value containing the content of the file
     * @throws IOException
     * @throws URISyntaxException
     */
    public String readReuterFile() throws IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url= classLoader.getResource(this.filename);
        byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
        return new String(encoded, Charset.forName("utf-8"));
    }

    /**
     * This method extracts the doc ID and its content and creates an Article object.
     * Given the content of the article we create our tokens by splitting the body.
     * Then we call the normalize method that will normalize the tokens using compression techniques
     * @return a List of Articles contained in a reuter file
     */
    public List<Article> readAllArticles() {
        //get Article's docid and content
        articles = new ArrayList<>();
        List<String> articleBody = new ArrayList<>();
        List<Article> articlesInFile = new ArrayList<>();
        articleBody.addAll(Arrays.asList(this.content.split("(?=(?:<REUTERS)(?:.)+(?:NEWID=\".+\">))+")));
        articleBody.remove(0);
        for (String body: articleBody) {
            Document doc = Jsoup.parse(body.split("\n")[0]);
            Element reuters = doc.select("REUTERS").first();
            String docID = reuters.attr("NEWID");
            body = Jsoup.clean(body, Whitelist.none());
            String[] tokens = body.split("\\s");
            Article article = new Article(docID, tokens);
            List<String> updatedTokens = article.normalize(tokens);
            article.setNormalizedTokens(updatedTokens);
            this.articles.add(article);
            articlesInFile = getArticles();
        }

        return articlesInFile;
    }
}
