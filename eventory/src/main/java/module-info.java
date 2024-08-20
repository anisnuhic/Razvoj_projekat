module grupa8 {
    requires javafx.controls;
    requires javafx.fxml;

    opens grupa8 to javafx.fxml;
    exports grupa8;
}
