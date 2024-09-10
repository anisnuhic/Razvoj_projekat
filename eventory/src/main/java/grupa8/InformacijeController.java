package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class InformacijeController {
    @FXML
    private TextField naziv, datum, vrijeme, opis, uslov;
    @FXML
    private Label warning;
    @FXML
    private Spinner<Integer> karte;
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
        // Postavi Spinner da ide od 0, maksimalno 1000 (npr.), inkrement za 1
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1);
        karte.setValueFactory(valueFactory);
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
                Label nazivSektora = new Label(
                        "Sektor: " + sektor.getNazivSektora() + "   Kapacitet: " + sektor.getKapacitet());
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
    String imageUrl;
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
                System.out.println(imageUrl);
                // Provjeri da li direktorijum postoji, ako ne, kreiraj ga
                if (!Files.exists(parentDir)) {
                    Files.createDirectories(parentDir); // Kreiraj sve potrebne direktorijume
                }
                // Provjeri da li fajl već postoji
                if (!Files.exists(assetsPath)) {
                    // Kopiraj sliku u assets folder
                    Files.copy(selectedFile.toPath(), assetsPath, StandardCopyOption.REPLACE_EXISTING);
                }
                imageUrl = "assets/slikeDogadjaja/" + selectedFile.getName();

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
        // Prikupimo podatke sa forme
        String nazivDogadjaja = naziv.getText();
        String datumDogadjaja = datum.getText();
        String vrijemeDogadjaja = vrijeme.getText();
        String opisDogadjaja = opis.getText();
        String uslovOtkazivanja = uslov.getText();

        // Prikupljanje informacija iz ComboBox-ova
        String vrstaDogadjaja = vrsta.getValue();
        String podvrstaDogadjaja = podvrsta.getValue();
        String grad = gradComboBox.getValue();
        String lokacijaNaziv = lokacijaComboBox.getValue();

        // Prikupljanje informacija iz Spinner-a (broj karata)
        int maxKarti = karte.getValue();

        // Provjera da li su datum i vrijeme u pravilnom formatu
        try {
            String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";
            // Kompajliraj pattern
            Pattern dateRegex = Pattern.compile(datePattern);
            // Matchiraj datum sa patternom
            Matcher dateMatcher = dateRegex.matcher(datumDogadjaja);
        
            if (!dateMatcher.matches()) {
                warning.setText("Nevalidan format");
                warning.setStyle("-fx-text-fill: red;");
                return;
            }

            // Provjera da li je današnji datum veći od unesenog datuma
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate enteredDate = LocalDate.parse(datumDogadjaja, dateFormatter);
                LocalDate today = LocalDate.now();
                if (today.isAfter(enteredDate)) {
                    warning.setText("Nevalidan datum");
                    warning.setStyle("-fx-text-fill: red;");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Date parsing error: " + e.getMessage());
            }
        
            String timePattern = "^\\d{2}:\\d{2}$";
            Pattern timeRegex = Pattern.compile(timePattern);
            Matcher timeMatcher = timeRegex.matcher(vrijemeDogadjaja);
        
            if (!timeMatcher.matches()) {
                warning.setText("Nevalidan format");
                warning.setStyle("-fx-text-fill: red;");
                return;
            }

            // Provjera da li su sati i minute validni
            String[] timeParts = vrijemeDogadjaja.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            if (hour > 23) {
                warning.setText("Nevalidno vrijeme");
                warning.setStyle("-fx-text-fill: red;");
                return;
            }

            if (minute > 59) {
                warning.setText("Nevalidno vrijeme");
                warning.setStyle("-fx-text-fill: red;");
                return;
            }
        
        } catch (PatternSyntaxException e) {
            System.out.println("Error in the regex pattern: " + e.getMessage());
        }

        warning.setText("");
        
        // Konvertovanje datuma i vremena u LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime datumVrijeme = LocalDateTime.parse(datumDogadjaja + " " + vrijemeDogadjaja, formatter);
        LocalDate datumVrijemeKarta = LocalDate.parse(datumDogadjaja, formatter2);
        // Pronalazak lokacije u bazi podataka na osnovu naziva
        TypedQuery<Lokacija> lokacijaQuery = em.createQuery("SELECT l FROM Lokacija l WHERE l.naziv = :naziv",
                Lokacija.class);
        lokacijaQuery.setParameter("naziv", lokacijaNaziv);
        Lokacija lokacija = lokacijaQuery.getSingleResult();

        // Prikupljanje sektora za odabranu lokaciju
        TypedQuery<Sektor> sektorQuery = em.createQuery("SELECT s FROM Sektor s WHERE s.lokacija.naziv = :lokacija",
                Sektor.class);
        sektorQuery.setParameter("lokacija", lokacijaNaziv);
        List<Sektor> sektori = sektorQuery.getResultList();

        // Unos cijena za sektore sa forme
        Map<Sektor, BigDecimal> sektorCijene = new HashMap<>();
        for (int i = 0; i < vbox.getChildren().size(); i += 2) {
            Label sektorLabel = (Label) vbox.getChildren().get(i);
            TextField cijenaField = (TextField) vbox.getChildren().get(i + 1);

            String sektorNaziv = sektorLabel.getText().split(": ")[1].split("   ")[0]; // Izdvajanje naziva sektora iz
                                                                                       // labela
            BigDecimal cijena = new BigDecimal(cijenaField.getText()); // Cijena iz unosa

            // Provjera da li je cijena negativna
            try {
                if (cijena.compareTo(BigDecimal.ZERO) < 0) {
                    warning.setText("Nevalidna cijena");
                    warning.setStyle("-fx-text-fill: red;");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Price entry error:" + e.getMessage());
            }

            // Pronađi odgovarajući sektor na osnovu naziva
            Sektor sektor = sektori.stream().filter(s -> s.getNazivSektora().equals(sektorNaziv)).findFirst()
                    .orElse(null);
            if (sektor != null) {
                sektorCijene.put(sektor, cijena); // Poveži sektor sa unesenom cijenom
            }
        }

        // Kreiraj novi događaj
        Dogadjaj noviDogadjaj = new Dogadjaj();
        noviDogadjaj.setNaziv(nazivDogadjaja);
        noviDogadjaj.setOpis(opisDogadjaja);
        noviDogadjaj.setLokacija(lokacija);
        noviDogadjaj.setDatumVrijeme(datumVrijeme);
        noviDogadjaj.setVrstaDogadjaja(vrstaDogadjaja);
        noviDogadjaj.setPodvrstaDogadjaja(podvrstaDogadjaja);
        noviDogadjaj.setOdobreno(false); // Pretpostavka da događaj mora biti odobren
        noviDogadjaj.setSlikaUrl(imageUrl);
        String imeKorisnika = primaryController.getImeKorisnika();
        TypedQuery<Integer> korisnikIdQuery = em
                .createQuery("SELECT k.korisnikId FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme", Integer.class);
        korisnikIdQuery.setParameter("korisnickoIme", imeKorisnika);
        Integer korisnikId = korisnikIdQuery.getSingleResult();

        TypedQuery<Organizator> organizatorQuery = em
                .createQuery("SELECT o FROM Organizator o WHERE o.organizatorId = :korisnikId", Organizator.class);
        organizatorQuery.setParameter("korisnikId", korisnikId);
        Organizator organizator = organizatorQuery.getSingleResult();

        noviDogadjaj.setOrganizator(organizator);
        // Sačuvaj događaj u bazi podataka
        em.getTransaction().begin();
        em.persist(noviDogadjaj);

        // Kreiraj karte za svaki sektor
        for (Map.Entry<Sektor, BigDecimal> entry : sektorCijene.entrySet()) {
            Sektor sektor = entry.getKey();
            BigDecimal cijena = entry.getValue();

            Karta karta = new Karta();
            karta.setDogadjaj(noviDogadjaj);
            karta.setSektor(sektor);
            karta.setCijena(cijena);
            karta.setDatumPocetkaProdaje(LocalDate.now()); // Postavi trenutni datum kao početak prodaje
            karta.setDatumZavrsetkaProdaje(datumVrijemeKarta.minusDays(1)); // Završi prodaju dan prije događaja
            karta.setMaksimalanBrojKartiPoKorisniku(maxKarti);
            karta.setUslovOtkazivanja(uslovOtkazivanja);

            // Sačuvaj kartu u bazi podataka
            em.persist(karta);
        }

        em.getTransaction().commit();
        primaryController.addDogadjajListToResetka(primaryController.getInitDogadjajList());
        System.out.println("Događaj uspješno organizovan i karte kreirane.");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
