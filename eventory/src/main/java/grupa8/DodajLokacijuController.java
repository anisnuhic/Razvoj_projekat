package grupa8;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class DodajLokacijuController {

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private Button urediSektoreButton;
    
    @FXML
    private AdminLokacijaController adminLokacijaController;

    // public void setPrimaryController(AdminLokacijaController adminLokacijaController) {
    //     this.adminLokacijaController = adminLokacijaController;
    // }
    @FXML
    public void initialize() {
        // Postavite spinner s minimalnom vrijednošću 0, maksimalnom 100 i početnom 0
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        spinner.setValueFactory(valueFactory);

        // Sakrij button na početku, jer je početna vrijednost 0
        urediSektoreButton.setVisible(false);

        // Dodajte listener koji prati promjene spinnera
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            // Ako je vrijednost veća od 0, prikaži button, inače sakrij
            if (newValue > 0) {
                urediSektoreButton.setVisible(true);
            } else {
                urediSektoreButton.setVisible(false);
            }
        });
    }
}
