package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrimaryController {

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

      public static void main(String[] args) {
        // Kreiraj listu za Dogadjaj objekte
        List<Dogadjaj> listaDogadjaja = new ArrayList<>();
        
        // Kreiraj 5 objekata Dogadjaj sa hardkodiranim vrijednostima
        listaDogadjaja.add(new Dogadjaj("Koncert u parku", "http://example.com/koncert", new Date())); // 15. avgust 2024
        listaDogadjaja.add(new Dogadjaj("Izložba umetnosti", "http://example.com/izlozba", new Date())); // 20. septembar 2024
        listaDogadjaja.add(new Dogadjaj("Sportsko takmičenje", "http://example.com/sport", new Date())); // 10. oktobar 2024
        listaDogadjaja.add(new Dogadjaj("Pozorišna predstava", "http://example.com/pozoriste", new Date())); // 5. novembar 2024
        listaDogadjaja.add(new Dogadjaj("Tehnička konferencija", "http://example.com/konferencija", new Date())); // 25. decembar 2024

        // Ispis liste Dogadjaj objekata
        for (Dogadjaj d : listaDogadjaja) {
            System.out.println("Naziv: " + d.nazivDogadjaja + ", URL: " + d.urlDogadjaja + ", Datum: " + d.datumDogadjaja);
        }
    }
}
