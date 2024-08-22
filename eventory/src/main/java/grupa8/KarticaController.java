

package grupa8;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class KarticaController {
    @FXML 
    private ImageView slikaDogadjaja;
    @FXML
    private Label nazivDogadjaja, datumDogadjaja;
    private Dogadjaj dogadjaj;
   private PrimaryController primaryController;
    public void setPrimaryController(PrimaryController x){
        this.primaryController = x;
    } 
    public void setDogadjaj(Dogadjaj x){
        nazivDogadjaja.setText(x.nazivDogadjaja);
        datumDogadjaja.setText(x.datumDogadjaja.toString());
        Image slika = new Image(getClass().getResourceAsStream(x.urlDogadjaja));
        slikaDogadjaja.setImage(slika);
        this.dogadjaj = x;
    }
    public void eventCardClicked(MouseEvent e){
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dogadjaj.fxml"));
            Parent prijavaRoot = loader.load();
            DogadjajController controller = loader.getController();
            controller.setDogadjaj(this.dogadjaj);
            controller.setKarticaController(this);
            Stage stage = new Stage();
            stage.setTitle("Dogadjaj");
            stage.setScene(new Scene(prijavaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow()));
            stage.showAndWait();

        } catch (IOException em) {
            em.printStackTrace();
        }
    }
    @FXML
    public void initialize(){}
}
