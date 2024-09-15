package grupa8;
import jakarta.persistence.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.util.regex.*;

import java.util.Date;

public class RegistracijaController {
    @FXML
    private PrimaryController primaryController;
    @FXML
    private TextField imeField;

    @FXML
    private TextField prezimeField;

    @FXML
    private TextField korisnickoImeField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField lozinkaField;

    @FXML
    private PasswordField potvrdaLozinkeField;

    @FXML
    private RadioButton korisnikRadioButton;

    @FXML
    private RadioButton organizatorRadioButton;

    @FXML
    private Label warning, label1, label2, label3, label4;

    @FXML
    private TextField nazivOrg;

    @FXML
    private TextField kontaktOrg;

    @FXML
    private TextField telefonOrg;

    @FXML
    private TextField adresaOrg;

    private ToggleGroup roleToggleGroup;

    private EntityManagerFactory emf;

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    @FXML
    public void initialize() {
        // Kreirajte ToggleGroup
        roleToggleGroup = new ToggleGroup();

        korisnikRadioButton.setToggleGroup(roleToggleGroup);
        organizatorRadioButton.setToggleGroup(roleToggleGroup);

        roleToggleGroup.selectToggle(korisnikRadioButton);
        roleToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
        if(korisnikRadioButton.isSelected()) {
            label1.setVisible(false);
            label2.setVisible(false);
            label3.setVisible(false);
            label4.setVisible(false);
            nazivOrg.setVisible(false);
            kontaktOrg.setVisible(false);
            telefonOrg.setVisible(false);
            adresaOrg.setVisible(false);
        }
        else{
            label1.setVisible(true);
            label2.setVisible(true);
            label3.setVisible(true);
            label4.setVisible(true);
            nazivOrg.setVisible(true);
            kontaktOrg.setVisible(true);
            telefonOrg.setVisible(true);
            adresaOrg.setVisible(true);
        }
    });
    }

    // public void RegistracijaController(){
    // emf = Persistence.createEntityManagerFactory("eventoryPU");
    // }

    @FXML
    private void handleRegistracija(ActionEvent event) {
        emf = Persistence.createEntityManagerFactory("eventoryPU");
        EntityManager em = emf.createEntityManager();
        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        String korisnickoIme = korisnickoImeField.getText();
        String email = emailField.getText();
        String lozinka = lozinkaField.getText();
        String potvrdaLozinke = potvrdaLozinkeField.getText();

        try {
            String namePattern = "^[\\p{L}]+([ '-][\\p{L}]+)*([\\s][\\p{L}]+([ '-][\\p{L}]+)*)*$";
            // Kompajliraj pattern
            Pattern pattern = Pattern.compile(namePattern);
            Matcher matcher1 = pattern.matcher(ime);
            Matcher matcher2 = pattern.matcher(prezime);

            // Provjera da li ime i prezime odgovaraju patternu
            if (!matcher1.matches()) {
                warning.setText("Nevalidno ime");
                warning.setStyle("-fx-text-fill: red;");
                return;
            } 
            if (!matcher2.matches()) {
                warning.setText("Nevalidno prezime");
                warning.setStyle("-fx-text-fill: red;");
                return;
            }
        } catch (PatternSyntaxException e) {
            System.out.println("Error in the regex pattern: " + e.getMessage());
        }

        // if (!lozinka.equals(potvrdaLozinke)) {
        //     warning.setText("Lozinke nisu iste");
        //     return;
        // }

        // Provjera da li korisničko ime već postoji
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(k) FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme", Long.class);
            query.setParameter("korisnickoIme", korisnickoIme);
            Long count = query.getSingleResult();

            if (count > 0) {
                warning.setText("Korisničko ime već postoji");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed to check username existence");
            e.printStackTrace();
            return;
        }

        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(k) FROM Korisnik k WHERE k.email = :email", Long.class);
            query.setParameter("email", email);
            Long count = query.getSingleResult();

            if (count > 0) {
                warning.setText("E-mail već u upotrebi");
                warning.setStyle("-fx-text-fill: red;");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed to check email existence");
            e.printStackTrace();
            return;
        }

        // Provjera da li je email u ispravnom formatu
        try {
            String emailPattern = "^[\\p{L}0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            // Kompajliraj pattern
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(email);

            // Provjeri da li mail odgovara patternu
            if (!matcher.matches()) {
                warning.setText("Nevalidan e-mail");
                warning.setStyle("-fx-text-fill: red;");
                return;
            } 
        } catch (PatternSyntaxException e) {
            System.out.println("Error in the regex pattern: " + e.getMessage());
        }

        // Provjera da li su loznike iste
        if (!lozinka.equals(potvrdaLozinke)) {
            warning.setText("Lozinke nisu iste");
            warning.setStyle("-fx-text-fill: red;");
            return;
        }

        Korisnik.TipKorisnika tipKorisnika;
        String a = "ADMIN";
        if (korisnikRadioButton.isSelected()) {
            tipKorisnika = Korisnik.TipKorisnika.REGULAR;
        
            a = "KORISNIK";
        } else if (organizatorRadioButton.isSelected()) {
            tipKorisnika = Korisnik.TipKorisnika.ORGANIZATOR;
           
            a = "ORGANIZATOR";
        } else {
            // Prikazati grešku korisniku - nije odabran tip korisnika
            return;
        }

           // Provjera da li su ime kontakt osobe i telefonski broj u ispravnom formatu
           if(a == "ORGANIZATOR") {
            try {
                String fullNamePattern = "^[\\p{L}]+([ '-][\\p{L}]+)*([\\s][\\p{L}]+([ '-][\\p{L}]+)*)*$";
                String kontaktOsoba = kontaktOrg.getText();
                // Kompajliraj pattern
                Pattern pattern = Pattern.compile(fullNamePattern);
                Matcher matcher = pattern.matcher(kontaktOsoba);
    
                // Provjera da li ime odgovara patternu
                if (!matcher.matches()) {
                    warning.setText("Nevalidna kontakt osoba");
                    warning.setStyle("-fx-text-fill: red;");
                    return;
                }
            } catch (PatternSyntaxException e) {
                System.out.println("Error in the regex pattern: " + e.getMessage());
            }

            try {
                String phoneNumberPattern = "^\\+?\\d+$";
                String telefonskiBroj = telefonOrg.getText();
                // Kompajliraj pattern
                Pattern pattern = Pattern.compile(phoneNumberPattern);
                Matcher matcher = pattern.matcher(telefonskiBroj);
            
                // Provjeri da li broj odgovara patternu
                if (!matcher.matches()) {
                    warning.setText("Nevalidan telefonski broj"); 
                    warning.setStyle("-fx-text-fill: red;");
                    return;
                }
            } catch (PatternSyntaxException e) {
                System.out.println("Error in the regex pattern: " + e.getMessage());
            }
        }

        // Kreiranje novog korisnika
        Korisnik korisnik = new Korisnik();
        korisnik.setIme(ime);
        korisnik.setPrezime(prezime);
        korisnik.setKorisnickoIme(korisnickoIme);
        korisnik.setEmail(email);
        korisnik.setLozinka(lozinka);
        korisnik.setDatumRegistracije(new Date());
        korisnik.setTipKorisnika(tipKorisnika);

        // Spremanje korisnika u bazu
        try {
            em.getTransaction().begin();
            em.persist(korisnik);
            em.getTransaction().commit();

            if (tipKorisnika == Korisnik.TipKorisnika.ORGANIZATOR) {
                Organizator organizator = new Organizator();
                organizator.setNazivOrganizacije(nazivOrg.getText());
                organizator.setOrganizatorId(korisnik.getKorisnikId());
                organizator.setKontaktOsoba(kontaktOrg.getText());
                organizator.setTelefon(telefonOrg.getText());
                organizator.setAdresa(adresaOrg.getText());
                organizator.setKorisnik(korisnik); // Postavljanje relacije
    
                em.getTransaction().begin();
                em.persist(organizator);
                em.getTransaction().commit();
            }
            if (primaryController != null) {
                primaryController.hideButton();
                primaryController.setKorisnickoIme(korisnickoImeField.getText(), a);
                primaryController.mojiDogadjaji.setVisible(true);
                primaryController.slicica2.setVisible(true);
            }
            if(korisnikRadioButton.isSelected()) 
                primaryController.napraviButton.setVisible(false);
                primaryController.mojiDogadjaji.setVisible(false);
                primaryController.slicica2.setVisible(false);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("Failed to persist");
            // Handle any exceptions that may occur during database interaction
            // For example, log the exception or display an error message to the user
            e.printStackTrace();
        }
    }
}
