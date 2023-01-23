package io.github.manuelosorio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class TextAnalyzer {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder text = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            text.append(line).append("\n");
        }
        reader.close();

        String[] words = text.toString().split("<h1>")[1] // Ignores the Text Before the title
                    .split("<!--end chapter-->")[0] // Ignores the text at the end of the title
                    .replaceAll("<[^>]*>", "") // Strips away all html tags
                    .replaceAll("&(#?\\w+);", " ") // Removes all html entities
                    .replaceAll("[\\p{P}$+<=>^`|~]", "") // Remove all punctuation
                    .replaceAll("\n", " ") // This ensures the string is all
                                                            // in the same line preventing possible issues.
                    .split("\\s+");


        HashMap<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            wordCount.merge(word, 1, Integer::sum);
        }
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordCount.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int wordIndex = 1;
        for (Object wordObject : sortedList) {
            if (wordIndex <= 20) {
                System.out.println(wordIndex + ".\t" + wordObject);
            }
            wordIndex++;
        }
    }
}