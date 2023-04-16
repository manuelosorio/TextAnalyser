package io.github.manuelosorio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.util.*;

/**
 * This class handles the core functionality for the Text Analyzer application.
 * It will read the text from a web page and analyze it.
 * @see TextAnalyzerGui
 */
public class TextAnalyzerCore{

    private final Database db;
    int poemId;
    private List<Map.Entry<String, Integer>> sortedList;
    private boolean dataExists;
    private String[] words;
    /**
     * This is the constructor for the TextAnalyzerCore class.
     * @param webUrl The url to read the text from.
     */
    public TextAnalyzerCore(String webUrl) throws Exception {
        this.db = new Database(Database.sqlUrl, Database.databaseName, Database.user, Database.password, false);
        this.readWebPage(webUrl);
    }
    public TextAnalyzerCore() {
        this.db = new Database(Database.sqlUrl, Database.databaseName, Database.user, Database.password, false);
    }
    private void readWebPage(String webURL) throws Exception {
        Object poemCreationResults = this.db.executeQuery("INSERT INTO poem(url) VALUES('" + webURL + "')");
        this.dataExists = poemCreationResults instanceof Integer && (int) poemCreationResults == 1062;

        ResultSet rs = this.db.executeQuery("SELECT id FROM poem WHERE url = '" + webURL + "'");
        if (rs != null && rs.next()) {
            this.poemId = rs.getInt("id");
            System.out.println("Poem ID: " + this.poemId);
        } else {
            throw new Exception("Unable to retrieve poem ID from the database");
        }

        URL url = new URL(webURL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder text = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            text.append(line).append("\n");
        }
        reader.close();

        this.words = this.strip(text);

        this.setSortedList();
    }

    private String[] strip(StringBuilder text) {
        this.words = text.toString().toLowerCase()
                .split("<h1>")[1] // Ignores the Text Before the title
                .split("<!--end chapter-->")[0] // Ignores the text at the end of the title
                .replaceAll("<[^>]*>", "") // Strips away all html tags
                .replaceAll("&(#?\\w+);", " ") // Removes all html entities
                .replaceAll("[^a-zA-Z0-9\\s']+", "")
                .replaceAll("\n", " ") // This ensures the string is all
                // in the same line preventing possible issues.
                .split("\\s+");
        return words;
    }

    private void setSortedList() {

        if (!this.dataExists) {
            for (String word : this.words) {
                try {
                    this.db.executeQuery("INSERT INTO word(word, poem_id, frequency) VALUES('" + word + "', " + this.poemId
                            + ", 1) ON DUPLICATE KEY UPDATE frequency = frequency + 1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.sortedList = new ArrayList<>();
        try {
            ResultSet rs = this.db.executeQuery("SELECT word, frequency FROM word WHERE poem_id = "
                    + this.poemId + " ORDER BY frequency DESC, word ASC");
            while (rs.next()) {
                this.sortedList.add(new AbstractMap.SimpleEntry<>(rs
                        .getString("word"), rs.getInt("frequency")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Map.Entry<String, Integer>> getTopListEntities(int limit) {
        return this.sortedList.subList(0, limit);
    }
    public List<Map.Entry<String, Integer>> getSortedList() {
        return sortedList;
    }

    public void setUrl(String webUrl) throws Exception {
        this.readWebPage(webUrl);
    }

}