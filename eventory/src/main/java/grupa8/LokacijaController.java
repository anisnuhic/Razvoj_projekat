package grupa8;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.*;
public class LokacijaController {
    @FXML
    private VBox lokacije;
    private void prikaziMjesta(List<String> mjesta) {
        lokacije.getChildren().clear();

        // Create and add new CheckBox elements
        for (String mjesto : mjesta) {
            CheckBox checkBox = new CheckBox(mjesto);
            checkBox.getStyleClass().add("custom-checkbox");
            checkBox.setUserData(mjesto);
            lokacije.getChildren().add(checkBox);
        }
    }

    @FXML
    private void initialize(){
        List<String> listaMjesta = new ArrayList<String>();
        listaMjesta.add("Tuzla");
        listaMjesta.add("Lukavac");
        listaMjesta.add("Gracanica");
        prikaziMjesta(listaMjesta);
    }
}
