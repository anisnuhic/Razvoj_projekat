package grupa8;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class InformacijeController {

    @FXML
    private ComboBox<String> vrsta;

    @FXML
    private ComboBox<String> podvrsta;

    @FXML
    private PrimaryController primaryController;

    @FXML
    private ImageView imageView;

    @FXML
    private Button dodajSlikuButton;

    private final Map<String, String[]> podvrstaOptions = new HashMap<>();

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    public void initialize() {
        // Definiraj opcije za podvrstu na osnovu vrste
        podvrstaOptions.put("sport", new String[]{"utakmica", "meč"});
        podvrstaOptions.put("muzika", new String[]{"festival", "koncert"});
        podvrstaOptions.put("kultura", new String[]{"predstava", "izložba"});
        podvrstaOptions.put("ostalo", new String[]{});

        vrsta.getItems().addAll(podvrstaOptions.keySet());
        vrsta.setOnAction(event -> updatePodvrstaOptions());
    }

    private void updatePodvrstaOptions() {
        String selectedVrsta = vrsta.getValue();
        if (selectedVrsta != null && podvrstaOptions.containsKey(selectedVrsta)) {
            podvrsta.getItems().clear();
            podvrsta.getItems().addAll(podvrstaOptions.get(selectedVrsta));
            podvrsta.setValue(null); // Resetuj odabranu podvrstu
        }
    }

    @FXML
    private void dodajSliku() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Izaberi sliku");

        // Filtriraj fajlove po tipovima slika
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Otvori explorer prozor za biranje fajla
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                // Definiraj putanju do assets foldera
                Path assetsPath = Paths.get("src/main/resources/grupa8/assets/" + selectedFile.getName());
                // Provjeri da li fajl već postoji
                if (!Files.exists(assetsPath)) {
                    // Kopiraj sliku u assets folder
                    Files.copy(selectedFile.toPath(), assetsPath, StandardCopyOption.REPLACE_EXISTING);
                }

                // Ako je fajl izabran, postavi sliku u ImageView
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
