package grupa8;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import java.util.Date;

public class RegistracijaController {
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

    private ToggleGroup roleToggleGroup;

    private EntityManagerFactory emf;
    @FXML
    public void initialize() {
        // Kreirajte ToggleGroup
        roleToggleGroup = new ToggleGroup();

        // Dodajte RadioButton dugmad u grupu
        korisnikRadioButton.setToggleGroup(roleToggleGroup);
        organizatorRadioButton.setToggleGroup(roleToggleGroup);
    }



    public RegistracijaController() {
        emf = Persistence.createEntityManagerFactory("eventoryPU");
    }



     @FXML
    private void handleRegistracija() {
        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        String korisnickoIme = korisnickoImeField.getText();
        String email = emailField.getText();
        String lozinka = lozinkaField.getText();
        String potvrdaLozinke = potvrdaLozinkeField.getText();

        if (!lozinka.equals(potvrdaLozinke)) {
            // Prikazati grešku korisniku - lozinke se ne podudaraju
            return;
        }

        Korisnik.TipKorisnika tipKorisnika;
        if (korisnikRadioButton.isSelected()) {
            tipKorisnika = Korisnik.TipKorisnika.REGULAR;
        } else if (organizatorRadioButton.isSelected()) {
            tipKorisnika = Korisnik.TipKorisnika.ORGANIZATOR;
        } else {
            // Prikazati grešku korisniku - nije odabran tip korisnika
            return;
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
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(korisnik);
            em.getTransaction().commit();
        } catch (Exception e) {
            // Handle any exceptions that may occur during database interaction
            // For example, log the exception or display an error message to the user
            e.printStackTrace();
        }
    }
}
