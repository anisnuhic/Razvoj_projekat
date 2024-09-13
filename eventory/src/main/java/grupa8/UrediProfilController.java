package grupa8;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UrediProfilController {
    @FXML
    private Label label3, label4, staroIme, staroPrezime, stariEmail, stariKorisnik, staraLozinka, novalozinka, potvrdiLozinka, staraOrg, stariKontakt, stariTelefon, staraAdresa;
    @FXML
    private TextField novoIme, novoIme1, novoIme11, novoIme12, novoIme13, novoIme14, novoIme15, novoIme16, novoIme17, novoIme18, novoIme19;
    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
    private PrimaryController primaryController;

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }
    
    @FXML
    public void initialize(){
        Platform.runLater(()-> {
        String imeKorisnika = primaryController.getImeKorisnika();
        Korisnik korisnik = findKorisnikByUsername(imeKorisnika);

        if (korisnik != null) {
            // Prikaži informacije korisnika
            staroIme.setText(korisnik.getIme());
            staroPrezime.setText(korisnik.getPrezime());
            stariEmail.setText(korisnik.getEmail());
            stariKorisnik.setText(korisnik.getKorisnickoIme());
            // Ako je korisnik organizator, dohvati podatke o organizatoru
            if (korisnik.getTipKorisnika().toString().equals("ORGANIZATOR")) {
                Organizator organizator = findOrganizatorById(korisnik.getKorisnikId());

                if (organizator != null) {
                   staraOrg.setText(organizator.getNazivOrganizacije());
                   stariKontakt.setText(organizator.getKontaktOsoba());
                   stariTelefon.setText(organizator.getTelefon());
                   staraAdresa.setText(organizator.getAdresa());
                }

            }
            else{
                staraOrg.setVisible(false);
                    stariKontakt.setVisible(false);
                    staraAdresa.setVisible(false); 
                    stariTelefon.setVisible(false);
                    novoIme16.setVisible(false);
                    novoIme17.setVisible(false);
                    novoIme18.setVisible(false);
                    novoIme19.setVisible(false);
                    label3.setVisible(false);
                    label4.setVisible(false);
            }
        } else {
            System.out.println("Korisnik nije pronađen.");
        }
    });
    }


     // Metoda za dohvaćanje korisnika po korisničkom imenu
    private Korisnik findKorisnikByUsername(String korisnickoIme) {
        try {
            TypedQuery<Korisnik> query = em.createQuery(
                "SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme", Korisnik.class);
            query.setParameter("korisnickoIme", korisnickoIme);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Organizator findOrganizatorById(Integer id) {
        try {
            TypedQuery<Organizator> query = em.createQuery(
                "SELECT k FROM Organizator k WHERE k.organizatorId = :id", Organizator.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void izmjenaClicked(ActionEvent event) {
        // Kreiraj novi EntityManager za ovu transakciju
        EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
    
        try {
            tx.begin();  // Početak transakcije
            String imeKorisnika = primaryController.getImeKorisnika();
            Korisnik korisnik = findKorisnikByUsername(imeKorisnika);
            System.out.println(korisnik);
            System.out.println(korisnik.getIme());
            System.out.println(korisnik.getPrezime());
            System.out.println(korisnik.getEmail());
            System.out.println(korisnik.getKorisnickoIme());
            
            if (korisnik != null) {
                // Ažuriraj informacije o korisniku
                if (novoIme.getText() != null && !novoIme.getText().isEmpty()) korisnik.setIme(novoIme.getText());
                if (novoIme1.getText() != null && !novoIme1.getText().isEmpty()) korisnik.setPrezime(novoIme1.getText());
                if (novoIme11.getText() != null && !novoIme11.getText().isEmpty()) korisnik.setEmail(novoIme11.getText());
                if (novoIme12.getText() != null && !novoIme12.getText().isEmpty()) korisnik.setKorisnickoIme(novoIme12.getText());
                // Ažuriraj lozinku samo ako se trenutna i nova lozinka podudaraju
                if (novoIme13.getText().equals(korisnik.getLozinka()) && novoIme14.getText().equals(novoIme15.getText())) {
                    korisnik.setLozinka(novoIme14.getText());
                }
    
                // Ako je korisnik organizator, ažuriraj podatke o organizatoru
                if (korisnik.getTipKorisnika().toString().equals("ORGANIZATOR")) {
                    Organizator organizator = findOrganizatorById(korisnik.getKorisnikId());
    
                    if (organizator != null) {
                        if (novoIme16.getText() != null && !novoIme16.getText().isEmpty()) organizator.setNazivOrganizacije(novoIme16.getText());
                        if (novoIme17.getText() != null && !novoIme17.getText().isEmpty()) organizator.setKontaktOsoba(novoIme17.getText());
                        if (novoIme18.getText() != null && !novoIme18.getText().isEmpty()) organizator.setTelefon(novoIme18.getText());
                        if (novoIme19.getText() != null && !novoIme19.getText().isEmpty()) organizator.setAdresa(novoIme19.getText());
                        em.merge(organizator);
                    }
                }
                em.merge(korisnik);    
                tx.commit();  // Zatvori transakciju i spremi promjene
            }
    
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();  // U slučaju greške, izvrši rollback
            }
            e.printStackTrace();
        } finally {
            em.close();  // Zatvori EntityManager nakon što završi transakcija
        }
        primaryController.odjavaAction();
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
}
