package grupa8;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.function.UnaryOperator;

public class DatumController {

    @FXML
    private TextField odDatumTextField;

    @FXML
    private TextField doDatumTextField;

    @FXML
    private Button potvrdiDatumButton;


    @FXML
    public void initialize() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        TextFormatter<String> textFormatterDoDatum = new TextFormatter<>(getDateFilter(dateFormatter));
        TextFormatter<String> textFormatterOdDatum = new TextFormatter<>(getDateFilter(dateFormatter));

        doDatumTextField.setTextFormatter(textFormatterDoDatum);
        odDatumTextField.setTextFormatter(textFormatterOdDatum);

        potvrdiDatumButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> !validateInputFields(),
                odDatumTextField.textProperty(), doDatumTextField.textProperty()));
    }

    @FXML
    private PrimaryController primarycontroller;

    public void setPrimaryController(PrimaryController primarycontroller) {
        this.primarycontroller = primarycontroller;
    }

    @FXML
    private void potvrdiDatum(ActionEvent event) {
        String pocetniDatum = odDatumTextField.getText();
        String krajnjiDatum = doDatumTextField.getText();

        primarycontroller.updateDate(pocetniDatum, krajnjiDatum);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private UnaryOperator<TextFormatter.Change> getDateFilter(DateTimeFormatter dateFormatter) {
        return change -> {
            String newText = change.getControlNewText();

            if (!newText.matches("(" +
                    "((0|0[1-9]|[1-2]|[1-2][0-9]|3|3[0-1])" +
                    "|(((0[1-9]|[1-2][0-9]|3[0-1])-)|((0[1-9]|[1-2][0-9]|3[0-1])-[0-1])|((0[1-9]|[1-2][0-9]|3[0-1])-0[1-9])|((0[1-9]|[1-2][0-9]|3[0-1])-1[0-2])))" +
                    "|(((0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-)|((0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-2)|((0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-20)|((0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-20[0-9])|((0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-20[0-9][0-9]))" +
                    ")?")) {
                return null;
            }
            if (newText.length() == 10) {
                try {
                    LocalDate.parse(newText, dateFormatter);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
            return change;
        };
    }

    private Boolean validateInputFields() {
        String odDatumTextFieldText = odDatumTextField.getText();
        String doDatumTextFieldText = doDatumTextField.getText();

        if (!odDatumTextFieldText.isEmpty() && odDatumTextFieldText.length() != 10) {
            return false;
        }
        if (!doDatumTextFieldText.isEmpty() && doDatumTextFieldText.length() != 10) {
            return false;
        }
        if(odDatumTextFieldText.length() == 10 && doDatumTextFieldText.length() == 10){
            SimpleDateFormat dtobj = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date odDatum = dtobj.parse(odDatumTextFieldText);
                Date doDatum = dtobj.parse(doDatumTextFieldText);
                if (odDatum.after(doDatum)) {
                    return false;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
