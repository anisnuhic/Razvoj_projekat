package grupa8;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CijenaController {

    @FXML
    private TextField odCijenaTextField;

    @FXML
    private TextField doCijenaTextField;

    @FXML
    private Button potvrdiButton;

    @FXML
    public void initialize() {
        odCijenaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                odCijenaTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        doCijenaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                odCijenaTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        potvrdiButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> !validateInputFields(),
                odCijenaTextField.textProperty(), doCijenaTextField.textProperty()));
    }

    @FXML
    private PrimaryController primarycontroller;

    public void setPrimaryController(PrimaryController primarycontroller) {
        this.primarycontroller = primarycontroller;
    }

    @FXML
    private void potvrdiCijene(ActionEvent event) {
        // Dohvatamo unesene cijene
        String pocetnaCijena = odCijenaTextField.getText();
        String krajnjaCijena = doCijenaTextField.getText();

        // Ažuriramo cijene u primary controlleru
        primarycontroller.updatePrice(pocetnaCijena, krajnjaCijena);

        // Zatvaramo trenutni prozor
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private Boolean validateInputFields() {
        String odCijenaTextFieldText = odCijenaTextField.getText();
        String doCijenaTextFieldText = doCijenaTextField.getText();

        if (!doCijenaTextFieldText.isEmpty() && !odCijenaTextFieldText.isEmpty()) {
            int doCijenaValue = Integer.parseInt(doCijenaTextField.getText());
            int odCijenaValue = Integer.parseInt(odCijenaTextField.getText());
            return odCijenaValue <= doCijenaValue;
        }
        return true;
    }
}
