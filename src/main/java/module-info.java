module io.github.manuelosorio {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens io.github.manuelosorio to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    opens io.github.manuelosorio.controllers to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;

}
