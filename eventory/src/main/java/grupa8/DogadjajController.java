package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.time.format.DateTimeFormatter;

public class DogadjajController {

    @FXML
    private ImageView slikaDogadjaja;

    @FXML
    private Label nazivDogadjaja, datumDogadjaja;

    @FXML
    private Button openNewViewButton;

    @FXML
    private StackPane stek;

    private KarticaController karticaController;

    public void setKarticaController(KarticaController x) {
        this.karticaController = x;
    }

    public void setDogadjaj(Dogadjaj x) {
        nazivDogadjaja.setText(x.getNaziv());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        datumDogadjaja.setText(x.getDatumVrijeme().format(formatter));
        if (x.getSlikaUrl() != null) {
            Image slika = new Image(getClass().getResourceAsStream(x.getSlikaUrl()));
            slikaDogadjaja.setImage(slika);
        }
        Rectangle2D viewport = new Rectangle2D(0, 0, 480, 330);
        slikaDogadjaja.setViewport(viewport);
    }

    @FXML
    private void rezervacijaClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("kupovina.fxml"));
            Parent parent = fxmlLoader.load();

            KupovinaController kupovinaController = fxmlLoader.getController();
            kupovinaController.stek = this.stek; // Postavi StackPane
            //kupovinaController.setAkcijskiButtonText("Kupi");
            kupovinaController.setAkcijskiButtonText("Rezerviši");
            stek.getChildren().add(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void kupovinaClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("kupovina.fxml"));
            Parent parent = fxmlLoader.load();

            KupovinaController kupovinaController = fxmlLoader.getController();
            kupovinaController.stek = this.stek; // Postavi StackPane
            kupovinaController.setAkcijskiButtonText("Kupi");
            // kupovinaController.setAkcijskiButtonText("Rezerviši");
            stek.getChildren().add(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
