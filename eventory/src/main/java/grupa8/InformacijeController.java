package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class InformacijeController {
    @FXML
    private TextField naziv, datum, vrijeme, opis, uslov, karte;
    @FXML
    private ComboBox<String> vrsta;

    @FXML
    private ComboBox<String> podvrsta;

    @FXML
    private ComboBox<String> lokacijaComboBox, gradComboBox;

    @FXML
    private PrimaryController primaryController;

    @FXML
    private VBox vbox;
    @FXML
    private ImageView imageView;

    @FXML
    private Button dodajSlikuButton;

    private final Map<String, String[]> podvrstaOptions = new HashMap<>();

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

    public void initialize() {
        // Definiraj opcije za podvrstu na osnovu vrste
        podvrstaOptions.put("sport", new String[] { "utakmica", "meč" });
        podvrstaOptions.put("muzika", new String[] { "festival", "koncert" });
        podvrstaOptions.put("kultura", new String[] { "predstava", "izložba" });
        podvrstaOptions.put("ostalo", new String[] {});

        vrsta.getItems().addAll(podvrstaOptions.keySet());
        vrsta.setOnAction(event -> updatePodvrstaOptions());

        populateGradovi();

        gradComboBox.setOnAction(event -> updateLokacije());
        lokacijaComboBox.setOnAction(event -> prikaziSektore());

    }

    private void populateGradovi() {
        TypedQuery<String> query = em.createQuery("SELECT DISTINCT l.grad FROM Lokacija l", String.class);
        List<String> gradovi = query.getResultList();
        gradComboBox.getItems().addAll(gradovi);
    }

    private void updateLokacije() {
        String selectedGrad = gradComboBox.getValue();
        if (selectedGrad != null) {
            TypedQuery<String> query = em.createQuery("SELECT l.naziv FROM Lokacija l WHERE l.grad = :grad",
                    String.class);
            query.setParameter("grad", selectedGrad);
            List<String> lokacije = query.getResultList();
            lokacijaComboBox.getItems().clear();
            lokacijaComboBox.getItems().addAll(lokacije);
        }
    }

    private void prikaziSektore() {
        String selectedLokacija = lokacijaComboBox.getValue();
        if (selectedLokacija != null) {
            TypedQuery<Sektor> query = em.createQuery("SELECT s FROM Sektor s WHERE s.lokacija.naziv = :lokacija",
                    Sektor.class);
            query.setParameter("lokacija", selectedLokacija);
            List<Sektor> sektori = query.getResultList();

            vbox.getChildren().clear(); // Očisti VBox prije dodavanja novih sektora

            // Dinamički dodaj sektore u VBox
            for (Sektor sektor : sektori) {
                Label nazivSektora = new Label("Sektor: " + sektor.getNazivSektora() + "   Kapacitet: " + sektor.getKapacitet());
                nazivSektora.setStyle("-fx-text-weight: bold;");
                TextField textField = new TextField();
                textField.setPromptText("unesi cijenu");
                vbox.getChildren().addAll(nazivSektora, textField);
            }
        }
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
                Path assetsPath = Paths.get("Razvoj_projekat/eventory/src/main/resources/grupa8/assets/slikeDogadjaja/"
                        + selectedFile.getName());
                Path parentDir = assetsPath.getParent();

                // Provjeri da li direktorijum postoji, ako ne, kreiraj ga
                if (!Files.exists(parentDir)) {
                    Files.createDirectories(parentDir); // Kreiraj sve potrebne direktorijume
                }
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
    @FXML
    private void organizuj(ActionEvent event) {
                EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
                String nazivDogadjaja = naziv.getText();
                String datumDogadjaja = datum.getText();
                String vrijemeDogadjaja = vrijeme.getText();
                String opisDogadjaja = opis.getText();
                String mjestoDogadajaja = gradComboBox.getPromptText();
                String lokacijaDogadjaja = lokacijaComboBox.getPromptText();
                String brojKarti = karte.getText();
                String urlSlike = imageView.getImage().getUrl();

                Dogadjaj dogadjaj = new Dogadjaj();
                dogadjaj.setNaziv(nazivDogadjaja);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(datumDogadjaja + " " +  vrijemeDogadjaja, formatter);
                dogadjaj.setDatumVrijeme(dateTime);
                dogadjaj.setOpis(opisDogadjaja);
                dogadjaj.setVrstaDogadjaja(vrsta.getPromptText());
                dogadjaj.setPodvrstaDogadjaja(podvrsta.getPromptText());
                dogadjaj.setSlikaUrl(urlSlike);
                //... nastaviti 

    }
}
