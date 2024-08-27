package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class CijenaController {

    @FXML
    private TextField odCijenaTextField;

    @FXML
    private TextField doCijenaTextField;

   

    @FXML
    public void initialize() {
    }

    @FXML
    private PrimaryController primarycontroller;

    public void setPrimaryController(PrimaryController primarycontroller) {
        this.primarycontroller = primarycontroller;
    }
    @FXML
    private void  potvrdiCijene(ActionEvent event) {
        
        // Dohvatamo unesene cijene
        String pocetnaCijena = odCijenaTextField.getText();
        String krajnjaCijena = doCijenaTextField.getText();

        primarycontroller.updatePrice(pocetnaCijena, krajnjaCijena);

    }
}
