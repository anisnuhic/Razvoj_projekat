package grupa8;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
//import java.util.regex.*;

public class UrediProfilController {
    @FXML
    private Label label3, label4, staroIme, staroPrezime, stariEmail, stariKorisnik, staraLozinka, novalozinka, potvrdiLozinka, staraOrg, stariKontakt, stariTelefon, staraAdresa, warning;
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
                String ime2 = novoIme.getText();
                String prezime2 = novoIme1.getText();
                String korisnickoIme2 = novoIme12.getText();
                String email2 = novoIme11.getText();
                // Ažuriraj informacije o korisniku
                if (novoIme.getText() != null && !novoIme.getText().isEmpty()){
                    try {
                        String namePattern = "^[\\p{L}]+([ '-][\\p{L}]+)*([\\s][\\p{L}]+([ '-][\\p{L}]+)*)*$";
                        // Kompajliraj pattern
                        Pattern pattern = Pattern.compile(namePattern);
                        Matcher matcher = pattern.matcher(ime2);

                        if (!matcher.matches()) {
                            warning.setText("Nevalidno ime");
                            warning.setStyle("-fx-text-fill: red;");
                            return;
                        } 
                    } catch (PatternSyntaxException e) {
                        System.out.println("Error in the regex pattern: " + e.getMessage());
                    }
                    warning.setText("");
                    korisnik.setIme(novoIme.getText());
                }
                if (novoIme1.getText() != null && !novoIme1.getText().isEmpty()){
                    try {
                        String namePattern = "^[\\p{L}]+([ '-][\\p{L}]+)*([\\s][\\p{L}]+([ '-][\\p{L}]+)*)*$";
                        // Kompajliraj pattern
                        Pattern pattern = Pattern.compile(namePattern);
                        Matcher matcher = pattern.matcher(prezime2);

                        if (!matcher.matches()) {
                            warning.setText("Nevalidno prezime");
                            warning.setStyle("-fx-text-fill: red;");
                            return;
                        } 
                    } catch (PatternSyntaxException e) {
                        System.out.println("Error in the regex pattern: " + e.getMessage());
                    }
                    warning.setText("");
                    korisnik.setPrezime(novoIme1.getText());
                }
                if (novoIme11.getText() != null && !novoIme11.getText().isEmpty()){
                    try {
                        TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(k) FROM Korisnik k WHERE k.email = :email", Long.class);
                        query.setParameter("email", email2);
                        Long count = query.getSingleResult();

                        if (count > 0 && !email2.equals(korisnik.getEmail())) {
                            warning.setText("E-mail već u upotrebi");
                            warning.setStyle("-fx-text-fill: red;");
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to check email existence");
                        e.printStackTrace();
                        return;
                    }
                    warning.setText("");
                    // Provjera da li je email u ispravnom formatu
                    try {
                        String emailPattern = "^[\\p{L}0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
                        // Kompajliraj pattern
                        Pattern pattern = Pattern.compile(emailPattern);
                        Matcher matcher = pattern.matcher(email2);

                        // Provjeri da li mail odgovara patternu
                        if (!matcher.matches()) {
                            warning.setText("Nevalidan e-mail");
                            warning.setStyle("-fx-text-fill: red;");
                            return;
                        } 
                    } catch (PatternSyntaxException e) {
                        System.out.println("Error in the regex pattern: " + e.getMessage());
                    }
                    warning.setText("");
                    korisnik.setEmail(novoIme11.getText());
                }
                if (novoIme12.getText() != null && !novoIme12.getText().isEmpty()){
                    try {
                        TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(k) FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme", Long.class);
                        query.setParameter("korisnickoIme", korisnickoIme2);
                        Long count = query.getSingleResult();

                        if (count > 0 && !korisnickoIme2.equals(korisnik.getKorisnickoIme())) {
                            warning.setText("Korisničko ime već postoji");
                            warning.setStyle("-fx-text-fill: red;");
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to check email existence");
                        e.printStackTrace();
                        return;
                    }
                    warning.setText("");
                    korisnik.setKorisnickoIme(novoIme12.getText());
                }
                // Ažuriraj lozinku samo ako se trenutna i nova lozinka podudaraju
                if (novoIme13.getText().equals(korisnik.getLozinka()) && novoIme14.getText().equals(novoIme15.getText())) {
                    korisnik.setLozinka(novoIme14.getText());
                } else {
                    warning.setText("Pogrešna lozinka");
                    warning.setStyle("-fx-text-fill: red;");
                    return;
                }
                warning.setText("");
    
                // Ako je korisnik organizator, ažuriraj podatke o organizatoru
                if (korisnik.getTipKorisnika().toString().equals("ORGANIZATOR")) {
                    Organizator organizator = findOrganizatorById(korisnik.getKorisnikId());
    
                    if (organizator != null) {
                        String kontakt2 = novoIme17.getText();
                        String telefon2 = novoIme18.getText();
                        if (novoIme16.getText() != null && !novoIme16.getText().isEmpty()) organizator.setNazivOrganizacije(novoIme16.getText());
                        if (novoIme17.getText() != null && !novoIme17.getText().isEmpty()) {
                            try {
                                String fullNamePattern = "^[\\p{L}]+([ '-][\\p{L}]+)*([\\s][\\p{L}]+([ '-][\\p{L}]+)*)*$";
                                // Kompajliraj pattern
                                Pattern pattern = Pattern.compile(fullNamePattern);
                                Matcher matcher = pattern.matcher(kontakt2);
                                // Provjera da li ime odgovara patternu
                                if (!matcher.matches()) {
                                    warning.setText("Nevalidna kontakt osoba");
                                    warning.setStyle("-fx-text-fill: red;");
                                    return;
                                }
                            } catch (PatternSyntaxException e) {
                                System.out.println("Error in the regex pattern: " + e.getMessage());
                            }
                            warning.setText("");
                            organizator.setKontaktOsoba(novoIme17.getText());
                        }
                        if (novoIme18.getText() != null && !novoIme18.getText().isEmpty()){
                            try {
                                String phoneNumberPattern = "^\\+?\\d+$";
                                // Kompajliraj pattern
                                Pattern pattern = Pattern.compile(phoneNumberPattern);
                                Matcher matcher = pattern.matcher(telefon2);
                                // Provjeri da li broj odgovara patternu
                                if (!matcher.matches()) {
                                    warning.setText("Nevalidan telefonski broj"); 
                                    warning.setStyle("-fx-text-fill: red;");
                                    return;
                                }
                            } catch (PatternSyntaxException e) {
                                System.out.println("Error in the regex pattern: " + e.getMessage());
                            }
                            warning.setText("");
                            organizator.setTelefon(novoIme18.getText());
                        }
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