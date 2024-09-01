package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DatumController {

    @FXML
    private TextField odDatumTextField;

    @FXML
    private TextField doDatumTextField;


    @FXML
    public void initialize() {
    }

    @FXML
    private PrimaryController primarycontroller;

    public void setPrimaryController(PrimaryController primarycontroller) {
        this.primarycontroller = primarycontroller;
    }

    @FXML
    private void potvrdiDatum(ActionEvent event) {

        // Dohvatamo unesene cijene
        String pocetniDatum = odDatumTextField.getText();
        String krajnjiDatum = doDatumTextField.getText();

        primarycontroller.updateDate(pocetniDatum, krajnjiDatum);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
