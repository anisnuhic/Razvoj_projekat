package grupa8;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.application.Platform;
import javafx.event.ActionEvent;

// import com.itextpdf.layout.element.Image;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class MojeKarteController {

    private PrimaryController primaryController;

    @FXML
    private ImageView slikaDogadjaja;
    @FXML
    private Label cijenaDogadjaja, nazivDogadjaja, sektorDogadjaja, slovoDogadjaja, stanje;
    @FXML
    private Button kupi;
    private Karta karta ;
    private int brojKarti;
    private Korisnik korisnik;

     EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    public void postaviIzgledKarte(String naziv, String cijena, String slikaUrl, String sektor, String status, Integer broj) {
        brojKarti = broj;
        cijenaDogadjaja.setText("Cijena: " + cijena);
        nazivDogadjaja.setText(naziv);
        sektorDogadjaja.setText(sektor.toUpperCase());
        slovoDogadjaja.setText(String.valueOf(sektor.charAt(0)).toUpperCase());
        slikaDogadjaja.setImage(new Image(getClass().getResourceAsStream(slikaUrl)));
        if(status.equals("REZERVISANO"))kupi.setVisible(true);
        else kupi.setVisible(false);
        stanje.setText(status + ": " + broj);

    }

    public void inicijaliziraj(Karta karta, Korisnik korisnik){
        this.karta = karta;
        this.korisnik = korisnik;
        System.out.println("Korisnik u kontroleru:" + this.korisnik);
        System.out.println("Karta u kontroleru:" + this.karta);

    }
    @FXML
private void kupiButtonAction(ActionEvent event) {
    TypedQuery<Rezervacija> query = em.createQuery("SELECT r FROM Rezervacija r WHERE r.karta = :karta AND r.korisnik = :korisnik", Rezervacija.class);
    query.setParameter("karta", karta);
    query.setParameter("korisnik", korisnik);
    List<Rezervacija> rezervacije = query.getResultList();

    
    try {
        em.getTransaction().begin();
        for (Rezervacija x : rezervacije) {
            x.setStatus(Rezervacija.Status.KUPLJENO);
            em.merge(x);
        }
        em.getTransaction().commit();
        kupi.setVisible(false);
        stanje.setText("Kupljeno: " + brojKarti); 
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback(); 
        }
        e.printStackTrace();  
    } 
}

}
