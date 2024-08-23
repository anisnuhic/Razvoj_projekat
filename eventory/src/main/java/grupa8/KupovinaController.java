package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class KupovinaController {

    @FXML
    StackPane stek;

    @FXML
    private void vratiSeNazad(ActionEvent event) {
        try {
            stek.getChildren().remove(stek.getChildren().size()-1);  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
