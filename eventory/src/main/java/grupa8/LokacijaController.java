package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        List<String> selectedLocationNames = FilterDefinition.getInstance().getLocationNames();
        for (String mjesto : mjesta) {
            CheckBox checkBox = new CheckBox(mjesto);
            checkBox.getStyleClass().add("custom-checkbox");
            checkBox.setUserData(mjesto);
            if (selectedLocationNames.contains(mjesto)) {
                checkBox.setSelected(true);
            }
            lokacije.getChildren().add(checkBox);
        }
    }

    @FXML
    private void prikaziOdabranaMjesta(ActionEvent event) {
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }
}
