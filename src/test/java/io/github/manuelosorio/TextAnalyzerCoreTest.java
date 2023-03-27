package io.github.manuelosorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

public class TextAnalyzerCoreTest {
    TextAnalyzerCore textAnalyzerCore;

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
}
