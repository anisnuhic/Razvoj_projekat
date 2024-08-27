package grupa8;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LokacijaController {

    @FXML
    private VBox lokacije;

    private PrimaryController primaryController; // Referenca na PrimaryController

    @FXML
    private void initialize() {
        List<String> listaMjesta = new ArrayList<>();
        listaMjesta.add("Tuzla");
        listaMjesta.add("Lukavac");
        listaMjesta.add("Gracanica");
        prikaziMjesta(listaMjesta);
    }

    private void prikaziMjesta(List<String> mjesta) {
        lokacije.getChildren().clear();

        for (String mjesto : mjesta) {
            CheckBox checkBox = new CheckBox(mjesto);
            checkBox.getStyleClass().add("custom-checkbox");
            checkBox.setUserData(mjesto);
            lokacije.getChildren().add(checkBox);
        }
    }

    @FXML
    private void prikaziOdabranaMjesta() {
        List<String> odabranaMjesta = new ArrayList<>();
    
        for (var node : lokacije.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    odabranaMjesta.add((String) checkBox.getUserData());
                }
            }
        }
    
        // Poziv metode u PrimaryController za ažuriranje prikaza odabranih mjesta
        primaryController.updateLocations(odabranaMjesta);
    }
    

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }
}
