package grupa8;

import javafx.application.Platform;

// import com.itextpdf.layout.element.Image;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MojeKarteController {

    private PrimaryController primaryController;

    @FXML
    private ImageView slikaDogadjaja;
    @FXML
    private Label cijenaDogadjaja, nazivDogadjaja, sektorDogadjaja, slovoDogadjaja, stanje;
    @FXML
    private Button kupi;

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    public void postaviIzgledKarte(String naziv, String cijena, String slikaUrl, String sektor, String status, Integer broj) {
        cijenaDogadjaja.setText("Cijena: " + cijena);
        nazivDogadjaja.setText(naziv);
        sektorDogadjaja.setText(sektor.toUpperCase());
        slovoDogadjaja.setText(String.valueOf(sektor.charAt(0)).toUpperCase());
        slikaDogadjaja.setImage(new Image(getClass().getResourceAsStream(slikaUrl)));
        if(status.equals("REZERVISANO"))kupi.setVisible(true);
        else kupi.setVisible(false);
        stanje.setText(status + ": " + broj);

    }
}
