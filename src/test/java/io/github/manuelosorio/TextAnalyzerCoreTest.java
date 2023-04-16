package io.github.manuelosorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will test the TextAnalyzerCore class.
 * @see TextAnalyzerCore
 */
public class TextAnalyzerCoreTest {
    TextAnalyzerCore textAnalyzerCore;

    TextAnalyzerCoreTest() {
        new Database("jdbc:mysql://localhost:3306", "osorio_m_word_occurrences_database_test",
                "root", "", true);
    }

    @BeforeEach
    void setup(){
        this.textAnalyzerCore = new TextAnalyzerCore();
    }

    @Test
    void ShouldReturnList() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        assert this.textAnalyzerCore.getSortedList() != null;
    }
    @Test
    void shouldReturnStringIntegerList() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        assert this.textAnalyzerCore.getSortedList()
                instanceof List<Map.Entry<String, Integer>>;
    }

    @Test
    void shouldReturnAListWithMoreThanOneElement() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        assert this.textAnalyzerCore.getSortedList().size() > 1;
    }
    @Test
    void shouldReturnListOf20Words() throws Exception {
        this.textAnalyzerCore.setUrl("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        assert this.textAnalyzerCore.getTopListEntities(20).size() == 20;
    }

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
