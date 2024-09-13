package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class LokacijaController {

    @FXML
    private VBox lokacije;

    private PrimaryController primaryController; // Referenca na PrimaryController

    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
    @FXML
    private void initialize() {
        List<String> listaMjesta = new ArrayList<>();
       TypedQuery<String> query = em.createQuery("SELECT DISTINCT l.grad FROM Lokacija l", String.class);
        listaMjesta = query.getResultList();
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