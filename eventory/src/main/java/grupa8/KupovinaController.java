package grupa8;

//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
//import javafx.stage.FileChooser;

public class KupovinaController {

    @FXML
    StackPane stek;

    @FXML
    private Button akcijskiButton;

    @FXML
    private ImageView strelica1, strelica2;

    @FXML
    private Label brojKartiLabela, cijenaKartiLabela, ispis, novcanik;

    @FXML
    private VBox vBox;

    private DogadjajController dogadjajController;

    private KarticaController karticaController;

    private PrimaryController primaryController;

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    public void setDogadjajController(DogadjajController x) {
        this.dogadjajController = x;
    }

    public void setKarticaController(KarticaController x) {
        this.karticaController = x;
    }

    private int brojKarti = 0;
    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

    public int maxKarti(Dogadjaj x) {
        TypedQuery<Integer> query = em.createQuery(
                "SELECT k.maksimalanBrojKartiPoKorisniku from Karta k WHERE k.dogadjaj:= x", Integer.class);
        query.setParameter("x", x);
        return query.getSingleResult();
    }

    @FXML
    private void vratiSeNazad(ActionEvent event) {
        try {
            stek.getChildren().remove(stek.getChildren().size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAkcijskiButtonText(String text) {
        akcijskiButton.setText(text);
    }

    @FXML
    private void povecajBrojKarti(ActionEvent event) {
        if (((dogadjajController.getKartaBySektor(dogadjajController.lista_sektora.get(1),
                karticaController.getDogadjaj())).getMaksimalanBrojKartiPoKorisniku() > brojKarti))
            brojKarti++;
        else {
            // printat u labelu neki da je to max broj karti po korisniku
        }
        azurirajBrojKartiLabelu();
        azurirajUkupnuCijenu();
    }

    @FXML
    private void smanjiBrojKarti(ActionEvent event) {
        if (brojKarti > 0) {
            brojKarti--;
        }
        azurirajBrojKartiLabelu();
        azurirajUkupnuCijenu();
    }

    private void azurirajBrojKartiLabelu() {
        brojKartiLabela.setText(String.valueOf(brojKarti));
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            String imeKorisnika = dogadjajController.getImeKorisnika();
            TypedQuery<BigDecimal> query1 = em.createQuery("SELECT k.novcanik from Korisnik k WHERE k.korisnickoIme = :imeKorisnika",
                BigDecimal.class);
            query1.setParameter("imeKorisnika", imeKorisnika);
            novcanik.setText(query1.getSingleResult().toString());
            ispis.setVisible(false);
            vBox.getChildren().clear();
            strelica1.setMouseTransparent(true);
            strelica2.setMouseTransparent(true);
            if (dogadjajController != null) {
                System.out.println("DogadjajController je postavljen");
            }

            for (Sektor s : dogadjajController.lista_sektora) {
                CheckBox check = new CheckBox();
                Karta karta = dogadjajController.getKartaBySektor(s, karticaController.getDogadjaj());
                System.out.println(karticaController.getDogadjaj());
                check.setText(s.getNazivSektora() + ": " + karta.getCijena() + "KM");
                check.setStyle("-fx-text-fill: white;");

                // Dodaj listener za izbor jednog checkbox-a
                check.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        // Poništi sve ostale checkboxove
                        for (var node : vBox.getChildren()) {
                            if (node instanceof CheckBox && node != check) {
                                ((CheckBox) node).setSelected(false);
                            }
                        }
                    }
                    azurirajUkupnuCijenu();
                });

                vBox.getChildren().add(check);
            }

        });
    }

    private void azurirajUkupnuCijenu() {
        // Pronađi odabrani sektor
        for (var node : vBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    // Izdvoji cijenu iz naziva CheckBox-a (pretpostavljam da je format: "Naziv
                    // sektora: XXKM")
                    String text = checkBox.getText();
                    String[] parts = text.split(":");
                    if (parts.length > 1) {
                        String cijenaString = parts[1].trim().replace("KM", "");
                        try {
                            double cijenaKarte = Double.parseDouble(cijenaString);
                            // Izračunaj ukupnu cijenu
                            double ukupnaCijena = brojKarti * cijenaKarte;
                            // Prikaz ukupne cijene u labeli
                            cijenaKartiLabela.setText(String.format("%.2f KM", ukupnaCijena));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void handleStanjeKarti(ActionEvent event) {
        EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

        Rezervacija rezervacija = new Rezervacija();
        
        //kod za postavljanje statusa rezervacije
        Rezervacija.Status status;
        if(akcijskiButton.getText().equals("Kupi")) status = Rezervacija.Status.KUPLJENO;
        else if (akcijskiButton.getText().equals("Rezerviši"))status = Rezervacija.Status.REZERVISANO;
        else status = Rezervacija.Status.OTKAZANO;
        // kod za dohvatanje dogadjaja
        String nazivDogadjaja = dogadjajController.getNazivDogadjaja();
        TypedQuery<Dogadjaj> query = em.createQuery("SELECT d from Dogadjaj d WHERE d.naziv = :nazivDogadjaja",
                Dogadjaj.class);
        query.setParameter("nazivDogadjaja", nazivDogadjaja);
        Dogadjaj dogadjaj = query.getSingleResult();

        // kod za dohvatanje lokacije na kojoj ce se dogadjaj manifestovati
        Lokacija lokacija = dogadjaj.getLokacija();

        // kod za dohvatanje ukupne cijene
        String broj = cijenaKartiLabela.getText().replaceAll("[^\\d.]", "");
        BigDecimal d = new BigDecimal(broj);

        // dohvatanje korisnika
        String imeKorisnika = dogadjajController.getImeKorisnika();
        TypedQuery<Korisnik> query1 = em.createQuery("SELECT k from Korisnik k WHERE k.korisnickoIme = :imeKorisnika",
                Korisnik.class);
        query1.setParameter("imeKorisnika", imeKorisnika);
        Korisnik korisnik = query1.getSingleResult();

        //kod za dohvatanje sektora
        String nazivSektora = "";
        for (var node : vBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    String text = checkBox.getText();
                    String[] parts = text.split(":");
                    if (parts.length > 1) {
                        nazivSektora = parts[0].trim();
                    }
                }
            }
        }
        //dohvatanje sektora na osnovu naziva i lokacije
        TypedQuery<Sektor> query2 = em.createQuery("SELECT s from Sektor s WHERE s.nazivSektora = :nazivSektora AND s.lokacija = :lokacija", Sektor.class);
        query2.setParameter("nazivSektora", nazivSektora);
        query2.setParameter("lokacija", lokacija);
        Sektor sektor = query2.getSingleResult();

        TypedQuery<Karta> query3 = em.createQuery("SELECT k FROM Karta k WHERE k.sektor = :sektor AND k.dogadjaj = :dogadjaj", Karta.class);
        query3.setParameter("sektor", sektor);
        query3.setParameter("dogadjaj", dogadjaj);
        Karta karta = query3.getSingleResult();
        // popunjavanje objekta Rezervacija
        rezervacija.setKorisnik(korisnik);
        rezervacija.setKarta(karta);
        rezervacija.setDatumRezervacije(LocalDateTime.now());
        rezervacija.setStatus(status);
        rezervacija.setKolicina(brojKarti);
        rezervacija.setUkupnaCijena(d);
        korisnik.setNovcanik(korisnik.getNovcanik().subtract(d));
        try{
            em.getTransaction().begin();
            em.persist(rezervacija); 
            em.merge(korisnik);
            em.getTransaction().commit();
            ispis.setVisible(true);

        }
        catch (Exception e){
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        }
        if(akcijskiButton.getText().equals("Kupi")){
            //logika za download pdf-a
    }
    }
}
