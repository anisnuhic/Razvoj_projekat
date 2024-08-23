module grupa8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens grupa8 to javafx.fxml;
    exports grupa8;
}
