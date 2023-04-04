package io.github.manuelosorio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * This class handles the core functionality for the Text Analyzer application.
 * It will read the text from a web page and analyze it.
 * @see TextAnalyzerGui
 */
public class TextAnalyzerCore {

    private List<Map.Entry<String, Integer>> sortedList;

    private String[] words;

    /**
     * This is the constructor for the TextAnalyzerCore class.
     * @param webUrl The url to read the text from.
     * @throws Exception
     */
    public TextAnalyzerCore(String webUrl) throws Exception {
        this.readWebPage(webUrl);

    }
    public TextAnalyzerCore() {}
    private void readWebPage(String webURL) throws Exception {
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
        HashMap<String, Integer> wordCount = new HashMap<>();
        for (String word : this.words) {
            wordCount.merge(word, 1, Integer::sum);
        }
        this.sortedList = new ArrayList<>(wordCount.entrySet());
        this.sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
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