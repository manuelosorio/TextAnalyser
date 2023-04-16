package io.github.manuelosorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class contains test cases for the TextAnalyzerCore class.
 * It tests various aspects of the TextAnalyzerCore functionality,
 * such as reading from a URL, generating a sorted list of words and frequencies,
 * and returning a specified number of top frequent words.
 *
 * @see TextAnalyzerCore
 */
public class TextAnalyzerCoreTest {
    TextAnalyzerCore textAnalyzerCore;

    TextAnalyzerCoreTest() {
        new Database("jdbc:mysql://localhost:3306", "osorio_m_word_occurrences_database_test",
                "root", "", true);
    }

    /**
     * Initializes a new instance of the TextAnalyzerCore class before each test.
     */
    @BeforeEach
    void setup(){
        this.textAnalyzerCore = new TextAnalyzerCore();
    }

    /**
     * Test case to ensure the getSortedList() method returns a non-null list.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void ShouldReturnList() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        assert this.textAnalyzerCore.getSortedList() != null;
    }

    /**
     * Test case to check if the list returned by the getSortedList() method is of type List<Map.Entry<String, Integer>>.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldReturnStringIntegerList() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        assert this.textAnalyzerCore.getSortedList()
                instanceof List<Map.Entry<String, Integer>>;
    }

    /**
     * Test case to ensure the list returned by the getSortedList() method has more than one element.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldReturnAListWithMoreThanOneElement() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        assert this.textAnalyzerCore.getSortedList().size() > 1;
    }

    /**
     * Test case to check if the list returned by the getTopListEntities(20) method has exactly 20 elements.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldReturnListOf20Words() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        assert this.textAnalyzerCore.getTopListEntities(20).size() == 20;
    }

    /**
     * Test case to verify if the list returned by the getSortedList().subList(0, 20) method matches the expected list of the 20 most frequent words.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void shouldMatchList() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("the",57);
        map.put("and",38);
        map.put("i",32);
        map.put("my",24);
        map.put("of",22);
        map.put("that",18);
        map.put("this",16);
        map.put("a",15);
        map.put("door",14);
        map.put("is",11);
        map.put("raven",11);
        map.put("nevermore",11);
        map.put("chamber",11);
        map.put("bird",10);
        map.put("on",10);
        map.put("me",9);
        map.put("at",8);
        map.put("by",8);
        map.put("from",8);
        map.put("lenore",8);
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> {
            if (o1.getValue().equals(o2.getValue())) {
                return o1.getKey().compareTo(o2.getKey());
            }
            return o2.getValue().compareTo(o1.getValue());
        });
        List<Map.Entry<String, Integer>> actualList = this.textAnalyzerCore.getSortedList().subList(0, 20);
        assert(actualList.containsAll(list) && list.containsAll(actualList));

    }
}
