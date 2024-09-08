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
        private Label warning;

        private ToggleGroup roleToggleGroup;

        private EntityManagerFactory emf;
        public void setPrimaryController(PrimaryController primaryController) {
            this.primaryController = primaryController;
        }
        @FXML
        public void initialize() {
            // Kreirajte ToggleGroup
            roleToggleGroup = new ToggleGroup();
        
            // Dodajte RadioButton dugmad u grupu
            korisnikRadioButton.setToggleGroup(roleToggleGroup);
            organizatorRadioButton.setToggleGroup(roleToggleGroup);

            roleToggleGroup.selectToggle(korisnikRadioButton);
        }

        // public void RegistracijaController(){
        //     emf = Persistence.createEntityManagerFactory("eventoryPU");
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

            if (!lozinka.equals(potvrdaLozinke)) {
                warning.setText("Lozinke nisu iste");
                return;
            }

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
    }   
    catch (Exception e) {
        System.out.println("Failed to check username existence");
        e.printStackTrace();
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
                if (primaryController != null) {
                    primaryController.hideButton();
                    primaryController.setKorisnickoIme(korisnickoImeField.getText(), a );
                }
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
