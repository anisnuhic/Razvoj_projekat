package grupa8;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminLokacijaController {
    @FXML
    private PrimaryController primaryController;
    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }
    @FXML 
    private void dodajLokaciju(ActionEvent event){
         try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dodajLokaciju.fxml"));
            Parent registracijaRoot = fxmlLoader.load();
            // DodajLokacijuController adminController = fxmlLoader.getController();
            // adminController.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Napravi lokaciju");
            stage.setScene(new Scene(registracijaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize(){

    }
    
}
