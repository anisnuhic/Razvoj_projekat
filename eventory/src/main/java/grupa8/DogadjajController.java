package grupa8;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DogadjajController {
    @FXML
    private ImageView slikaDogadjaja;
    @FXML
    private Label nazivDogadjaja, datumDogadjaja;

    private KarticaController karticaController;
    public void setKarticaController(KarticaController x){
        this.karticaController = x;
    }
    public void setDogadjaj(Dogadjaj x){
        nazivDogadjaja.setText(x.nazivDogadjaja);
        datumDogadjaja.setText(x.datumDogadjaja.toString());
        Image slika = new Image(getClass().getResourceAsStream(x.urlDogadjaja));
        slikaDogadjaja.setImage(slika);
        Rectangle2D viewport = new Rectangle2D(0, 0, 500, 300);
        slikaDogadjaja.setViewport(viewport);
    }
}
