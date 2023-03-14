package io.github.manuelosorio.controls;

import io.github.manuelosorio.TextAnalyzerCore;

import java.util.List;
import java.util.Map;

// setup controller class
public class MainController {
    // setup method to handle button click
    public void handleButtonAction() {
        System.out.println("You clicked me!");
        try {
            TextAnalyzerCore textAnalyzerCore = new TextAnalyzerCore("https://www.gutenberg.org/files/1342/1342-h/1342-h.htm");
            List<Map.Entry<String, Integer>> sortedList = textAnalyzerCore.getSortedList();
            this.loadTable(sortedList, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadTable(List<Map.Entry<String, Integer>> data, int limit) {
        int index = 0;
        for (Map.Entry<String, Integer> entry : data) {
//            if (index >= limit) {
//                break;
//            }
            System.out.println(entry.getKey() + " " + entry.getValue());
            index++;
        }
    }
}