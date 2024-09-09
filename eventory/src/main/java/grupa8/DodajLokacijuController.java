package grupa8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DodajLokacijuController {

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private Button urediSektoreButton;
    @FXML
    private AdminLokacijaController primaryController;
    @FXML
    private ImageView imageView;

    @FXML
    private TextField nazivGrada, nazivLokacije, nazivAdrese;

    private String imageUrl;

    private List<SektorController.SektorData> sektorDataList;

    public void setPrimaryController(AdminLokacijaController primaryController) {
        this.primaryController = primaryController;
    }
    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        spinner.setValueFactory(valueFactory);

        urediSektoreButton.setVisible(false);

        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            urediSektoreButton.setVisible(newValue > 0);
        });
    }

    @FXML
    private void urediSektore() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sektor.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the number of sectors
            SektorController sektorController = loader.getController();
            sektorController.initData(spinner.getValue());

            // Open the new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Uredi sektore");
                        stage.showAndWait();

            sektorDataList = sektorController.getSektorDataList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void odaberiSliku() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        
        // Otvaranje file explorera za odabir slike
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        System.out.println("Izabran file" + selectedFile);
        if (selectedFile != null) {
            try {
                 // Definiraj putanju do assets foldera
                 Path assetsPath = Paths.get("Razvoj_projekat/eventory/src/main/resources/grupa8/assets/slikeLokacija/" + selectedFile.getName());
                 Path parentDir = assetsPath.getParent();

                 System.out.println("Putanja:" +assetsPath);
                 System.out.println("Parent dir:" + parentDir);
                imageUrl = assetsPath.toString();
                 // Provjeri da li direktorijum postoji, ako ne, kreiraj ga
                 if (!Files.exists(parentDir)) {
                    System.out.println("Ne postoji direktorij");
                     Files.createDirectories(parentDir);  // Kreiraj sve potrebne direktorijume
                 }
                 // Provjeri da li fajl već postoji
                 if (!Files.exists(assetsPath)) {
                    System.out.println("Fajl nije postojao");
                    System.out.println("Uzimam ga sa ove putanje: " + selectedFile.toPath());
                    System.out.println("Kopiram ga u: " + assetsPath);
                     // Kopiraj sliku u assets folder
                     Files.copy(selectedFile.toPath(), assetsPath, StandardCopyOption.REPLACE_EXISTING);
                 }
                 System.out.println("Uzimam ga sa ove putanje: " + selectedFile.toPath());
                 System.out.println("Kopiram ga u: " + assetsPath);
                 Files.copy(selectedFile.toPath(), assetsPath, StandardCopyOption.REPLACE_EXISTING);
                 System.out.println("kopiran");
                 // Ako je fajl izabran, postavi sliku u ImageView
                 Image image = new Image(selectedFile.toURI().toString());
                 imageView.setImage(image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void dodajLokaciju(ActionEvent event) {
        // Create an EntityManager to handle database operations
        EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            // Create new Lokacija object and set its attributes
            Lokacija lokacija = new Lokacija();
            lokacija.setNaziv(nazivLokacije.getText());  // Set this dynamically
            lokacija.setAdresa(nazivAdrese.getText());         // Set this dynamically
            lokacija.setGrad(nazivGrada.getText());             // Set this dynamically
            lokacija.setKapacitet(calculateTotalCapacity());

            if (imageView.getImage() != null) {
                lokacija.setSlikaUrl(imageUrl);  // Update with actual file name
            }

            em.persist(lokacija);

            // Insert sectors associated with the location
            for (SektorController.SektorData data : sektorDataList) {
                Sektor sektor = new Sektor();
                sektor.setLokacija(lokacija);
                sektor.setNazivSektora(data.getNazivSektora());
                sektor.setKapacitet(data.getKapacitetSektora());

                em.persist(sektor);
            }

            em.getTransaction().commit();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            if (primaryController != null) {
                primaryController.displayLocations();
            }
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    private int calculateTotalCapacity() {
        return sektorDataList.stream().mapToInt(SektorController.SektorData::getKapacitetSektora).sum();
    }
}
