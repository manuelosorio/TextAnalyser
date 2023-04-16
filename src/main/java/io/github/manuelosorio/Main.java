package io.github.manuelosorio;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        new Database("jdbc:mysql://localhost:3306", "osorio_m_word_occurrences_database",
                "root", "", true);
        Application.launch(TextAnalyzerGui.class, args);

    }
}