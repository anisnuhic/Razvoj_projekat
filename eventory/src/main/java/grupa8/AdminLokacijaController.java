package grupa8;

import java.io.IOException;
import java.util.List;

import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminLokacijaController {
    @FXML
    private PrimaryController primaryController;
    @FXML
    private AnchorPane currentLocationsPane;
    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

      void displayLocations() {
            EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();       
         try {
            // Load locations from database
            List<Lokacija> lokacije = em.createQuery("SELECT l FROM Lokacija l ORDER BY l.grad ASC", Lokacija.class).getResultList();
            // Create a VBox to hold labels
            VBox vbox = new VBox(5);  // 5 is the spacing between labels
            
            currentLocationsPane.getChildren().clear();

            for (Lokacija lokacija : lokacije) {
                // Create a label for each location
                Label label = new Label(lokacija.getGrad()  + ", " + lokacija.getNaziv());
                label.setStyle("-fx-text-fill: black; -fx-font-size: 14px; -fx-padding-left: 10px; -fx-float:center; -fx-text-weight: bold;");
                vbox.getChildren().add(label);
            }

            // Add VBox to the AnchorPane
            currentLocationsPane.getChildren().add(vbox);

        } finally {
            em.close();
        }
    }
  
    @FXML 
    private void dodajLokaciju(ActionEvent event){
         try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dodajLokaciju.fxml"));
            Parent registracijaRoot = fxmlLoader.load();
             DodajLokacijuController dodajController = fxmlLoader.getController();
            dodajController.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Napravi lokaciju");
            stage.setScene(new Scene(registracijaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize(){
        displayLocations();

    }
    
}
