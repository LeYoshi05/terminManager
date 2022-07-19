module de.leghast {
    requires javafx.controls;
    requires javafx.fxml;

    opens de.leghast to javafx.fxml;
    exports de.leghast;
}
