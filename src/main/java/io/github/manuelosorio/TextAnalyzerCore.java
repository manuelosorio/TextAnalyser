package io.github.manuelosorio;

import io.github.manuelosorio.logger.LoggerAbstraction;
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

    private final LoggerAbstraction logger;
    /**
     * This is the constructor for the TextAnalyzerCore class.
     * It initializes the database and reads the text from the provided web URL.
     * @param webUrl The URL to read the text from.
     * @throws Exception If there is an issue reading the web page or retrieving the poem ID from the database.
     */
    public TextAnalyzerCore(String webUrl) throws Exception {
        this.logger = new LoggerAbstraction(TextAnalyzerCore.class.getName());
        this.db = new Database(Database.sqlUrl, Database.databaseName, Database.user, Database.password, false);
        this.readWebPage(webUrl);
    }

    /**
     * This constructor initializes the TextAnalyzerCore class with the default database settings.
     */
    public TextAnalyzerCore() {
        this.logger = new LoggerAbstraction(TextAnalyzerCore.class.getName());
        this.db = new Database(Database.sqlUrl, Database.databaseName, Database.user, Database.password, false);
    }

    private void readWebPage(String webURL) throws Exception {
        Object poemCreationResults = this.db.executeQuery("INSERT INTO poem(url) VALUES('" + webURL + "')");
        this.dataExists = poemCreationResults instanceof Integer && (int) poemCreationResults == 1062;

        ResultSet rs = this.db.executeQuery("SELECT id FROM poem WHERE url = '" + webURL + "'");
        if (rs != null && rs.next()) {
            this.poemId = rs.getInt("id");
        } else {
            this.logger.severe("Unable to retrieve poem ID from the database");
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
    /**
     * Strips away all the unwanted parts of the text, such as HTML tags, entities, and non-word characters,
     * and splits the resulting text into an array of words.
     * @param text A StringBuilder containing the raw text from the web page.
     * @return An array of words after stripping away unwanted parts of the text.
     */
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
    /**
     * Sets the sorted list of words and their frequencies. If the data does not already exist in the database,
     * it inserts the words and their frequencies into the database. Then, it retrieves the sorted list of words
     * and their frequencies from the database.
     */
    private void setSortedList() {

        if (!this.dataExists) {
            for (String word : this.words) {
                try {
                    this.db.executeQuery("INSERT INTO word(word, poem_id, frequency) VALUES('" + word + "', " + this.poemId
                            + ", 1) ON DUPLICATE KEY UPDATE frequency = frequency + 1");
                } catch (Exception e) {
                    this.logger.severe("Unable to insert word into database: " + e.getMessage());
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
            this.logger.severe("Unable to retrieve sorted list from database: " + e.getMessage());
        }
    }
    /**
     * Returns a list of the top 'limit' words and their frequencies.
     * @param limit The number of top words to return.
     * @return A list of the top 'limit' words and their frequencies.
     */
    public List<Map.Entry<String, Integer>> getTopListEntities(int limit) {
        return this.sortedList.subList(0, limit);
    }
    /**
     * Returns the sorted list of words and their frequencies.
     * @return A list of words and their frequencies, sorted by frequency and then alphabetically.
     */
    public List<Map.Entry<String, Integer>> getSortedList() {
        return sortedList;
    }
    /**
     * Sets the URL for the TextAnalyzerCore class and reads the web page content.
     * @param webUrl The URL to read the text from.
     * @throws Exception If there is an issue reading the web page or retrieving the poem ID from the database.
     */
    public void setUrl(String webUrl) throws Exception {
        this.readWebPage(webUrl);
    }

}