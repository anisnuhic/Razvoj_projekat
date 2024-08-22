package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrimaryController {
    @FXML
    private GridPane resetka;
    @FXML
    private void handlePrijavaButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("prijava.fxml"));
            Parent prijavaRoot = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Prijava");
            stage.setScene(new Scene(prijavaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistracijaButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registracija.fxml"));
            Parent registracijaRoot = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Registracija");
            stage.setScene(new Scene(registracijaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        @FXML
        public void initialize() {
        // Kreiraj listu za Dogadjaj objekte
        List<Dogadjaj> listaDogadjaja = new ArrayList<>();
        
        // Kreiraj 5 objekata Dogadjaj sa hardkodiranim vrijednostima
        listaDogadjaja.add(new Dogadjaj("Aleksandra Prijovic", "/grupa8/assets/slikeDogadjaja/aleksandra.png", new Date())); // 15. avgust 2024
        listaDogadjaja.add(new Dogadjaj("Izložba umetnosti", "/grupa8/assets/slikeDogadjaja/boks_mec.png", new Date())); // 20. septembar 2024
        listaDogadjaja.add(new Dogadjaj("Sportsko takmičenje", "/grupa8/assets/slikeDogadjaja/brena.png", new Date())); // 10. oktobar 2024
        listaDogadjaja.add(new Dogadjaj("Pozorišna predstava", "/grupa8/assets/slikeDogadjaja/heni.png", new Date())); // 5. novembar 2024
        listaDogadjaja.add(new Dogadjaj("Tehnička konferencija", "/grupa8/assets/slikeDogadjaja/folk_fest.png", new Date())); // 25. decembar 2024
        resetka.getChildren().clear();
        int row = 0;
        int col = 0;
        // Ispis liste Dogadjaj objekata
        for (Dogadjaj d : listaDogadjaja) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("kartica.fxml"));
                AnchorPane eventCard = loader.load();
                KarticaController controller = loader.getController();
                controller.setDogadjaj(d);
                controller.setPrimaryController(this);
    
                resetka.add(eventCard, col, row);
                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
