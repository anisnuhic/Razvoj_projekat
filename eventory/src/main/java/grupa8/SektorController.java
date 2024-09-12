package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SektorController {

    @FXML
    private VBox sektorContainer;

    private List<SektorData> sektorDataList = new ArrayList<>();

    public void initData(int numberOfSectors) {
        sektorDataList.clear();
        for (int i = 0; i < numberOfSectors; i++) {
            // Create TextFields for sector name and capacity
            TextField nazivSektora = new TextField();
            nazivSektora.setPromptText("Naziv sektora " + (i + 1));
            
            TextField kapacitetSektora = new TextField();
            kapacitetSektora.setPromptText("Kapacitet sektora " + (i + 1));

            // Add TextFields to the VBox
            sektorContainer.getChildren().addAll(nazivSektora, kapacitetSektora);

            // Add sector data to list
            sektorDataList.add(new SektorData(nazivSektora, kapacitetSektora));
        }
    }
    
    @FXML
    private void potvrdi(ActionEvent event) {
        // Process and close the window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    

    public List<SektorData> getSektorDataList() {
        return sektorDataList;
    }

    public static class SektorData {
        private TextField nazivSektora;
        private TextField kapacitetSektora;

        public SektorData(TextField nazivSektora, TextField kapacitetSektora) {
            this.nazivSektora = nazivSektora;
            this.kapacitetSektora = kapacitetSektora;
        }

        public String getNazivSektora() {
            return nazivSektora.getText();
        }

        public Integer getKapacitetSektora() {
            return Integer.parseInt(kapacitetSektora.getText());
        }
    }

}
