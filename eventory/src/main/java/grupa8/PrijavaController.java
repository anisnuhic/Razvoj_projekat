package grupa8;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PrijavaController {

    @FXML
    private TextField korisnickoImeField;

    @FXML
    private PasswordField lozinkaField;

    @FXML
    private Label warning;

    private EntityManagerFactory emf;
    private PrimaryController primaryController;

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    @FXML
    private void handlePrijava(ActionEvent event) {
        emf = Persistence.createEntityManagerFactory("eventoryPU");
        EntityManager em = emf.createEntityManager();

        String korisnickoIme = korisnickoImeField.getText();
        String lozinka = lozinkaField.getText();

        // Provjera da li korisnik postoji u bazi i da li se lozinka poklapa
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(k) FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme AND k.lozinka = :lozinka", Long.class);
            query.setParameter("korisnickoIme", korisnickoIme);
            query.setParameter("lozinka", lozinka);
            Long count = query.getSingleResult();

            if (count > 0) {
                // Prijava uspješna
                if (primaryController != null) {
                    TypedQuery<Korisnik.TipKorisnika> query1 = em.createQuery(
                "SELECT k.tipKorisnika FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme", Korisnik.TipKorisnika.class);
                    query1.setParameter("korisnickoIme", korisnickoIme);
                    Korisnik.TipKorisnika a = query1.getSingleResult();
                    if (a.toString().equals("REGULAR"))
                        primaryController.setKorisnickoIme(korisnickoImeField.getText(), "KORISNIK" );
                    else if (a.toString().equals("ORGANIZATOR"))
                        primaryController.setKorisnickoIme(korisnickoImeField.getText(),  "ORGANIZATOR");
                    else 
                        primaryController.setKorisnickoIme(korisnickoImeField.getText(), "ADMINISTRATOR" );


                    primaryController.hideButton(); // or whatever method to show or update primary stage
                }
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                // Prijava neuspješna
                warning.setText("Pogrešno korisničko ime ili lozinka");
            }
        } catch (Exception e) {
            System.out.println("Failed to check login credentials");
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
